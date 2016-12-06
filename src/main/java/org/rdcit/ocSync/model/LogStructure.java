/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.model;

import javax.faces.bean.ManagedBean;

/**
 *
 * @author sa841
 */
@ManagedBean( name ="LogStructure")
public class LogStructure {
    
    String typeMessage;
    String message;

    public LogStructure(String typeMessage, String message) {
        this.typeMessage = typeMessage;
        this.message = message;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
