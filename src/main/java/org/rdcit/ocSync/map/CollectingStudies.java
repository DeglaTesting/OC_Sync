/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import org.rdcit.ocSync.utils.ToDocument;
import java.util.ArrayList;
import java.util.List;
import org.rdcit.ocSync.model.Study;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sa841
 */
public class CollectingStudies {

    public List<Study> collectingStudies(Document document) {
        List<Study> lStudy = new ArrayList();
        NodeList nlStudy = document.getElementsByTagName("Study");
        for (int i = 0; i < nlStudy.getLength(); i++) {
            Node nStudy = nlStudy.item(i);
            if ((nStudy.getNodeType() == Node.ELEMENT_NODE)) {
                Element eStudy = (Element) nStudy;
                Study study = new Study(eStudy.getAttribute("OID"));
                NodeList nlStudyChildren = nStudy.getChildNodes();
                for (int j = 0; j < nlStudyChildren.getLength(); j++) {
                    if (nlStudyChildren.item(j).getNodeName().equals("GlobalVariables")) {
                        study.setStudyName(getStudyName(nlStudyChildren.item(j)));
                        study.setStudyUPID(getStudyProtocolName(nlStudyChildren.item(j)));
                        break;
                    }
                }
                study.setStudyParams(getStudyParams(nStudy));
                lStudy.add(study);
            }
        }
        return lStudy;
    }

    public String getStudyName(Node nStudy) {
        String studyName = null;
        ToDocument toDocument = new ToDocument();
        Document document = toDocument.nodeToDocument(nStudy);
        NodeList nlStudy = document.getElementsByTagName("StudyName");
        Node nStudyName = nlStudy.item(0);
        if ((nStudyName.getNodeType() == Node.ELEMENT_NODE)) {
            Element eStudyName = (Element) nStudyName;
            studyName = eStudyName.getTextContent();
        }
        return studyName;
    }

    public String getStudyProtocolName(Node nStudy) {
        String studyName = null;
        ToDocument toDocument = new ToDocument();
        Document document = toDocument.nodeToDocument(nStudy);
        NodeList nlStudy = document.getElementsByTagName("ProtocolName");
        Node nStudyProtocolName = nlStudy.item(0);
        if ((nStudyProtocolName.getNodeType() == Node.ELEMENT_NODE)) {
            Element eStudyProtocolName = (Element) nStudyProtocolName;
            studyName = eStudyProtocolName.getTextContent();
        }
        return studyName;
    }

    public String[] getStudyParams(Node nStudy) {
        String[] studyParams = new String[3];
        ToDocument toDocument = new ToDocument();
        Document document = toDocument.nodeToDocument(nStudy);
        NodeList nlStudyParamListRef = document.getElementsByTagName("OpenClinica:StudyParameterListRef");
        for (int i = 0; i < nlStudyParamListRef.getLength(); i++) {
            Node nStudyParamListRef = nlStudyParamListRef.item(i);
            if ((nStudyParamListRef.getNodeType() == Node.ELEMENT_NODE)) {      
                Element eStudyParamListRef = (Element) nStudyParamListRef;
                if (eStudyParamListRef.getAttribute("StudyParameterListID").equals("SPL_collectDob")) {
                    studyParams[0] = eStudyParamListRef.getAttribute("Value");
                }
                if (eStudyParamListRef.getAttribute("StudyParameterListID").equals("SPL_subjectPersonIdRequired")) {
                    studyParams[1] = eStudyParamListRef.getAttribute("Value");
                }
                if (eStudyParamListRef.getAttribute("StudyParameterListID").equals("SPL_genderRequired")) {
                    studyParams[2] = eStudyParamListRef.getAttribute("Value");
                }
            }
        }
        return studyParams;
    }
}
