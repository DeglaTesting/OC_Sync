/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "Study")
public class Study implements Serializable {

    String studyName;
    String studyOID;
    String studyUPID;
    String[] studyParams; //studyParams[0]=value(SPL_collectDob); studyParams[1]= value(SubjectPersonIdRequired); studyParams[2]=value(SPL_genderRequired)
    List<StudyEvent> lStudyEvent;
    List<Subject> lSubject;

    public Study() {
    }

    public Study(String study_oid) {
        this.studyOID = study_oid;
        lStudyEvent = new ArrayList();
        lSubject = new ArrayList();
    }

    public Study(String study_u_p_id, String study_name, String study_oid) {
        this.studyUPID = study_u_p_id;
        this.studyName = study_name;
        this.studyOID = study_oid;
        lStudyEvent = new ArrayList();
        lSubject = new ArrayList();
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getStudyOID() {
        return studyOID;
    }

    public void setStudyOID(String studyOID) {
        this.studyOID = studyOID;
    }

    public String getStudyUPID() {
        return studyUPID;
    }

    public void setStudyUPID(String studyUPID) {
        this.studyUPID = studyUPID;
    }

    public List<StudyEvent> getlStudyEvent() {
        return lStudyEvent;
    }

    public void setlStudyEvent(List<StudyEvent> lStudyEvent) {
        this.lStudyEvent = lStudyEvent;
    }

    public void addStudyEvent(StudyEvent StudyEvent) {
        this.lStudyEvent.add(StudyEvent);
    }

    public List<Subject> getlSubject() {
        return lSubject;
    }

    public void setlSubject(List<Subject> lSubject) {
        this.lSubject = lSubject;
    }

    public void addSubject(Subject subject) {
        this.lSubject.add(subject);
    }

    public String[] getStudyParams() {
        return studyParams;
    }

    public void setStudyParams(String[] studyParams) {
        this.studyParams = studyParams;
    }

    @Override
    public String toString() {
        return "Study {" + "study_name=" + studyName + ", study_oid=" + studyOID + ", study_id=" + studyUPID + '}';
    }
}
