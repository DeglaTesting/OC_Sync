/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocOdm;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.xml.soap.SOAPMessage;
import org.rdcit.ocSync.model.*;
import org.rdcit.ocSync.ocws.*;

/**
 *
 * @author sa841
 */
public class UpdateOIDs {

    List<OIDMapper> lOIDMapper;
    List<LogStructure> lOcWsResponse;
    List<Study> lSourceDataStudy;

    public UpdateOIDs() {
        CollectingClinicalData collectingClinicalData = new CollectingClinicalData();
        lSourceDataStudy = collectingClinicalData.collectingClinicalData();
        lOIDMapper = (List<OIDMapper>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("OIDMapperList");
        lOcWsResponse = new ArrayList();
    }

    public List<Study> updatelSourceDataStudy() {
        updateClinicalData();
        setNamesSourceStudyClinicalData();
        updateStudyOIDs();
        return this.lSourceDataStudy;
    }

    public void updateStudyClinicalData() {
        List<Subject> lSubject = lSourceDataStudy.get(0).getlSubject();
        for (int j = 0; j < lSubject.size(); j++) {
            IsStudySubject_ws isStudySubject_ws = new IsStudySubject_ws(getTargetStudyPUID(lSourceDataStudy.get(0).getStudyOID()), lSubject.get(j).getSubjectID());
            SOAPMessage sp = isStudySubject_ws.createSOAPRequest();
            if (isStudySubject_ws.isStudySubjectExist(sp)) {
                lOcWsResponse.add(isStudySubject_ws.getLogStructure());
                lSubject.get(j).setSubjectOID(isStudySubject_ws.getStudySubjectOID(sp));
                List<StudyEvent> lSubjectStudyEvent = lSubject.get(j).getlSubjectstudyEvent();
                for (int k = 0; k < lSubjectStudyEvent.size(); k++) {
                    int maxSource = getMaxSourceStudyEventRepeatingKey(lSubjectStudyEvent.get(k).getEventOID(), lSubjectStudyEvent);
                    String sTargetStudyEventOID = getTargetStudyEventOID(lSubjectStudyEvent.get(k).getEventName(), lSourceDataStudy.get(0).getStudyOID());
                    ScheduledSubjectEvent scheduledSubjectEvent = new ScheduledSubjectEvent(sTargetStudyEventOID, lSubject.get(j).getSubjectID());
                    int maxTarget = Integer.parseInt(scheduledSubjectEvent.getScheduledSubjectEventNumber());
                    if (maxTarget < maxSource) {
                        for (int l = maxSource; l < maxTarget; l++) {
                            ScheduleSubjectEvent_ws scheduleSubjectevent_ws = new ScheduleSubjectEvent_ws(lSubject.get(j).getSubjectID(), getTargetStudyPUID(lSourceDataStudy.get(0).getStudyOID()), sTargetStudyEventOID);
                            scheduleSubjectevent_ws.createSOAPRequest();
                            lOcWsResponse.add(scheduleSubjectevent_ws.getLogStructure());
                        }
                    }
                }
            } else {
                CreateStudySubject_ws createStudySubject = new CreateStudySubject_ws(getTargetStudyPUID(lSourceDataStudy.get(0).getStudyOID()), lSubject.get(j).getSubjectID(), lSubject.get(j).getSubjectGendre(), lSubject.get(j).getSubjectDateOfBirth());
                createStudySubject.createSOAPRequest();
                lOcWsResponse.add(createStudySubject.getLogStructure());
                IsStudySubject_ws isStudySubject_ws2 = new IsStudySubject_ws(getTargetStudyPUID(lSourceDataStudy.get(0).getStudyOID()), lSubject.get(j).getSubjectID());
                SOAPMessage sp2 = isStudySubject_ws2.createSOAPRequest();
                lSubject.get(j).setSubjectOID(isStudySubject_ws.getStudySubjectOID(sp2));
                List<StudyEvent> lSubjectStudyEvent = lSubject.get(j).getlSubjectstudyEvent();
                for (int k = 0; k < lSubjectStudyEvent.size(); k++) {
                    String eventOID = getTargetStudyEventOID(lSubjectStudyEvent.get(k).getEventName(), lSourceDataStudy.get(0).getStudyOID());
                    ScheduleSubjectEvent_ws scheduleSubjectevent_ws = new ScheduleSubjectEvent_ws(lSubject.get(j).getSubjectID(), getTargetStudyPUID(lSourceDataStudy.get(0).getStudyOID()), eventOID);
                    scheduleSubjectevent_ws.createSOAPRequest();
                    lOcWsResponse.add(scheduleSubjectevent_ws.getLogStructure());
                }
            }
        }
    }

    public void updateSitesClinicalData() {
        for (int i = 1; i < lSourceDataStudy.size(); i++) {
            List<Subject> lSubject = lSourceDataStudy.get(i).getlSubject();
            for (int j = 0; j < lSubject.size(); j++) {
                String targetStudyPUID = getTargetStudyPUID(lSourceDataStudy.get(0).getStudyOID());
                IsStudySubject_ws isStudySubject_ws = new IsStudySubject_ws(targetStudyPUID, lSubject.get(j).getSubjectID(), getTargetSitePUID(lSourceDataStudy.get(i).getStudyOID(), targetStudyPUID));
                SOAPMessage sp = isStudySubject_ws.createSOAPRequest();
                if (isStudySubject_ws.isStudySubjectExist(sp)) {
                    lOcWsResponse.add(isStudySubject_ws.getLogStructure());
                    lSubject.get(j).setSubjectOID(isStudySubject_ws.getStudySubjectOID(sp));
                    List<StudyEvent> lSubjectStudyEvent = lSubject.get(j).getlSubjectstudyEvent();
                    for (int k = 0; k < lSubjectStudyEvent.size(); k++) {
                        int maxSource = getMaxSourceStudyEventRepeatingKey(lSubjectStudyEvent.get(k).getEventOID(), lSubjectStudyEvent);
                        String sTargetStudyEventOID = getTargetStudyEventOID(lSubjectStudyEvent.get(k).getEventName(), lSourceDataStudy.get(i).getStudyOID());
                        ScheduledSubjectEvent scheduledSubjectEvent = new ScheduledSubjectEvent(sTargetStudyEventOID, lSubject.get(j).getSubjectID());
                        int maxTarget = Integer.parseInt(scheduledSubjectEvent.getScheduledSubjectEventNumber());
                        if (maxTarget < maxSource) {
                            for (int l = maxSource; l < maxTarget; l++) {
                                ScheduleSubjectEvent_ws scheduleSubjectevent_ws = new ScheduleSubjectEvent_ws(lSubject.get(j).getSubjectID(), targetStudyPUID, getTargetSitePUID(lSourceDataStudy.get(i).getStudyOID(), targetStudyPUID), sTargetStudyEventOID);
                                scheduleSubjectevent_ws.createSOAPRequest();
                                lOcWsResponse.add(scheduleSubjectevent_ws.getLogStructure());
                            }
                        }
                    }
                } else {
                    CreateStudySubject_ws createStudySubject = new CreateStudySubject_ws(targetStudyPUID, getTargetSitePUID(lSourceDataStudy.get(i).getStudyOID(), targetStudyPUID), lSubject.get(j).getSubjectID(), lSubject.get(j).getSubjectGendre(), lSubject.get(j).getSubjectDateOfBirth());
                    createStudySubject.createSOAPRequest();
                    lOcWsResponse.add(createStudySubject.getLogStructure());
                    IsStudySubject_ws isStudySubject_ws2 = new IsStudySubject_ws(targetStudyPUID, lSubject.get(j).getSubjectID(), getTargetSitePUID(lSourceDataStudy.get(i).getStudyOID(), targetStudyPUID));
                    SOAPMessage sp2 = isStudySubject_ws2.createSOAPRequest();
                    lSubject.get(j).setSubjectOID(isStudySubject_ws.getStudySubjectOID(sp2));
                    List<StudyEvent> lSubjectStudyEvent = lSubject.get(j).getlSubjectstudyEvent();
                    for (int k = 0; k < lSubjectStudyEvent.size(); k++) {
                        String eventOID = getTargetStudyEventOID(lSubjectStudyEvent.get(k).getEventName(), lSourceDataStudy.get(i).getStudyOID());
                        ScheduleSubjectEvent_ws scheduleSubjectevent_ws = new ScheduleSubjectEvent_ws(lSubject.get(j).getSubjectID(), targetStudyPUID, getTargetSitePUID(lSourceDataStudy.get(i).getStudyOID(), targetStudyPUID), eventOID);
                        scheduleSubjectevent_ws.createSOAPRequest();
                        lOcWsResponse.add(scheduleSubjectevent_ws.getLogStructure());
                    }
                }
            }
        }
    }

    public void updateClinicalData() {
        updateStudyClinicalData();
        updateSitesClinicalData();
    }

    public int getMaxSourceStudyEventRepeatingKey(String studyEventOID, List<StudyEvent> lStudyEvent) {
        int max = 0;
        for (int i = 0; i < lStudyEvent.size(); i++) {
            if (lStudyEvent.get(i).getEventOID().equals(studyEventOID)) {
                if (max < Integer.parseInt(lStudyEvent.get(i).getEventRepeatingKey())) {
                    max = Integer.parseInt(lStudyEvent.get(i).getEventRepeatingKey());
                }
            }
        }
        return max;
    }

    public void setNamesSourceStudyClinicalData() {
        for (int r = 0; r < this.lSourceDataStudy.size(); r++) {
            for (int i = 0; i < this.lOIDMapper.size(); i++) {
                Study sourceStudy = this.lSourceDataStudy.get(r);
                Study targetStudy = this.lOIDMapper.get(i).getSourceStudy();
                if (sourceStudy.getStudyOID().equals(targetStudy.getStudyOID())) {
                    sourceStudy.setStudyName(targetStudy.getStudyName());
                    List<Subject> lSubject = sourceStudy.getlSubject();
                    for (int s = 0; s < lSubject.size(); s++) {
                        List<StudyEvent> lSourceStudyEvent = lSubject.get(s).getlSubjectstudyEvent();
                        List<StudyEvent> lTargetStudyEvent = targetStudy.getlStudyEvent();
                        for (int j = 0; j < lSourceStudyEvent.size(); j++) {
                            for (int k = 0; k < lTargetStudyEvent.size(); k++) {
                                if (lSourceStudyEvent.get(j).getEventOID().equals(lTargetStudyEvent.get(k).getEventOID())) {
                                    lSourceStudyEvent.get(j).setEventName(lTargetStudyEvent.get(k).getEventName());
                                    List<StudyEventForm> lSourceStudyForm = lSourceStudyEvent.get(j).getlStudyEventForm();
                                    List<StudyEventForm> lTargetStudyForm = lTargetStudyEvent.get(k).getlStudyEventForm();
                                    for (int l = 0; l < lSourceStudyForm.size(); l++) {
                                        for (int m = 0; m < lTargetStudyForm.size(); m++) {
                                            if (lSourceStudyForm.get(l).getFormOID().equals(lTargetStudyForm.get(m).getFormOID())) {
                                                lSourceStudyForm.get(l).setFormName(lTargetStudyForm.get(m).getFormName());
                                                List<ItemGroup> lSourceGroupItem = lSourceStudyForm.get(l).getlItemGroup();
                                                List<ItemGroup> lTargetGroupItem = lTargetStudyForm.get(m).getlItemGroup();
                                                for (int n = 0; n < lSourceGroupItem.size(); n++) {
                                                    for (int o = 0; o < lTargetGroupItem.size(); o++) {
                                                        if (lSourceGroupItem.get(n).getItemGroupOID().equals(lTargetGroupItem.get(o).getItemGroupOID())) {
                                                            lSourceGroupItem.get(n).setItemGroupName(lTargetGroupItem.get(o).getItemGroupName());
                                                            List<Item> lSourceItem = lSourceGroupItem.get(n).getlItem();
                                                            List<Item> lTargetItem = lTargetGroupItem.get(o).getlItem();
                                                            for (int p = 0; p < lSourceItem.size(); p++) {
                                                                for (int q = 0; q < lTargetItem.size(); q++) {
                                                                    if (lSourceItem.get(p).getItemOID().equals(lTargetItem.get(q).getItemOID())) {
                                                                        lSourceItem.get(p).setItemName(lTargetItem.get(q).getItemName());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateStudyOIDs() {
        for (int r = 0; r < this.lSourceDataStudy.size(); r++) {
            for (int i = 0; i < this.lOIDMapper.size(); i++) {
                Study sourceStudy = this.lSourceDataStudy.get(r);
                Study targetStudy = this.lOIDMapper.get(i).getTargetStudy();
                if (sourceStudy.getStudyName().equals(targetStudy.getStudyName())) {
                    sourceStudy.setStudyOID(targetStudy.getStudyOID());
                    List<Subject> lSubject = sourceStudy.getlSubject();
                    for (int s = 0; s < lSubject.size(); s++) {
                        List<StudyEvent> lSourceStudyEvent = lSubject.get(s).getlSubjectstudyEvent();
                        List<StudyEvent> lTargetStudyEvent = targetStudy.getlStudyEvent();
                        for (int j = 0; j < lSourceStudyEvent.size(); j++) {
                            for (int k = 0; k < lTargetStudyEvent.size(); k++) {
                                if (lSourceStudyEvent.get(j).getEventName().equals(lTargetStudyEvent.get(k).getEventName())) {
                                    lSourceStudyEvent.get(j).setEventOID(lTargetStudyEvent.get(k).getEventOID());
                                    List<StudyEventForm> lSourceStudyForm = lSourceStudyEvent.get(j).getlStudyEventForm();
                                    List<StudyEventForm> lTargetStudyForm = lTargetStudyEvent.get(k).getlStudyEventForm();
                                    for (int l = 0; l < lSourceStudyForm.size(); l++) {
                                        for (int m = 0; m < lTargetStudyForm.size(); m++) {
                                            if (lSourceStudyForm.get(l).getFormName().equals(lTargetStudyForm.get(m).getFormName())) {
                                                lSourceStudyForm.get(l).setFormOID(lTargetStudyForm.get(m).getFormOID());
                                                List<ItemGroup> lSourceGroupItem = lSourceStudyForm.get(l).getlItemGroup();
                                                List<ItemGroup> lTargetGroupItem = lTargetStudyForm.get(m).getlItemGroup();
                                                for (int n = 0; n < lSourceGroupItem.size(); n++) {
                                                    for (int o = 0; o < lTargetGroupItem.size(); o++) {
                                                        if (lSourceGroupItem.get(n).getItemGroupName().equals(lTargetGroupItem.get(o).getItemGroupName())) {
                                                            lSourceGroupItem.get(n).setItemGroupOID(lTargetGroupItem.get(o).getItemGroupOID());
                                                            List<Item> lSourceItem = lSourceGroupItem.get(n).getlItem();
                                                            List<Item> lTargetItem = lTargetGroupItem.get(o).getlItem();
                                                            for (int p = 0; p < lSourceItem.size(); p++) {
                                                                for (int q = 0; q < lTargetItem.size(); q++) {
                                                                    if (lSourceItem.get(p).getItemName().equals(lTargetItem.get(q).getItemName())) {
                                                                        lSourceItem.get(p).setItemOID(lTargetItem.get(q).getItemOID());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getTargetStudyPUID(String sourceStudyOID) {
        String sStudyPUID = "";
        for (int i = 0; i < lOIDMapper.size(); i++) {
            if (lOIDMapper.get(i).getSourceStudy().getStudyOID().equals(sourceStudyOID)) {
                sStudyPUID = lOIDMapper.get(i).getTargetStudy().getStudyUPID();
                break;
            }
        }
        return sStudyPUID;
    }

    public String getTargetSitePUID(String sourceStudyOID, String TargetSudyPUID) {
        String sSitePUID = "";
        for (int i = 0; i < lOIDMapper.size(); i++) {
            if (lOIDMapper.get(i).getSourceStudy().getStudyOID().equals(sourceStudyOID)) {
                sSitePUID = lOIDMapper.get(i).getTargetStudy().getStudyUPID();
                sSitePUID = sSitePUID.replaceFirst(TargetSudyPUID, "");
                sSitePUID = sSitePUID.replaceFirst(" - ", "");
                break;
            }
        }
        return sSitePUID;
    }

    public String getTargetStudyEventOID(String sourceStudyEventName, String sourceStudyOID) {
        String sTargetStudyEventOID = "";
        for (int i = 0; i < lOIDMapper.size(); i++) {
            if (lOIDMapper.get(i).getSourceStudy().getStudyOID().equals(sourceStudyOID)) {
                List<StudyEvent> lTargetStudyEvent = lOIDMapper.get(i).getTargetStudy().getlStudyEvent();
                for (int j = 0; j < lTargetStudyEvent.size(); j++) {
                    if (lTargetStudyEvent.get(j).getEventName().equals(sourceStudyEventName)) {
                        sTargetStudyEventOID = lTargetStudyEvent.get(j).getEventOID();
                        break;
                    }
                }
            }
        }
        return sTargetStudyEventOID;
    }

    public List<LogStructure> getlOcWsResponse() {
        return lOcWsResponse;
    }

    public void setlOcWsResponse(List<LogStructure> lOcWsResponse) {
        this.lOcWsResponse = lOcWsResponse;
    }

}
