/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "ConfFileReader")
public class ConfFileReader {

    private final String confFile = ConfFileReader.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("ConfFileReader.class", "connect.conf");
    
    String ocInstance;
    String sOcInstanceDBconf;
    String[] ocInstanceDBconf;

    String host;
    String port;
    String db_name;
    String db_user_name;
    String db_user_pwd;

    public ConfFileReader() {
        ocInstance = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ocInstance");
        this.setsOcInstanceDBconf();
        this.setOcInstanceDBconf();
        host = this.ocInstanceDBconf[1];
        port = this.ocInstanceDBconf[2];
        db_name = this.ocInstanceDBconf[3];
        db_user_name = this.ocInstanceDBconf[4];
        db_user_pwd = this.ocInstanceDBconf[5];
    }

    public void setOcInstanceDBconf() {
        ocInstanceDBconf = this.sOcInstanceDBconf.split("\t");
    }

    public void setsOcInstanceDBconf() {
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(this.confFile));
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith(this.ocInstance)) {
                    sOcInstanceDBconf = sCurrentLine;
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDb_name() {
        return db_name;
    }

    public String getDb_user_name() {
        return db_user_name;
    }

    public String getDb_user_pwd() {
        return db_user_pwd;
    }

    public String getOcInstance() {
        return ocInstance;
    }
    
    public String[] getOcInstanceDBconf() {
        return ocInstanceDBconf;
    }

}
