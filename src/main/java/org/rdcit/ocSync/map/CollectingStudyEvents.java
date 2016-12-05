/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import java.util.ArrayList;
import java.util.List;
import org.rdcit.ocSync.model.Study;
import org.rdcit.ocSync.model.StudyEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sa841
 */
public class CollectingStudyEvents {

    Study study;
    Document document;

    public CollectingStudyEvents(Document document, Study study) {
        this.study = study;
        this.document = document;
    }

    public List<StudyEvent> collectingSiteEvents() {
        Document studyDoc = getStudyDocument();
        List<StudyEvent> lStudyEvent = new ArrayList();
        NodeList nlStudyEvent = studyDoc.getElementsByTagName("StudyEventRef");
        for (int i = 0; i < nlStudyEvent.getLength(); i++) {
            Node nStudyEvent = nlStudyEvent.item(i);
            if ((nStudyEvent.getNodeType() == Node.ELEMENT_NODE)) {
                Element eStydyEvent = (Element) nStudyEvent;
                StudyEvent studyEvent = new StudyEvent(eStydyEvent.getAttribute("StudyEventOID"));
                studyEvent.setEventName(getStuydEventName(eStydyEvent.getAttribute("StudyEventOID")));
                 System.out.println(studyEvent.toString());
                lStudyEvent.add(studyEvent);
                 this.study.addStudyEvent(studyEvent);
            }
        }
       
        return lStudyEvent;
    }

    public List<StudyEvent> collectingStudyEvents() {
        List<StudyEvent> lStudyEvent = new ArrayList();
        NodeList nlStudyEvent = this.document.getElementsByTagName("StudyEventDef");
        for (int i = 0; i < nlStudyEvent.getLength(); i++) {
            Node nStudyEvent = nlStudyEvent.item(i);
            if ((nStudyEvent.getNodeType() == Node.ELEMENT_NODE)) {
                Element eStydyEvent = (Element) nStudyEvent;
                StudyEvent studyEvent = new StudyEvent(eStydyEvent.getAttribute("OID"));
                studyEvent.setEventName(eStydyEvent.getAttribute("Name"));
                System.out.println(studyEvent.toString());
                lStudyEvent.add(studyEvent);
                this.study.addStudyEvent(studyEvent);
            }
        }
        return lStudyEvent;
    }

    public Document getStudyDocument() {
        Document studyDoc = null;
        NodeList nlStudy = document.getElementsByTagName("Study");
        for (int i = 0; i < nlStudy.getLength(); i++) {
            Node nStudy = nlStudy.item(i);
            if ((nStudy.getNodeType() == Node.ELEMENT_NODE)) {
                Element eStudy = (Element) nStudy;
                if (eStudy.getAttribute("OID").equals(this.study.getStudyOID())) {
                    ToDocument toDocument = new ToDocument();
                    studyDoc = toDocument.nodeToDocument(nStudy);
                }
            }
        }
        return studyDoc;
    }

    public String getStuydEventName(String studyEventOID) {
        String studyEventName = null;
        NodeList nlStudyEvent = this.document.getElementsByTagName("StudyEventDef");
        for (int i = 0; i < nlStudyEvent.getLength(); i++) {
            Node nStudyEvent = nlStudyEvent.item(i);
            if ((nStudyEvent.getNodeType() == Node.ELEMENT_NODE)) {
                Element eStudyEvent = (Element) nStudyEvent;
                if (eStudyEvent.getAttribute("OID").equals(studyEventOID)) {
                   studyEventName = eStudyEvent.getAttribute("Name");
                }
            }
        }
        return studyEventName;
    }
}
