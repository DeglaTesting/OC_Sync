/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import org.mockito.runners.MockitoJUnitRunner;
import org.rdcit.ocSync.model.Study;
import org.rdcit.ocSync.utils.ToDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author sa841
 */
@RunWith(MockitoJUnitRunner.class)
public class CollectingStudiesTest {

 @Mock Node nStudy;
    @Mock ToDocument toDocument;
    static Document document;
    CollectingStudies collectingStudies = new CollectingStudies();
 @Ignore
    @BeforeClass
    public static void initTestDependencies() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        File file = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\src\\test\\Study1.xml");
        document = dBuilder.parse(file);
    }
  @Ignore
    @Test
    public void getStudyNameTest() {
        // given
        String studyName = null;
        doReturn(document).when(toDocument).nodeToDocument(nStudy);
        // When
        document = toDocument.nodeToDocument(nStudy);
        NodeList nlStudy = document.getElementsByTagName("StudyName");
        Node nStudyName = nlStudy.item(0);
        if ((nStudyName.getNodeType() == Node.ELEMENT_NODE)) {
            Element eStudyName = (Element) nStudyName;
            studyName = eStudyName.getTextContent();
        }
        // then
        assertTrue(studyName.equals("Study 1"));
    }
 @Ignore
    @Test
    public void getStudyProtocolName() {
        // given
        String ProtocolName = null;
        doReturn(document).when(toDocument).nodeToDocument(nStudy);
        // When
        document = toDocument.nodeToDocument(nStudy);
        NodeList nlStudy = document.getElementsByTagName("ProtocolName");
        Node nStudyProtocolName = nlStudy.item(0);
        if ((nStudyProtocolName.getNodeType() == Node.ELEMENT_NODE)) {
            Element eStudyProtocolName = (Element) nStudyProtocolName;
            ProtocolName = eStudyProtocolName.getTextContent();
        }
        // then
        assertTrue(ProtocolName.equals("Study1"));
    }
 @Ignore
    @Test
    public void getStudyParamsTest() {
        // given
        String[] studyParams = new String[3];
        doReturn(document).when(toDocument).nodeToDocument(nStudy);
        // When
        document = toDocument.nodeToDocument(nStudy);
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
        //then
        assertTrue(studyParams[0].equals("1"));
        assertTrue(studyParams[1].equals("required"));
        assertTrue(studyParams[2].equals("true"));
    }
 @Ignore
    @Test
    public void collectingStudiesTest() {
        List<Study> lStudy = collectingStudies.collectingStudies(document);
        assertTrue(lStudy.size() == 1);
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CollectingStudiesTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successfull " + result.wasSuccessful());
    }
}
