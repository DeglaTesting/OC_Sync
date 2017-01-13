/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocOdm;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import org.rdcit.ocSync.map.CollectingStudiesTest;
import org.rdcit.ocSync.model.Item;
import org.rdcit.ocSync.model.ItemGroup;
import org.rdcit.ocSync.model.Study;
import org.rdcit.ocSync.model.StudyEvent;
import org.rdcit.ocSync.model.StudyEventForm;
import org.rdcit.ocSync.model.Subject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author sa841
 */
public class CollectingClinicalDataTest {

    static Document document;
    CollectingClinicalData ccd = new CollectingClinicalData();
    CollectingClinicalData spyCcd = spy(ccd);

    @BeforeClass
    public static void initTestDependencies() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        File file = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\src\\test\\Study1.xml");
        document = dBuilder.parse(file);
    }

    @Test
    public void openFileTest() {
        document = spyCcd.openFile();
        assertNotNull(document);
    }

    @Test
    public void collectingStudiesTest() {
        spyCcd.collectingStudies(document);
        assertTrue(spyCcd.lStudy.size() == 1);
    }

    @Test
    public void mockCollectingSubjectClinicalDataTest() {
        Study study = mock(Study.class);
        doReturn("S_STUDY1").when(study).getStudyOID();
        //when
        NodeList nlClinicalData = document.getElementsByTagName("ClinicalData");
        Node nClinicalData = nlClinicalData.item(0);
        if (nClinicalData.getNodeType() == Node.ELEMENT_NODE) {
            Element eClinicalData = (Element) nClinicalData;
            if (eClinicalData.getAttribute("StudyOID").equals(study.getStudyOID())) {
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
                        study.addSubject(subject);
                    }
                }
            }
        }

        assertTrue(study.getlSubject().size() == 1);
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CollectingStudiesTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successfull " + result.wasSuccessful());
    }

}
