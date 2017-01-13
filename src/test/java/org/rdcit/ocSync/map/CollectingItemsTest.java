/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;
import org.rdcit.ocSync.model.Item;
import org.rdcit.ocSync.model.ItemGroup;
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
public class CollectingItemsTest {

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
    public void collectingItemsTest() {
        // given
        ItemGroup mockItemGroup = mock(ItemGroup.class);
        doReturn("IG_SYNCC_FIRSTGROUP").when(mockItemGroup).getItemGroupOID();
        //when
        NodeList nlItemGroup = document.getElementsByTagName("ItemGroupDef");
        for (int i = 0; i < nlItemGroup.getLength(); i++) {
            Node nItemGroup = nlItemGroup.item(i);
            if (nItemGroup.getNodeType() == Node.ELEMENT_NODE) {
                Element eItemGroup = (Element) nItemGroup;
                if (eItemGroup.getAttribute("OID").equals(mockItemGroup.getItemGroupOID())) {
                    NodeList nlItem = nItemGroup.getChildNodes();
                    for (int j = 0; j < nlItem.getLength(); j++) {
                        Node nItem = nlItem.item(j);
                        if ((nItem.getNodeType() == Node.ELEMENT_NODE) && (nItem.getNodeName().equals("ItemRef"))) {
                            Element eItem = (Element) nItem;
                            Item item = new Item(eItem.getAttribute("ItemOID"));
                            mockItemGroup.addItem(item);
                        }
                    }
                }
            }
        }
        //then (Need the verify if the add method is called ONLY ONCE because the mock object has only one item)
        verify(mockItemGroup, times(1)).addItem((Item) anyObject()); //Use verify if the method return VOID type 
    }

    @Ignore
    @Test
    public void findItemNameTest() {
        // given 
        String itemOID = "I_SYNCC_FIRST_NAME";
        Item mockITem = mock(Item.class);
        // Then
        NodeList nlItem = document.getElementsByTagName("ItemDef");
        for (int i = 0; i < nlItem.getLength(); i++) {
            Node nItem = nlItem.item(i);
            if (nItem.getNodeType() == Node.ELEMENT_NODE) {
                Element eItem = (Element) nItem;
                if (eItem.getAttribute("OID").equals(itemOID)) {
                    mockITem.setItemName(eItem.getAttribute("Name"));
                }
            }
        }
        // Then
        verify(mockITem).setItemName("First_Name");
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CollectingItemsTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The tests were successful: " + result.wasSuccessful());
    }

}
