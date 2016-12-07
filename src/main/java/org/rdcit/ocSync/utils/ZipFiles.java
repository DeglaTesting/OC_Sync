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

    public void createAZipFile() {
        try {
            FileOutputStream fos = new FileOutputStream("OCSync.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            String file1Name = "C:\\Users\\sa841\\res.xml";
            String file2Name = "C:\\Users\\sa841\\res2.xml";
            addToZipFile(file1Name, zos);
            addToZipFile(file2Name, zos);
            zos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {
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
        zos.closeEntry();
        fis.close();
    }
}
