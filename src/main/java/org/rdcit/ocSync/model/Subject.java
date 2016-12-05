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
@ManagedBean( name = "Subject")
public class Subject {

    String subjectID;
    String subjectUID;
    String subjectOID;
    String subjectGendre;
    String subjectDateOfBirth;
    List<StudyEvent> lSubjectstudyEvent;
    
    public Subject() {
    }
   
    public Subject(String subjectid, String subjectUD, String subjectGendre, String subjectDateOfBirth) {
        this.subjectID = subjectid;
        this.subjectUID = subjectUD;
        this.subjectGendre = subjectGendre;
        this.subjectDateOfBirth = subjectDateOfBirth;
        lSubjectstudyEvent = new ArrayList();
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectUID() {
        return subjectUID;
    }

    public void setSubjectUID(String subjectUID) {
        this.subjectUID = subjectUID;
    }

    public String getSubjectGendre() {
        return subjectGendre;
    }

    public void setSubjectGendre(String subjectGendre) {
        this.subjectGendre = subjectGendre;
    }

    public String getSubjectDateOfBirth() {
        return subjectDateOfBirth;
    }

    public void setSubjectDateOfBirth(String subjectDateOfBirth) {
        this.subjectDateOfBirth = subjectDateOfBirth;
    }


    public void addSubjectStudyEvent(StudyEvent studyEvent) {
        this.lSubjectstudyEvent.add(studyEvent);
    }

    public List<StudyEvent> getlSubjectstudyEvent() {
        return lSubjectstudyEvent;
    }

    public void setlSubjectstudyEvent(List<StudyEvent> lSubjectstudyEvent) {
        this.lSubjectstudyEvent = lSubjectstudyEvent;
    }

/*    @Override
    public String toString() {
        return "Subject{" + "subjectID=" + subjectID + ", subjectUID=" + subjectUID + ", subjectGendre=" + subjectGendre + ", subjectDateOfBirth=" + subjectDateOfBirth + ", lSubjectstudyEvent=" + lSubjectstudyEvent.size() + '}';
    }
*/

    public String getSubjectOID() {
        return subjectOID;
    }

    public void setSubjectOID(String subjectOID) {
        this.subjectOID = subjectOID;
    }
}
