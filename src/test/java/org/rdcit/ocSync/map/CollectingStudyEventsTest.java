/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.assertNotNull;
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
import org.rdcit.ocSync.model.StudyEvent;
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
public class CollectingStudyEventsTest {

   @Mock
    Study study;
    static Document document;

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
    public void getStudyDocumentTest() {
        //given
        Document studyDoc = null;
        NodeList nlStudy = document.getElementsByTagName("Study");
        doReturn("S_STUDY1").when(study).getStudyOID();
        //when
        Node nStudy = nlStudy.item(0); //Removed The For LOOP (The test file has only one study)
        if ((nStudy.getNodeType() == Node.ELEMENT_NODE)) {
            Element eStudy = (Element) nStudy;
            if (eStudy.getAttribute("OID").equals(study.getStudyOID())) {
                ToDocument toDocument = new ToDocument();
                studyDoc = toDocument.nodeToDocument(nStudy);
            }
        }
        //then
        assertNotNull(studyDoc);
    }

    @Ignore
    @Test
    public void getStuydEventNameTest() {
        // given
        String studyEventName = null;
        String studyEventOID = "SE_FIRSTEVENT_6182";
        // when
        NodeList nlStudyEvent = document.getElementsByTagName("StudyEventDef");
        for (int i = 0; i < nlStudyEvent.getLength(); i++) {
            Node nStudyEvent = nlStudyEvent.item(i);
            if ((nStudyEvent.getNodeType() == Node.ELEMENT_NODE)) {
                Element eStudyEvent = (Element) nStudyEvent;
                if (eStudyEvent.getAttribute("OID").equals(studyEventOID)) {
                    studyEventName = eStudyEvent.getAttribute("Name");
                }
            }
        }
        //then
        assertTrue(studyEventName.equals("First event"));
    }

    @Ignore
    @Test
    public void collectingStudySiteEventsTest() {
        // given
        List<StudyEvent> lStudyEvent = new ArrayList();
        NodeList nlStudyEvent = document.getElementsByTagName("StudyEventDef");
        // when
        for (int i = 0; i < nlStudyEvent.getLength(); i++) {
            Node nStudyEvent = nlStudyEvent.item(i);
            if ((nStudyEvent.getNodeType() == Node.ELEMENT_NODE)) {
                Element eStydyEvent = (Element) nStudyEvent;
                StudyEvent studyEvent = new StudyEvent(eStydyEvent.getAttribute("OID"));
                studyEvent.setEventName(eStydyEvent.getAttribute("Name"));
                lStudyEvent.add(studyEvent);
                this.study.addStudyEvent(studyEvent);
            }
        }
        // then
        assertTrue(lStudyEvent.size() == 1);
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CollectingStudyEventsTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successfull " + result.wasSuccessful());
    }
}
