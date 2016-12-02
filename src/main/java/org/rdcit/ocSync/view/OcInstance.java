/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.view;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "OcInstance")
@SessionScoped
public class OcInstance implements Serializable {

    private static final long serialVersionUID = 1L;
    String instance;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void valueChangeMethod(ValueChangeEvent e) {
        if (e.getNewValue() == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Please choose an OC instance first!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            this.instance = e.getNewValue().toString();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ocInstance", this.instance);
        }

    }

}
