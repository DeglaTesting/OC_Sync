/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author sa841
 */
public class ConnectTest {

    @Mock
    Connect mockConnect;
    @Mock
    Connection mockConnection;

    @Ignore
    @Before
    public void intMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testCaseDriverManagerDoesNotExist() {
        //given
        doReturn(null).when(mockConnect).openConnection();
        //when       //then
        assertNull(mockConnect.openConnection());
    }

    @Ignore
    @Test
    public void testCaseDriverManagerExists() {
        //given
        doReturn(mockConnection).when(mockConnect).openConnection();
        //when
        mockConnect.openConnection();
        //then
        assertNotNull(mockConnection);
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ConnectTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The tests were successful " + result.wasSuccessful());
    }

}
