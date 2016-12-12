/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author sa841
 */
@ManagedBean(name = "FileDownloadView")
public class FileDownloadView {

    private StreamedContent file;

    public FileDownloadView() {

    }

    public StreamedContent getFile() {
        return file;
    }

    public StreamedContent prepDownload() {
        StreamedContent download = null;
        try {
            download = new DefaultStreamedContent();
            File file = new File(System.getProperty("user.dir")+"ocSync.zip");
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            download = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
            System.out.println("PREP = " + download.getName());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return download;
    }
}
