/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.rdcit.ocSync.model.OIDMapper;
import org.rdcit.ocSync.model.Study;
import org.rdcit.ocSync.ocOdm.CollectingClinicalData;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "SourceStudySubjectView")
@ViewScoped
public class SourceStudySubjectView implements Serializable {

    List<Study> lStudy;
    List<OIDMapper> lOIDMapper;
    boolean disableConfirmButton;

    public SourceStudySubjectView() {
    }

    @PostConstruct
    public void init() {
        CollectingClinicalData collectingClinicalData = new CollectingClinicalData();
        lStudy = collectingClinicalData.collectingClinicalData();
        lOIDMapper = (List<OIDMapper>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("OIDMapperList");
        boolean disabled = false;
        for (int i = 0; i < lOIDMapper.size(); i++) {
            Study sourceStudy = lOIDMapper.get(i).getSourceStudy();
            Study targetStudy = lOIDMapper.get(i).getTargetStudy();
            if (!sourceStudy.studyParamsEquals(targetStudy.getStudyParams())) {
                findStudy(lStudy, sourceStudy.getStudyOID()).setAptToUpload("error");
            }
        }
        /*   for (int i = 0; i < lStudy.size(); i++) {
            if (lStudy.get(i).getlSubject().isEmpty()) {
                disabled = true;
                Subject subject = new Subject();
                subject.setSubjectID("This study does not contains any clinical data subject.");
                lStudy.get(i).addSubject(subject);
            }
        }*/
        setDisableConfirmButton(disabled);
    }

    public List<Study> getlStudy() {
        return lStudy;
    }

    public void setlStudy(List<Study> lStudy) {
        this.lStudy = lStudy;
    }

    public boolean isDisableConfirmButton() {
        return disableConfirmButton;
    }

    public void setDisableConfirmButton(boolean disableConfirmButton) {
        this.disableConfirmButton = disableConfirmButton;
    }

    public String getSelectedStudyName() {
        Study selectedStudy = (Study) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedStudy");
        return selectedStudy.getStudyName();
    }

    public Study findStudy(List<Study> lStudy, String studyOID) {
        for (int i = 0; i < lStudy.size(); i++) {
            if (lStudy.get(i).getStudyOID().equals(studyOID)) {
                return lStudy.get(i);
            }
        }
        return null;
    }

}
