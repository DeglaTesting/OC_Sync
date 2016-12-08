/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sa841
 */
public class User implements Serializable{

    String user_name;
    String password;
    List<Study> lStudy;

    public User(String user_name,  String password) {
        this.user_name = user_name;
        this.password = password;
        lStudy = new ArrayList();
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Study> getlStudy() {
        return lStudy;
    }

    public void setlStudy(List<Study> lStudy) {
        this.lStudy = lStudy;
    }

    public void addStudy(Study study) {
        this.lStudy.add(study);
    }
}
