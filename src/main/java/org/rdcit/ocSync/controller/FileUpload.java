/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.rdcit.ocSync.model.UploadedFile;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "FileUpload")
@ViewScoped
public class FileUpload implements Serializable{

    public String sourcefilePath;
    private final String destination = "C:\\Users\\sa841\\Documents\\";
    String uploadedFileName; 
    boolean disableMapButton = true;
    boolean disableSelectStudyButton = false;

    public void uploadSourceFile(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            sourcefilePath = destination + event.getFile().getFileName();
            UploadedFile.sourceUploadedFile = new File(sourcefilePath);
            FacesMessage message = new FacesMessage(event.getFile().getFileName() , " following the next steps, we will try to upload its data to OpenClinica in the choosen study.");
            FacesContext.getCurrentInstance().addMessage("fileuploadMSG", message);
            uploadedFileName = event.getFile().getFileName();
            setDisableMapButton(false);
            setDisableSelectStudyButton(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void copyFile(String fileName, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(destination + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

     public void fileUploadName(FileUploadEvent event) {
      uploadedFileName = FilenameUtils.getName(event.getFile().getFileName());
    }

    public boolean isDisableMapButton() {
        return disableMapButton;
    }

    public void setDisableMapButton(boolean disableMapButton) {
        this.disableMapButton = disableMapButton;
        setDisableSelectStudyButton(false);
    }

    public boolean isDisableSelectStudyButton() {
        return disableSelectStudyButton;
    }

    public void setDisableSelectStudyButton(boolean disableSelectStudyButton) {
        this.disableSelectStudyButton = disableSelectStudyButton;
    }

     
}
