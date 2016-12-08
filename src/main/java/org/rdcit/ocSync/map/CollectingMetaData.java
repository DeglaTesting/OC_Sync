/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import org.rdcit.ocSync.model.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.rdcit.ocSync.model.Study;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author sa841
 */
public class CollectingMetaData {
    
    File file;

    public CollectingMetaData(File file) {
        this.file = file;
    }
    
     public CollectingMetaData() {}
    
    public Document openFile() {
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(this.file);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return doc;
    }

    public List<Study> collectingMetaDataFromFile() {
        Document doc = openFile();
        CollectingStudies CollectingStudies = new CollectingStudies();
        List<Study> lStudy = CollectingStudies.collectingStudies(doc);
        if (lStudy.size() > 1) {
            collectingSitesMetaData(doc, lStudy);
        } else {
            collectingStudyMetaData(doc, lStudy);
        }
        return lStudy;
    }
    
     public List<Study> collectingMetaDataFromDoc(Document doc) {
        CollectingStudies CollectingStudies = new CollectingStudies();
        List<Study> lStudy = CollectingStudies.collectingStudies(doc);
        if (lStudy.size() > 1) {
            collectingSitesMetaData(doc, lStudy);
        } else {
            collectingStudyMetaData(doc, lStudy);
        }
        return lStudy;
    }

    public void collectingSitesMetaData(Document doc, List<Study> lSite) {
        for (int i = 0; i < lSite.size(); i++) {
            CollectingStudyEvents collectingEvents = new CollectingStudyEvents(doc, lSite.get(i));
            List<StudyEvent> lStudyEvent = collectingEvents.collectingSiteEvents();
            for (int j = 0; j < lStudyEvent.size(); j++) {
                CollectingStudyEventForms collectingStudyEventForms = new CollectingStudyEventForms();
                List<StudyEventForm> lStudyEventForm = collectingStudyEventForms.collectingStudyEventForms(doc, lStudyEvent.get(j));
                for (int k = 0; k < lStudyEventForm.size(); k++) {
                    CollectingItemGroups collectingItemGroups = new CollectingItemGroups();
                    List<ItemGroup> lItemGroup = collectingItemGroups.collectingItemGroup(doc, lStudyEventForm.get(k));
                    for (int l = 0; l < lItemGroup.size(); l++) {
                        CollectingItems collectingItems = new CollectingItems();
                        List<Item> lItem = collectingItems.collectingItems(doc, lItemGroup.get(l));
                    }
                                   }
            }
        }
    }

    public void collectingStudyMetaData(Document doc, List<Study> lStudy) {
        CollectingStudyEvents collectingEvents = new CollectingStudyEvents(doc, lStudy.get(0));
        List<StudyEvent> lStudyEvent = collectingEvents.collectingStudyEvents();
        for (int j = 0; j < lStudyEvent.size(); j++) {
            CollectingStudyEventForms collectingStudyEventForms = new CollectingStudyEventForms();
            List<StudyEventForm> lStudyEventForm = collectingStudyEventForms.collectingStudyEventForms(doc, lStudyEvent.get(j));
            for (int k = 0; k < lStudyEventForm.size(); k++) {
                CollectingItemGroups collectingItemGroups = new CollectingItemGroups();
                List<ItemGroup> lItemGroup = collectingItemGroups.collectingItemGroup(doc, lStudyEventForm.get(k));
                for (int l = 0; l < lItemGroup.size(); l++) {
                    CollectingItems collectingItems = new CollectingItems();
                    List<Item> lItem = collectingItems.collectingItems(doc, lItemGroup.get(l));
                }
            }
        }
    }
}
