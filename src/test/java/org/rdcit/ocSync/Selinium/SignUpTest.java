/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.Selinium;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;

/**
 *
 * @author sa841
 */
public class SignUpTest extends FonctionalTest {

    @Ignore
    @Test
    public void signUp() {
        // Get the Login page of the AUT
        webDriver.get("http://localhost:8080/OC");
        LoginPageObject login = new LoginPageObject(webDriver);
        // Verify if the page Login is opened
        assertTrue(login.isThePageOpened());
        // Fill out the Login page
        login.selectOCInstance("OC_Play");
        login.enterCredentials("sa841", "RealPWD");
        // Submit 
        login.submit();
        // Wait until the main App page is loaded
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("j_idt7:loggedIn")));
        // Verify if the main App page is loaded
        StudyMappingPageObject studyMapping = new StudyMappingPageObject(webDriver);
        assertTrue(studyMapping.getConfirmationHeader().equals("Please upload your source study:"));
        // Verify if the first Tab is loaded and if all the others are disabled at the first load of the page
        assertTrue(studyMapping.tabHandles());
        // Verify what the components what should be enable/disable at the first load of the page
        assertTrue(studyMapping.activeComponents_FirstLoading());
    }

    @Ignore
    @Test
    public void signUpExpectingFail_InvalidCredentials() {
        webDriver.get("http://localhost:8080/OC");
        LoginPageObject login = new LoginPageObject(webDriver);
        // Verify if the page Login is opened
        assertTrue(login.isThePageOpened());
        // Fill out the Login page
        login.selectOCInstance("OC_Play");
        login.enterCredentials("anonymous", "somethingStupid");
        // Submit
        login.submit();
        // Verify the AUT is still in the logn Page
        assertTrue(login.isThePageOpened());

    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(SignUpTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successful " + result.wasSuccessful());
    }
}
