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

// The nodes (Study, event, froms and items) which belong the source file but they is no correspondance for them in the target file.

// the match will fails informing the user about the missing fileds in the source file.

@ManagedBean(name = "MissingStructure")
@ApplicationScoped
public class MissingStructure {
    
    String studyName;
    String eventName;
    String formName;
    String itemName;
    String missingFieldType;

    public MissingStructure(String studyName, String eventName, String formName, String itemName) {
        this.studyName = studyName;
        this.eventName = eventName;
        this.formName = formName;
        this.itemName = itemName;
        this.missingFieldType = "Item";
    }

    public MissingStructure(String studyName, String eventName, String formName) {
        this.studyName = studyName;
        this.eventName = eventName;
        this.formName = formName;
        this.missingFieldType = "StudyEventForm";
    }
    
     public MissingStructure(String studyName, String eventName) {
        this.studyName = studyName;
        this.eventName = eventName;
        this.missingFieldType = "StudyEvent";
    }

    public MissingStructure(String studyName) {
        this.studyName = studyName;
        this.missingFieldType = "Study";
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

    public String getMissingFieldType() {
        return missingFieldType;
    }

    public void setMissingFieldType(String missingFieldType) {
        this.missingFieldType = missingFieldType;
    }

}
