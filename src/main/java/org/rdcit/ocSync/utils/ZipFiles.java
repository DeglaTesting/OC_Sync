/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author sa841
 */
public class ZipFiles {

    FileOutputStream fos;
    ZipOutputStream zos;

    public ZipFiles() {
        fos = null;
        try {
            fos = new FileOutputStream(System.getProperty("user.dir")+"ocSync.zip");
            zos = new ZipOutputStream(fos);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void closingZipParams() {
        try {
            zos.closeEntry();
            zos.close();
            fos.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addToZipFile(String fileName) {
        try {
            System.out.println("Writing '" + fileName + "' to zip file");
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            fis.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
