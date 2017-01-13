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
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import static org.mockito.Matchers.anyObject;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;
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
@RunWith(MockitoJUnitRunner.class)
public class CollectingStudyEventFormsTest {

  static Document document;
    @Mock
    StudyEvent studyEvent;

    @Ignore
    @BeforeClass
    public static void initTestDependencies() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        File file = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\src\\test\\Study2.xml");
        document = dBuilder.parse(file);
    }

    @Ignore
    @Test
    public void findStudyEventFormName() {
        // given
        doReturn("SE_FIRSTEVENT_6182").when(studyEvent).getEventOID();
        List<StudyEventForm> lStudyEventForm = mock(List.class);
        // when
        // List<StudyEventForm> lStudyEventForm = new ArrayList();
        NodeList nlMetaDataVersion = document.getElementsByTagName("MetaDataVersion");
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
                            studyEvent.addStudyEventForm(studyEventForm);
                            lStudyEventForm.add(studyEventForm);
                        }
                    }
                }
            }
        }
        // then  
        verify(studyEvent, times(1)).addStudyEventForm((StudyEventForm) anyObject());
    }

    @Ignore
    @Test
    public void findStudyEventFormNameTest() {
        // given 
        String eventFormOID = "F_SYNCCRF_V01";
        String eventFormName = null;
        //when
        NodeList nlMetaDataVersion = document.getElementsByTagName("MetaDataVersion");
        NodeList nlStudyEventDef = nlMetaDataVersion.item(0).getChildNodes();
        for (int i = 0; i < nlStudyEventDef.getLength(); i++) {
            if ((nlStudyEventDef.item(i).getNodeType() == Node.ELEMENT_NODE) && (nlStudyEventDef.item(i).getNodeName().equals("FormDef"))) {
                Element eStudyEventDef = (Element) nlStudyEventDef.item(i);
                if (eStudyEventDef.getAttribute("OID").equals(eventFormOID)) {
                    eventFormName = eStudyEventDef.getAttribute("Name");
                }
            }
        }
        //then
        assertTrue(eventFormName.equals("SyncCRF - v0.1"));
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CollectingStudyEventFormsTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successfull: " + result.wasSuccessful());
    }
}
