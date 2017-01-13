/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.Selinium;

import java.util.ArrayList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author sa841
 */
public class StudyMappingPageObject extends PageObject {

    @FindBy(tagName = "h3")
    WebElement header;
    @FindBy(id = "j_idt23:userStudies_data")
    WebElement userStudiesFormData;
    @FindBy(id = "j_idt13:fileupload")
    WebElement fileuploadForm;
    @FindBy(id = "from2:submit")
    WebElement submitButtom;

    public StudyMappingPageObject(WebDriver webDriver) {
        super(webDriver);
    }

    public String getConfirmationHeader() {
        return this.header.getText();
    }

    //    Only userStudies Form should be enabled 
    public boolean activeComponents_FirstLoading() {
        tabHandles();
        return (fileuploadForm.isDisplayed() && !userStudiesFormData.isSelected() && submitButtom.isDisplayed());
    }

    //Checks if The main page of the AUT opens in the first tab 
    public boolean tabHandles() {
        String currentWindowHandle = webDriver.getWindowHandle();
        //Get the list of all window handles
        ArrayList<String> windowHandles = new ArrayList(webDriver.getWindowHandles());
        //if it contains the current window we want to eliminate that from switchTo();
        return ((windowHandles.size() == 1) && (windowHandles.get(0).equals(currentWindowHandle)));
    }
}
