/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author sa841
 */
@ManagedBean( name = "Structure")
@ApplicationScoped
public class Structure {
    String studyName;
    String eventName;
    String formName;
    String itemName;

    public Structure(String studyName, String eventName, String formName, String itemName) {
        this.studyName = studyName;
        this.eventName = eventName;
        this.formName = formName;
        this.itemName = itemName;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean equals(Structure structure) {
        boolean res = false;
        if ((this.getStudyName().equals(structure.getStudyName()))
                && (this.getEventName().equals(structure.getEventName()))
                && (this.getFormName().equals(structure.getFormName()))
                && (this.getItemName().equals(structure.getItemName()))) {
            res = true;
        }

        return res;
    }

}
