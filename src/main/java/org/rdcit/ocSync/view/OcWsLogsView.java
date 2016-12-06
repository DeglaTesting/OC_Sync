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
import org.rdcit.ocSync.model.LogStructure;

/**
 *
 * @author sa841
 */

@ManagedBean(name = "OcWsLogsView")
@ViewScoped
public class OcWsLogsView implements Serializable{
    
     List<LogStructure> lOcWsResponse;
    
    @PostConstruct
    public void init(){
        lOcWsResponse = (List<LogStructure>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("lOcWsResponse");
    }

    public List<LogStructure> getlOcWsResponse() {
        return lOcWsResponse;
    }

    public void setlOcWsResponse(List<LogStructure> lOcWsResponse) {
        this.lOcWsResponse = lOcWsResponse;
    }
  
}
