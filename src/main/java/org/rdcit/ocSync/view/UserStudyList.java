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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.rdcit.ocSync.model.Study;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "UserStudyList")
@ViewScoped
public class UserStudyList implements Serializable {

    private List<Study> lStudy;
    public Study selectedStudy;
    boolean disableFileUploadButton = true;

    @PostConstruct
    public void init() {
        lStudy = (List<Study>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("studyUserList");
        if ((Study) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedStudy") != null) {
            setSelectedStudy((Study) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SelectedStudy"));
        }

    }

    public UserStudyList() {
    }

    public List<Study> getlStudy() {
        return lStudy;

    }

    public void setlStudy(List<Study> lStudy) {
        this.lStudy = lStudy;
    }

    public Study getSelectedStudy() {
        return selectedStudy;
    }

    public void setSelectedStudy(Study selectedStudy) {
        this.selectedStudy = selectedStudy;
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Target study", selectedStudy.getStudyName());
        FacesContext.getCurrentInstance().addMessage("SelectStudyMSG", msg);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("SelectedStudy", selectedStudy);
        setDisableFileUploadButton(false);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Target study", "You have to choose a target study first");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        // setDisableFileUploadButton(true);
    }

    public boolean isDisableFileUploadButton() {
        return disableFileUploadButton;
    }

    public void setDisableFileUploadButton(boolean disableFileUploadButton) {
        this.disableFileUploadButton = disableFileUploadButton;
    }
}
