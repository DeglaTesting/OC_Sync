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

// The nodes (Study, event, froms and items) which belong the target file but they is no correspondance for them in the source file.

//The match will successed informing the user that will be Empty filed the target file.

@ManagedBean(name = "EmptyStructure")
@ApplicationScoped
public class EmptyStructure {
    
    
    String studyName;
    String eventName;
    String formName;
    String itemName;
    String emptyFieldType;

    public EmptyStructure(String studyName, String eventName, String formName, String itemName) {
        this.studyName = studyName;
        this.eventName = eventName;
        this.formName = formName;
        this.itemName = itemName;
        this.emptyFieldType = "Item";
    }

    
    
    
    public EmptyStructure(String studyName, String eventName, String formName) {
        this.studyName = studyName;
        this.eventName = eventName;
        this.formName = formName;
        this.emptyFieldType = "StudyEventForm";
    }

    public EmptyStructure(String studyName, String eventName) {
        this.studyName = studyName;
        this.eventName = eventName;
        this.emptyFieldType = "StudyEvent";
    }

    public EmptyStructure(String studyName) {
        this.studyName = studyName;
        this.emptyFieldType = "Study";
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

    public String getEmptyFieldType() {
        return emptyFieldType;
    }

    public void setEmptyFieldType(String emptyFieldType) {
        this.emptyFieldType = emptyFieldType;
    }
   
    
}
