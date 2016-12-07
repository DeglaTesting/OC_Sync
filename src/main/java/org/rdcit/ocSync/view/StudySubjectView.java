/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.rdcit.ocSync.model.OIDMapper;
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
            setAptToUploadStudyParam(lStudy.get(i));
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

    public void setAptToUploadStudyParam(Study sourceStudy) {
        List<OIDMapper> lOIDMapper =  (List<OIDMapper>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("OIDMapperList");
        for (int i = 0; i < lOIDMapper.size(); i++) {
            if (sourceStudy.getStudyOID().equals(lOIDMapper.get(i).getSourceStudy().getStudyOID())) {
                if ((lOIDMapper.get(i).getSourceStudy().studyParamsEquals(lOIDMapper.get(i).getTargetStudy().getStudyParams()))) {
                    sourceStudy.setAptToUpload("OK");
                } else {
                    sourceStudy.setAptToUpload("Not ok");
                    FacesContext.getCurrentInstance().addMessage("studyParams", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Study parametres configurations", "The two studies do not have the same parametres configuration."));
                }
            }
            break;
        }

    }

    public String getSelectedStudyName() {
        Study selectedStudy = (Study) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedStudy");
        return selectedStudy.getStudyName();
    }
}
