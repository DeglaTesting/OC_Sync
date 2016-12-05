/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.rdcit.ocSync.controller.UserClick;
import org.rdcit.ocSync.model.*;
import org.rdcit.ocSync.ocws.StudyMetaData_ws;
import org.w3c.dom.Document;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "Mapper")
public class Mapper {

    @ManagedProperty(value = "#{UserClick}")
    UserClick userClick;
    File sourceFile = UploadedFile.sourceUploadedFile;
    //File sourceFile = new File("C:\\Users\\sa841\\Documents\\source_xml.xml");
    File targetFile = new File("C:\\Users\\sa841\\Documents\\Study1.xml");
    List<Study> lSourceStudy;
    List<Study> lTargetStudy;
    List<Structure> lStructure;
    List<Study> lStructure1;
    List<EmptyStructure> lEmptyStructure;
    List<MissingStructure> lMissingStructure;
    List<ImproperStructure> lImproperMissingStructure;
    List<ImproperStructure> lImproperEmptyStructure;
    boolean areTheyMatch;
    Object[] resMapping;
    List<OIDMapper> lOIDMapper;

    public void preMapping() {
        try {
            CollectingMetaData collectingSourceMetaData = new CollectingMetaData(sourceFile);
            lSourceStudy = collectingSourceMetaData.collectingMetaDataFromFile();
            Study SelectedStudy = (Study) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedStudy");
            StudyMetaData_ws studyMetaData_ws = new StudyMetaData_ws();
            Document selectedStudyDoc = studyMetaData_ws.createSOAPRequest(SelectedStudy.getStudyUPID());
            CollectingMetaData collectingTargetMetaData = new CollectingMetaData();
            lTargetStudy = collectingTargetMetaData.collectingMetaDataFromDoc(selectedStudyDoc);
            lStructure = new ArrayList();
            lEmptyStructure = new ArrayList();
            lMissingStructure = new ArrayList();
            lImproperMissingStructure = new ArrayList();
            lImproperEmptyStructure = new ArrayList();
            lOIDMapper = new ArrayList();
            lStructure1 = new ArrayList();
            areTheyMatch = false;
            resMapping = new Object[5];
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Structure> getlStructure() {
        return lStructure;
    }

    public List<EmptyStructure> getlEmptyStructure() {
        return lEmptyStructure;
    }

    public List<MissingStructure> getlMissingStructure() {
        return lMissingStructure;
    }

    public Object[] mapping() {
        preMapping();
        try {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ " + lSourceStudy.size());
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ " + lTargetStudy.size());
            for (int i = 0; i < lSourceStudy.size(); i++) {
                for (int j = 0; j < lTargetStudy.size(); j++) {
                    if (lSourceStudy.get(i).getStudyUPID().equals(lTargetStudy.get(j).getStudyUPID())) {
                        List<StudyEvent> lSourceStudyEvent = lSourceStudy.get(i).getlStudyEvent();
                        List<StudyEvent> lTargetStudyEvent = lTargetStudy.get(j).getlStudyEvent();
                        for (int k = 0; k < lSourceStudyEvent.size(); k++) {
                            for (int l = 0; l < lTargetStudyEvent.size(); l++) {
                                if (lSourceStudyEvent.get(k).getEventName().equals(lTargetStudyEvent.get(l).getEventName())) {
                                    List<StudyEventForm> lSourceStudyEventFrom = lSourceStudyEvent.get(k).getlStudyEventForm();
                                    List<StudyEventForm> lTargetStudyEventFrom = lTargetStudyEvent.get(l).getlStudyEventForm();
                                    for (int m = 0; m < lSourceStudyEventFrom.size(); m++) {
                                        for (int n = 0; n < lTargetStudyEventFrom.size(); n++) {
                                            if (lSourceStudyEventFrom.get(m).getFormName().equals(lTargetStudyEventFrom.get(n).getFormName())) {
                                                List<ItemGroup> lSourceItemGroup = lSourceStudyEventFrom.get(m).getlItemGroup();
                                                List<ItemGroup> lTargetItemGroup = lTargetStudyEventFrom.get(n).getlItemGroup();
                                                for (int o = 0; o < lSourceItemGroup.size(); o++) {
                                                    for (int p = 0; p < lTargetItemGroup.size(); p++) {
                                                        if (lSourceItemGroup.get(o).getItemGroupName().equals(lTargetItemGroup.get(p).getItemGroupName())) {
                                                            List<Item> lSourceItem = lSourceItemGroup.get(o).getlItem();
                                                            List<Item> lTargetItem = lTargetItemGroup.get(p).getlItem();
                                                            int nItemChecked = 0;
                                                            for (int q = 0; q < lSourceItem.size(); q++) {
                                                                for (int r = 0; r < lTargetItem.size(); r++) {
                                                                    if (lSourceItem.get(q).getItemName().equals(lTargetItem.get(r).getItemName())) {
                                                                        Structure structure = new Structure(lSourceStudy.get(i).getStudyName(), lSourceStudyEvent.get(k).getEventName(), lSourceStudyEventFrom.get(m).getFormName(), lSourceItem.get(q).getItemName());
                                                                        lStructure.add(structure);
                                                                        nItemChecked++;
                                                                        break;
                                                                    } else if (r == lTargetItem.size() - 1) {
                                                                        MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(i).getStudyOID(), lSourceStudyEvent.get(k).getEventName(), lSourceStudyEventFrom.get(m).getFormName(), lSourceItem.get(q).getItemName());
                                                                        lMissingStructure.add(missingStructure);
                                                                        ImproperStructure improperStructure = new ImproperStructure(lSourceStudy.get(i), lSourceStudyEvent.get(k), lSourceStudyEventFrom.get(m), lSourceItem.get(q));
                                                                        lImproperMissingStructure.add(improperStructure);
                                                                    }
                                                                }
                                                            }

                                                            if (nItemChecked < lTargetItem.size() - 1) {
                                                                for (int q = 0; q < lTargetItem.size(); q++) {
                                                                    Structure structure = new Structure(lTargetStudy.get(i).getStudyName(), lTargetStudyEvent.get(k).getEventName(), lTargetStudyEventFrom.get(m).getFormName(), lTargetItem.get(q).getItemName());
                                                                    if (!contains(lStructure, structure)) {
                                                                        EmptyStructure emptyStructure = new EmptyStructure(lTargetStudy.get(i).getStudyOID(), lTargetStudyEvent.get(k).getEventName(), lTargetStudyEventFrom.get(m).getFormName(), lTargetItem.get(q).getItemName());
                                                                        lEmptyStructure.add(emptyStructure);
                                                                        ImproperStructure improperStructure = new ImproperStructure(lTargetStudy.get(i), lTargetStudyEvent.get(k), lTargetStudyEventFrom.get(m), lTargetItem.get(q));
                                                                        lImproperEmptyStructure.add(improperStructure);
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            } else if (n == lTargetStudyEventFrom.size() - 1) {
                                                MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(i).getStudyOID(), lSourceStudyEvent.get(k).getEventName(), lSourceStudyEventFrom.get(m).getFormName());
                                                lMissingStructure.add(missingStructure);
                                                ImproperStructure improperStructure = new ImproperStructure(lTargetStudy.get(i), lTargetStudyEvent.get(k), lTargetStudyEventFrom.get(m));
                                                lImproperMissingStructure.add(improperStructure);
                                            }
                                        }
                                    }
                                    break;
                                } else if (l == lTargetStudyEvent.size() - 1) {
                                    MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(i).getStudyOID(), lSourceStudyEvent.get(k).getEventName());
                                    lMissingStructure.add(missingStructure);
                                    ImproperStructure improperStructure = new ImproperStructure(lTargetStudy.get(i), lTargetStudyEvent.get(l));
                                    lImproperMissingStructure.add(improperStructure);
                                }
                            }
                        }
                        lStructure1.add(lSourceStudy.get(i));
                        OIDMapper oidMapper = new OIDMapper(lSourceStudy.get(i), lTargetStudy.get(j));
                        lOIDMapper.add(oidMapper);
                        break;
                    } else if (j == lTargetStudy.size() - 1) {
                        MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(i).getStudyOID());
                        lMissingStructure.add(missingStructure);
                        ImproperStructure improperStructure = new ImproperStructure(lTargetStudy.get(j));
                        lImproperMissingStructure.add(improperStructure);
                    }
                }
            }

            /* System.out.println("############## Structure ######################");
        printArray(getlStructure());
        System.out.println("############## EmptyStructure ######################");
        printArray(lEmptyStructure);
        System.out.println("############## MissingStructure ######################");
        printArray(getlMissingStructure());*/
            userClick.setFormLodder("Map");
            if (lMissingStructure.isEmpty()) {
                areTheyMatch = true;
            }
            resMapping[0] = areTheyMatch;
            resMapping[1] = lStructure;
            resMapping[2] = lEmptyStructure;
            resMapping[3] = lMissingStructure;
            resMapping[4] = lStructure1;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("OIDMapperList", lOIDMapper);
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Format Error", "Sorry, but the uploded file is not well formatted.");
            FacesContext.getCurrentInstance().addMessage("formatErrorMSG", message);
            System.out.println(ex.getMessage());
        }
        return resMapping;
    }

    public void printArray(List al) {
        for (int i = 0; i < al.size(); i++) {
            System.out.println(al.get(i).toString());
        }
    }

    public boolean contains(List<Structure> lStructure, Structure structure) {
        boolean contains = false;
        for (int i = 0; i < lStructure.size(); i++) {
            if (lStructure.get(i).equals(structure)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public Mapper() {
    }

    public UserClick getUserClick() {
        return userClick;
    }

    public void setUserClick(UserClick userClick) {
        this.userClick = userClick;
    }

    public List<OIDMapper> getlOIDMapper() {
        return lOIDMapper;
    }

    public void setlOIDMapper(List<OIDMapper> lOIDMapper) {
        this.lOIDMapper = lOIDMapper;
    }

    public static void main(String args[]) {
        Mapper mapper = new Mapper();
        mapper.mapping();
    }

}
