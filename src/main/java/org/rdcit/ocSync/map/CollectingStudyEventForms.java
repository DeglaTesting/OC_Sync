/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import java.util.ArrayList;
import java.util.List;
import org.rdcit.ocSync.model.StudyEvent;
import org.rdcit.ocSync.model.StudyEventForm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sa841
 */
public class CollectingStudyEventForms {

    public List<StudyEventForm> collectingStudyEventForms(Document doc, StudyEvent studyEvent) {
        List<StudyEventForm> lStudyEventForm = new ArrayList();
        NodeList nlMetaDataVersion = doc.getElementsByTagName("MetaDataVersion");
        NodeList nlStudyEventDef = nlMetaDataVersion.item(0).getChildNodes();
        for (int i = 0; i < nlStudyEventDef.getLength(); i++) {
            if ((nlStudyEventDef.item(i).getNodeType() == Node.ELEMENT_NODE) && (nlStudyEventDef.item(i).getNodeName().equals("StudyEventDef"))) {
                Element eStudyEventDef = (Element) nlStudyEventDef.item(i);
                if (eStudyEventDef.getAttribute("OID").equals(studyEvent.getEventOID())) {
                    NodeList nlFormRef = nlStudyEventDef.item(i).getChildNodes();
                    for (int j = 0; j < nlFormRef.getLength(); j++) {
                        Node nFormRef = nlFormRef.item(j);
                        if ((nFormRef.getNodeType() == Node.ELEMENT_NODE) && (nFormRef.getNodeName().equals("FormRef"))) {
                            Element eFormRef = (Element) nFormRef;
                            StudyEventForm studyEventForm = new StudyEventForm(eFormRef.getAttribute("FormOID"));
                            findStudyEventFormName(doc, eFormRef.getAttribute("FormOID"), studyEventForm);
                            studyEvent.addStudyEventForm(studyEventForm);
                            lStudyEventForm.add(studyEventForm);
                        }
                    }
                }
            }
        }
        return lStudyEventForm;
    }

    public void findStudyEventFormName(Document doc, String eventFormOID, StudyEventForm studyEventForm) {
        NodeList nlMetaDataVersion = doc.getElementsByTagName("MetaDataVersion");
        NodeList nlStudyEventDef = nlMetaDataVersion.item(0).getChildNodes();
        for (int i = 0; i < nlStudyEventDef.getLength(); i++) {
            if ((nlStudyEventDef.item(i).getNodeType() == Node.ELEMENT_NODE) && (nlStudyEventDef.item(i).getNodeName().equals("FormDef"))) {
                Element eStudyEventDef = (Element) nlStudyEventDef.item(i);
                if (eStudyEventDef.getAttribute("OID").equals(eventFormOID)) {
                    studyEventForm.setFormName(eStudyEventDef.getAttribute("Name"));
                }
            }
        }
    }

}
