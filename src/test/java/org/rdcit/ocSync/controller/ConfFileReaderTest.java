/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.controller;

import java.io.IOException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author sa841
 */
public class ConfFileReaderTest {

    @Mock
    ConfFileReader mockConfFileReader;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCaseOCInstanceExistsInConfFile() {
        //given
        when(mockConfFileReader.getOcInstance()).thenReturn("corresponding line to the chosen OC Instance");
        //when   //then
        assertNotNull(mockConfFileReader.getOcInstance());
    }

    @Test
    public void testCaseOCInstanceDoesNotExistsInConfFile() {
        //given
        when(mockConfFileReader.getOcInstance()).thenReturn(null);
        // when //then
        assertNull(mockConfFileReader.getOcInstance());
    }

    @Test
    public void testCaseOCInstanceExistsButNotWellFormatted() {
        //given 
        doThrow(IOException.class).when(mockConfFileReader).getOcInstanceDBconf();
        //when //then
        mockConfFileReader.getOcInstance();
    }

    @Test
    public void testCaseOCInstanceExistxAndWellFormatted() {
        //given
        doReturn(new String[5]).when(mockConfFileReader).getOcInstanceDBconf();
        //when //then
        assertTrue(mockConfFileReader.getOcInstanceDBconf().length == 5);
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ConfFileReaderTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The tests were successfull : " + result.wasSuccessful());
    }

}
