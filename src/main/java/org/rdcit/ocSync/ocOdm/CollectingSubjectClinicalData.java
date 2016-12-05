/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocOdm;

import java.util.ArrayList;
import java.util.List;
import org.rdcit.ocSync.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sa841
 */
public class CollectingSubjectClinicalData {
    
    Study study;
    List<Subject> lSubject;
    
    public CollectingSubjectClinicalData(Study study) {
        this.study = study;
        lSubject = new ArrayList();
    }
    
    public List<Subject> collectingStudySubjectsClinicalData(Document doc) {
        NodeList nlClinicalData = doc.getElementsByTagName("ClinicalData");
        for (int m = 0; m < nlClinicalData.getLength(); m++) {
            Node nClinicalData = nlClinicalData.item(m);
            if (nClinicalData.getNodeType() == Node.ELEMENT_NODE) {
                Element eClinicalData = (Element) nClinicalData;
                if (eClinicalData.getAttribute("StudyOID").equals(this.study.getStudyOID())) {
                    NodeList nlSubjectData = nClinicalData.getChildNodes();
                    for (int n = 0; n < nlSubjectData.getLength(); n++) {
                        Node nSubjectData = nlSubjectData.item(n);
                        if (nSubjectData.getNodeName().equals("SubjectData") && (nSubjectData.getNodeType() == Node.ELEMENT_NODE)) {
                            Element eSubjectData = (Element) nSubjectData;
                            Subject subject = new Subject(eSubjectData.getAttribute("OpenClinica:StudySubjectID"),
                                    eSubjectData.getAttribute("OpenClinica:UniqueIdentifier"),
                                    eSubjectData.getAttribute("OpenClinica:Sex"),
                                    eSubjectData.getAttribute("OpenClinica:DateOfBirth"));
                            NodeList nlStudyEventData = nSubjectData.getChildNodes();
                            for (int i = 0; i < nlStudyEventData.getLength(); i++) {
                                Node nStudyEventData = nlStudyEventData.item(i);
                                if (nStudyEventData.getNodeName().equals("StudyEventData") && nStudyEventData.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eStudyEventData = (Element) nStudyEventData;
                                    StudyEvent studyEvent = new StudyEvent(eStudyEventData.getAttribute("StudyEventOID"), eStudyEventData.getAttribute("StudyEventRepeatKey"));
                                    NodeList nlStudyFormData = nStudyEventData.getChildNodes();
                                    for (int j = 0; j < nlStudyFormData.getLength(); j++) {
                                        Node nStudyFormData = nlStudyFormData.item(j);
                                        if (nStudyFormData.getNodeName().equals("FormData") && nStudyFormData.getNodeType() == Node.ELEMENT_NODE) {
                                            Element eStudyFormData = (Element) nStudyFormData;
                                            StudyEventForm subjectStudyForm = new StudyEventForm(eStudyFormData.getAttribute("FormOID"));
                                            studyEvent.addStudyEventForm(subjectStudyForm);
                                            NodeList nlStudyItemGroupData = nStudyFormData.getChildNodes();
                                            for (int k = 0; k < nlStudyItemGroupData.getLength(); k++) {
                                                Node nStudyItemGroupData = nlStudyItemGroupData.item(k);
                                                if (nStudyItemGroupData.getNodeName().equals("ItemGroupData") && nStudyItemGroupData.getNodeType() == Node.ELEMENT_NODE) {
                                                    Element eStudyItemGroupData = (Element) nStudyItemGroupData;
                                                    ItemGroup subjectItemGroup = new ItemGroup(eStudyItemGroupData.getAttribute("ItemGroupOID"), eStudyItemGroupData.getAttribute("ItemGroupRepeatingKey"));
                                                    subjectStudyForm.addItemGroup(subjectItemGroup);
                                                    NodeList nlStudyItemData = nStudyItemGroupData.getChildNodes();
                                                    for (int l = 0; l < nlStudyItemData.getLength(); l++) {
                                                        Node nStudyItemData = nlStudyItemData.item(l);
                                                        if (nStudyItemData.getNodeName().equals("ItemData") && nStudyItemData.getNodeType() == Node.ELEMENT_NODE) {
                                                            Element eStudyItemData = (Element) nStudyItemData;
                                                            Item subjectItem = new Item(eStudyItemData.getAttribute("ItemOID"), eStudyItemData.getAttribute("Value"));
                                                            subjectItemGroup.addItem(subjectItem);
                                                        }
                                                    }
                                                    
                                                }
                                            }
                                        }
                                    }
                                    subject.addSubjectStudyEvent(studyEvent);
                                }
                            }
                            this.study.addSubject(subject);
                            lSubject.add(subject);
                        }
                    }
                }
            }
        }
        setSubjectStudyEventNames(doc);
        return lSubject;
    }
    
    public void setStudyEventName(StudyEvent studyEvent, Document doc) {
        NodeList nlStudyEventDef = doc.getElementsByTagName("StudyEventDef");
        for (int i = 0; i < nlStudyEventDef.getLength(); i++) {
            Node nStudyEventDef = nlStudyEventDef.item(i);
            if (nStudyEventDef.getNodeType() == Node.ELEMENT_NODE) {
                Element eStudyEventDef = (Element) nStudyEventDef;
                if (eStudyEventDef.getAttribute("OID").equals(studyEvent.getEventOID())) {
                    studyEvent.setEventName(eStudyEventDef.getAttribute("Name"));
                }
            }
        }
    }
    
    public void setSubjectStudyEventNames(Document doc) {
        for (int i = 0; i < lSubject.size(); i++) {
            List<StudyEvent> lStudyEvent = lSubject.get(i).getlSubjectstudyEvent();
            for (int j = 0; j < lStudyEvent.size(); j++) {
                setStudyEventName(lStudyEvent.get(j), doc);
            }
        } 
    }
}
