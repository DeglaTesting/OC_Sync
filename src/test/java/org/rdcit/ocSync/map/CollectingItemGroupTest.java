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
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rdcit.ocSync.model.ItemGroup;
import org.rdcit.ocSync.model.StudyEventForm;
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
public class CollectingItemGroupTest {

    static Document document;
    @Mock
    StudyEventForm studyEventForm;

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
    public void collectingItemGroup() {
        //given 
        doReturn("F_SYNCCRF_V01").when(studyEventForm).getFormOID();
        //when
        List<ItemGroup> lItemGroup = new ArrayList();
        NodeList nlFormDef = document.getElementsByTagName("FormDef");
        for (int i = 0; i < nlFormDef.getLength(); i++) {
            Node nFormDef = nlFormDef.item(i);
            if (nFormDef.getNodeType() == Node.ELEMENT_NODE) {
                Element eFormDef = (Element) nFormDef;
                if (eFormDef.getAttribute("OID").equals(studyEventForm.getFormOID())) {
                    NodeList nlItemGroupRef = nFormDef.getChildNodes();
                    for (int j = 0; j < nlItemGroupRef.getLength(); j++) {
                        Node nItemGroupRef = nlItemGroupRef.item(j);
                        if ((nItemGroupRef.getNodeType() == Node.ELEMENT_NODE) && (nItemGroupRef.getNodeName().equals("ItemGroupRef"))) {
                            Element eItemGroupRef = (Element) nItemGroupRef;
                            ItemGroup itemGroup = new ItemGroup(eItemGroupRef.getAttribute("ItemGroupOID"));
                            // findItemGroupName(document, eItemGroupRef.getAttribute("ItemGroupOID"), itemGroup);
                            studyEventForm.addItemGroup(itemGroup);
                            lItemGroup.add(itemGroup);
                        }
                    }
                }
            }
        }
        //then
        assertTrue(lItemGroup.size() == 2);
    }

    @Ignore
    @Test
    public void findItemGroupNameTest() {
        //given
        String itemGroupOID = "IG_SYNCC_FIRSTGROUP";
        ItemGroup mockItemGroup = mock(ItemGroup.class);
        doCallRealMethod().when(mockItemGroup).setItemGroupName(anyString());
        doCallRealMethod().when(mockItemGroup).getItemGroupName(); //Should put this stub for the assertion, otherwise, the mock does not know what it should do in the assertion call and the test will Fail retruning "null Object"
        //when
        NodeList nlGroupDef = document.getElementsByTagName("ItemGroupDef");
        for (int i = 0; i < nlGroupDef.getLength(); i++) {
            Node nGroupDef = nlGroupDef.item(i);
            if (nGroupDef.getNodeType() == Node.ELEMENT_NODE) {
                Element eGroupDef = (Element) nGroupDef;
                if (eGroupDef.getAttribute("OID").equals(itemGroupOID)) {
                    mockItemGroup.setItemGroupName(eGroupDef.getAttribute("Name"));
                }
            }
        }
        // then
        assertTrue(mockItemGroup.getItemGroupName().equals("FirstGroup"));
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CollectingItemGroupTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successfull " + result.wasSuccessful());
    }
}
