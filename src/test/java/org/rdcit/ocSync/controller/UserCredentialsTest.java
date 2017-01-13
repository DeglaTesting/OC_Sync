/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author sa841
 */
public class UserCredentialsTest {

    @Mock
    Connect mockConnect;
    @Mock
    ResultSet rs;

    @Before
    public void intMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testVerifyCredentialsCaseConnectionError() {
        //given
        doThrow(new SQLException()).when(mockConnect.openConnection());
        //when
        mockConnect.openConnection();
        //then
        verify(mockConnect.openConnection());

    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(UserCredentialsTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The tests were successful " + result.wasSuccessful());
    }
}
