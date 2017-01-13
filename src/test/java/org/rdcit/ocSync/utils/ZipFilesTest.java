/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author sa841
 */
@RunWith(MockitoJUnitRunner.class)
public class ZipFilesTest {

    @Test
    public void addToZipFileTest() throws IOException {
        //given
        String fileName = "zipTest.txt";
        ZipFiles zip = new ZipFiles();
        ZipFiles spyZip = spy(zip);
        //When
        spyZip.addToZipFile(fileName);
        //then
        verify(spyZip).fos.close();
        
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ZipFilesTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successful: " +result.wasSuccessful());
    }

}
