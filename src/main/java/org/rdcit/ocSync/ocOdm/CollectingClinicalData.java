/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocOdm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.rdcit.ocSync.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author sa841
 */
public class CollectingClinicalData {

    List<Study> lStudy;
    File file;

    public CollectingClinicalData() {
        this.file = UploadedFile.sourceUploadedFile;
        lStudy = new ArrayList();
    }

    public Document openFile() {
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return doc;
    }

    public void collectingStudies(Document doc) {
        NodeList nlClinicalData = doc.getElementsByTagName("ClinicalData");
        for (int i = 0; i < nlClinicalData.getLength(); i++) {
            Node nClinicalData = nlClinicalData.item(i);
            if (nClinicalData.getNodeType() == Node.ELEMENT_NODE) {
                Element eClinicalData = (Element) nClinicalData;
                Study study = new Study(eClinicalData.getAttribute("StudyOID"));
                this.lStudy.add(study);
            }
        }
    }

    public List<Study> collectingClinicalData() {
        Document doc = openFile();
        collectingStudies(doc);
        for (int i = 0; i < this.lStudy.size(); i++) {
            CollectingSubjectClinicalData collectingSubjects = new CollectingSubjectClinicalData(this.lStudy.get(i));
            collectingSubjects.collectingStudySubjectsClinicalData(doc);
        }
        return this.lStudy;
    }

    public List<Study> getlStudy() {
        return lStudy;
    }

    public void setlStudy(List<Study> lStudy) {
        this.lStudy = lStudy;
    }

}
