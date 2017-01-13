/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import java.io.File;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.mockito.Mock;
import static org.mockito.Mockito.doCallRealMethod;
import org.mockito.runners.MockitoJUnitRunner;
import org.rdcit.ocSync.model.Study;
import org.w3c.dom.Document;

/**
 *
 * @author sa841
 */
@RunWith(MockitoJUnitRunner.class)
public class CollectingMetaDataTest {

    private File file = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\src\\test\\Study1.xml");
    private CollectingMetaData collectingMetaData = new CollectingMetaData(file);
    @Mock
    private Document mockDoc;
    @Mock
    private CollectingStudies mockCollectingStudies;

    @Ignore
    @Test
    public void openingFileTest() {
        //given  //when
        mockDoc = collectingMetaData.openFile();
        //then
        assertNotNull(mockDoc);
    }

    @Ignore
    @Test
    public void collectingMetaDataTest() {
        //given
        mockDoc = collectingMetaData.openFile();
        doCallRealMethod().when(mockCollectingStudies).collectingStudies(mockDoc);
        //when
        List<Study> lStudy = mockCollectingStudies.collectingStudies(mockDoc);
        //then
        assertNotNull(lStudy);
    }

    // No need to test it, all its method calls are already tested!
    @Ignore
    @Test
    public void collectingSiteStudiesMetaData() {

    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CollectingMetaDataTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successful " + result.wasSuccessful());
    }
}
