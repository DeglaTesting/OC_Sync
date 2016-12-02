/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.view;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.rdcit.ocSync.controller.UserCredentials;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "UserLogin")
@SessionScoped
public class UserLogin implements Serializable {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login(ActionEvent event) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String smessage = "";
        try {
            if (!facesContext.getExternalContext().getSessionMap().get("ocInstance").equals("OC_Play")) {
                smessage = "Sorry, this instance is not defined yet.";
            } else {
                UserCredentials userCredentials = new UserCredentials(username, password);
                boolean loggedIn = userCredentials.verifyCredentials();
                if (loggedIn == true) {
                    userCredentials.redirectLoginPage();
                    facesContext.getExternalContext().redirect("studyMapping.xhtml");
                } else {
                    smessage = "Invalid credentials";
                }
            }
        } catch (Exception ex) {
            smessage = "Please fill the form first";
            System.out.println(ex.getMessage());
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", smessage);
        facesContext.addMessage(null, message);
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login.xhtml";
    }
}
