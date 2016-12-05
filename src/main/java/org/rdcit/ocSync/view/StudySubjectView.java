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
import org.rdcit.ocSync.model.Study;
import org.rdcit.ocSync.model.Subject;
import org.rdcit.ocSync.ocOdm.CollectingClinicalData;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "StudyView")
@ViewScoped
public class StudySubjectView implements Serializable {

    List<Study> lStudy;
    boolean disableConfirmButton;

    public StudySubjectView() {
    }

    @PostConstruct
    public void init() {
        CollectingClinicalData collectingClinicalData = new CollectingClinicalData();
        lStudy = collectingClinicalData.collectingClinicalData();
        boolean disabled = false;
        for (int i = 0; i < lStudy.size(); i++) {
            if (lStudy.get(i).getlSubject().isEmpty()) {
                disabled = true;
                Subject subject = new Subject();
                subject.setSubjectID("This study does not contains any clinical data subject.");
                lStudy.get(i).addSubject(subject);
            }
        }
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
}
