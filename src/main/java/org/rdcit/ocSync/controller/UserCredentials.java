/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.rdcit.ocSync.model.User;
import org.rdcit.ocSync.ocws.ListStudies_ws;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "UserCredentials")
@SessionScoped
public class UserCredentials {

    String userName;
    String password;
    boolean verifiy;
    User user;

    public UserCredentials(String userName, String password) {
        this.userName = userName;
        this.password = DigestUtils.sha1Hex(password);
        this.verifiy = false;
    }

    public boolean verifyCredentials() {
        try {
            Connect connect = new Connect();
            PreparedStatement prepStmt = connect.openConnection().prepareStatement("SELECT run_webservices FROM user_account WHERE user_name = '" + this.userName + "' AND passwd = '" + this.password + "';");
            ResultSet rs = prepStmt.executeQuery();
            if (!rs.next()) {
                this.verifiy = false;
            } else {
                this.verifiy = true;
                if (rs.getString("run_webservices").equals("t")) {
                    user = new User(this.userName, this.password);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Web Service Error", "Sorry, you are not authorized to use the web services.");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return this.verifiy;
    }

    public void redirectLoginPage() {
        ListStudies_ws listStudies_ws = new ListStudies_ws(this.user);
        listStudies_ws.getUserStudyList();
    }

}
