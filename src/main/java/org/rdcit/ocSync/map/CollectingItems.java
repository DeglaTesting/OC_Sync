/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rdcit.ocSync.map;


import java.util.ArrayList;
import java.util.List;
import org.rdcit.ocSync.model.Item;
import org.rdcit.ocSync.model.ItemGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sa841
 */
public class CollectingItems {

    public List<Item> collectingItems(Document doc, ItemGroup itemGroup) {
        List<Item> lItem = new ArrayList();
        NodeList nlItemGroup = doc.getElementsByTagName("ItemGroupDef");
        for (int i = 0; i < nlItemGroup.getLength(); i++) {
            Node nItemGroup = nlItemGroup.item(i);
            if (nItemGroup.getNodeType() == Node.ELEMENT_NODE) {
                Element eItemGroup = (Element) nItemGroup;
                if (eItemGroup.getAttribute("OID").equals(itemGroup.getItemGroupOID())) {
                    NodeList nlItem = nItemGroup.getChildNodes();
                    for (int j = 0; j < nlItem.getLength(); j++) {
                        Node nItem = nlItem.item(j);
                        if ((nItem.getNodeType() == Node.ELEMENT_NODE) && (nItem.getNodeName().equals("ItemRef"))) {
                            Element eItem = (Element)nItem;
                            Item item = new Item(eItem.getAttribute("ItemOID"));
                            findItemName(doc, eItem.getAttribute("ItemOID"), item);
                            itemGroup.addItem(item);
                            lItem.add(item);
                        }
                    }
                }
            }
        }
        return lItem;
    }
    
    public void findItemName(Document doc, String itemOID, Item item){
        NodeList nlItem = doc.getElementsByTagName("ItemDef");
        for (int i = 0; i < nlItem.getLength(); i++) {
            Node nItem = nlItem.item(i);
            if (nItem.getNodeType() == Node.ELEMENT_NODE) {
                Element eItem= (Element) nItem;
                if (eItem.getAttribute("OID").equals(itemOID)) {
                    item.setItemName(eItem.getAttribute("Name"));
                }
            }
        }  
    }
}
