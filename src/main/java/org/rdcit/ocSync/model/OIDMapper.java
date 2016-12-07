/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.model;

/**
 *
 * @author sa841
 */
public class OIDMapper {

  
    Study sourceStudy;
    Study targetStudy;

    public OIDMapper(Study sourceStudy, Study targetStudy) {
        this.sourceStudy = sourceStudy;
        this.targetStudy = targetStudy;
    }

    public Study getSourceStudy() {
        return sourceStudy;
    }

    public void setSourceStudy(Study sourceStudy) {
        this.sourceStudy = sourceStudy;
    }

    public Study getTargetStudy() {
        return targetStudy;
    }

    public void setTargetStudy(Study targetStudy) {
        this.targetStudy = targetStudy;
    }
}
