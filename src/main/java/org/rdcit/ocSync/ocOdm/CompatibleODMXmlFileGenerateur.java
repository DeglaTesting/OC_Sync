/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocOdm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.rdcit.ocSync.model.*;
import org.rdcit.ocSync.ocws.ImportData_ws;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "CompatibleODMXmlFileGenerateur")
public class CompatibleODMXmlFileGenerateur {

    public List<Study> lStudy;
     File file;
     

    public CompatibleODMXmlFileGenerateur() {
        lStudy = new ArrayList();
        file = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\test.xml");
    }

    public String generateOdmXmlFile() {
        UpdateOIDs updateOIDs = new UpdateOIDs();
        CompatibleODMXmlFileGenerateur compatibleODMXmlFileGenerateur = new CompatibleODMXmlFileGenerateur();
        compatibleODMXmlFileGenerateur.lStudy = updateOIDs.updatelSourceDataStudy();
        compatibleODMXmlFileGenerateur.writeTheOdmXmlFile();
        return "confirmation.xhtml";
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
   
    /* public static void main(String[] args) {
        //   CollectingClinicalData collectingClinicalData = new CollectingClinicalData();
        // collectingClinicalData.collectingClinicalData();
        UpdateOIDs updateOIDs = new UpdateOIDs();
        updateOIDs.updatelSourceDataStudy();
        CompatibleODMXmlFileGenerateur compatibleODMXmlFileGenerateur = new CompatibleODMXmlFileGenerateur();
        compatibleODMXmlFileGenerateur.lStudy = (List<Study>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("lStudySourceClinicalData");
        //  compatibleODMXmlFileGenerateur.lStudy = collectingClinicalData.getlStudy();
        System.out.println("@@@@@@@@@@@@@@@@ " + compatibleODMXmlFileGenerateur.lStudy.size());
        compatibleODMXmlFileGenerateur.writeTheOdmXmlFile();
    }*/
    public File writeTheOdmXmlFile() {
       // File file = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\test.xml");

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("ODM");
            /*  rootElement.setAttribute("xsi:schemaLocationxsi:schemaLocation", "http://www.cdisc.org/ns/odm/v1.3 ODM1-3.xsd");
            rootElement.setAttribute("ODMVersion", "1.3");
            rootElement.setAttribute("FileType", "Snapshot");
            rootElement.setAttribute("FileOID", "1D20080412202420");
            rootElement.setAttribute("Description", "1D20080412202420");
            rootElement.setAttribute("CreationDateTime", "1D20080412202420");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttribute("xmlns", "http://www.cdisc.org/ns/odm/v1.3");*/

            doc.appendChild(rootElement);
            for (int i = 0; i < this.lStudy.size(); i++) {
                Element clinicalData = doc.createElement("ClinicalData");
                clinicalData.setAttribute("StudyOID", lStudy.get(i).getStudyOID());
                List<Subject> lSubject = lStudy.get(i).getlSubject();
                for (int h = 0; h < lSubject.size(); h++) {
                    Element subjectData = doc.createElement("SubjectData");
                    subjectData.setAttribute("SubjectKey", lSubject.get(h).getSubjectOID());
                    List<StudyEvent> lSubjectStudyEvent = lSubject.get(h).getlSubjectstudyEvent();
                    for (int j = 0; j < lSubjectStudyEvent.size(); j++) {
                        Element studyEventData = doc.createElement("StudyEventData");
                        studyEventData.setAttribute("StudyEventOID", lSubjectStudyEvent.get(j).getEventOID());
                        studyEventData.setAttribute("StudyEventRepeatKey", lSubjectStudyEvent.get(j).getEventRepeatingKey());
                        List<StudyEventForm> lStudyEventForm = lSubjectStudyEvent.get(j).getlStudyEventForm();
                        for (int k = 0; k < lStudyEventForm.size(); k++) {
                            Element formData = doc.createElement("FormData");
                            formData.setAttribute("FormOID", lStudyEventForm.get(k).getFormOID());
                            List<ItemGroup> lItemGroup = lStudyEventForm.get(k).getlItemGroup();
                            for (int l = 0; l < lItemGroup.size(); l++) {
                                Element itemgroupData = doc.createElement("ItemGroupData");
                                itemgroupData.setAttribute("TransactionType", "Insert");
                                itemgroupData.setAttribute("ItemGroupOID", lItemGroup.get(l).getItemGroupOID());
                                itemgroupData.setAttribute("ItemGroupRepeatKey", lItemGroup.get(l).getItemGroupRepeatingKey());
                                List<Item> lItem = lItemGroup.get(l).getlItem();
                                for (int m = 0; m < lItem.size(); m++) {
                                    Element itemData = doc.createElement("ItemData");
                                    itemData.setAttribute("Value", lItem.get(m).getItemValue());
                                    itemData.setAttribute("ItemOID", lItem.get(m).getItemOID());
                                    itemgroupData.appendChild(itemData);
                                }
                                formData.appendChild(itemgroupData);
                            }
                            studyEventData.appendChild(formData);
                        }
                        subjectData.appendChild(studyEventData);
                    }

                    clinicalData.appendChild(subjectData);
                }
                rootElement.appendChild(clinicalData);
            }
            ImportData_ws importData_ws = new ImportData_ws(doc);
            importData_ws.createSOAPRequest();
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
            // Output to console for testing
            /* StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);*/

        } catch (ParserConfigurationException | TransformerException ex) {
            System.out.println(ex.getMessage());
        }
        return file;
    }
}
