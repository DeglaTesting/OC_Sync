/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.model;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author sa841
 */
@ManagedBean( name = "ItemGroup")
public class ItemGroup {

    String itemGroupOID;
    String itemGroupName;
    String itemGroupRepeatingKey;
    List<Item> lItem;

    public ItemGroup() {
    }

    public ItemGroup(String itemGroupOID) {
        this.itemGroupOID = itemGroupOID;
        lItem = new ArrayList();
    }

    public ItemGroup(String itemGroupOID, String itemGroupRepeatingKey) {
        this.itemGroupOID = itemGroupOID;
        this.itemGroupRepeatingKey = itemGroupRepeatingKey;
        lItem = new ArrayList();
    }

    public String getItemGroupOID() {
        return itemGroupOID;
    }

    public void setItemGroupOID(String itemGroupOID) {
        this.itemGroupOID = itemGroupOID;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public List<Item> getlItem() {
        return lItem;
    }

    public void setlItem(List<Item> lItem) {
        this.lItem = lItem;
    }

    public void addItem(Item item) {
        this.lItem.add(item);
    }

    public String getItemGroupRepeatingKey() {
        return itemGroupRepeatingKey;
    }

    public void setItemGroupRepeatingKey(String itemGroupRepeatingKey) {
        if ((itemGroupRepeatingKey.length() < 1) ||(itemGroupRepeatingKey == null)) {
            this.itemGroupRepeatingKey = "0";
        } else {
            this.itemGroupRepeatingKey = itemGroupRepeatingKey;
        }
    }

    @Override
    public String toString() {
        return "ItemGroup{" + "itemGroupOID=" + itemGroupOID + ", itemGroupName=" + itemGroupName + '}';
    }

}
