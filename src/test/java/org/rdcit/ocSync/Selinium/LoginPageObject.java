/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.Selinium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author sa841
 */
public class LoginPageObject extends PageObject {

    @FindBy(id = "j_idt9:username")
    private WebElement username;
    @FindBy(id = "j_idt9:password")
    private WebElement password;
    @FindBy(id = "j_idt9:login")
    private WebElement loginButtom;

    public LoginPageObject(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean isThePageOpened() {
        return !loginButtom.getTagName().isEmpty();
    }

    public void enterCredentials(String username, String password) {
        this.username.clear();
        this.username.sendKeys(username);
        this.password.clear();
        this.password.sendKeys(password);
    }

    public void selectOCInstance(String ocInstance) {
        selectOneMenu("j_idt9:ocInstance", ocInstance);
    }

    public void submit() {
        this.loginButtom.click();
    }

}
