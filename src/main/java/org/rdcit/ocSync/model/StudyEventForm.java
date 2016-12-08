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
@ManagedBean( name = "StudyEventForm")
public class StudyEventForm {

    String formOID;
    String formName;
    List<ItemGroup> lItemGroup;

    public StudyEventForm() {
    }

    public StudyEventForm(String formOID) {
        this.formOID = formOID;
        lItemGroup = new ArrayList();
    }

    public String getFormOID() {
        return formOID;
    }

    public void setFormOID(String formOID) {
        this.formOID = formOID;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public List<ItemGroup> getlItemGroup() {
        return lItemGroup;
    }

    public void setlItemGroup(List<ItemGroup> lItemGroup) {
        this.lItemGroup = lItemGroup;
    }
    
    public void addItemGroup(ItemGroup itemGroup){
        lItemGroup.add(itemGroup);
    }
}
