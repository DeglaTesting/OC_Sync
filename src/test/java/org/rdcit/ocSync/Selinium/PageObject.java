/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.Selinium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 *
 * @author sa841
 */
public class PageObject {

   protected WebDriver webDriver;

    public PageObject(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }
    
    public void selectOneMenu(String idPrefix, String value){
       webDriver.findElement(By.id(idPrefix + "_label")).click();
       webDriver.findElement(By.xpath("//div[@id='" + idPrefix + "_panel']/div/ul/li[text()='" + value + "']")).click();
    }

}
