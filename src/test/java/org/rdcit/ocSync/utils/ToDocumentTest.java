/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;

/**
 *
 * @author sa841
 */
@RunWith(MockitoJUnitRunner.class)
public class ToDocumentTest {

    @Mock
    ToDocument mockToDocument;

    @Test
    public void stringToDocumentTest() {
        //given
        doCallRealMethod().when(mockToDocument).stringToDocument(anyString());
        //when
        Document mockDoc = mockToDocument.stringToDocument("<Sweets><chocolate/></Sweets>");
        //then 
        assertNotNull(mockDoc);
    }

    @Test
    public void stringToDocumentTest_Fail_MalFormattedStringArg() throws Throwable {
        //given
        doReturn(null).when(mockToDocument).stringToDocument(anyString());
        //when
        Document mockDoc = mockToDocument.stringToDocument("<Sweets><chocolate></Sweets>");
        //then 
        assertNull(mockDoc);
    }


    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ToDocumentTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successful :" + result.wasSuccessful());
    }
}
