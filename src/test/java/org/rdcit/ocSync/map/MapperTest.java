/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.mockito.runners.MockitoJUnitRunner;
import org.rdcit.ocSync.model.*;

/**
 *
 * @author sa841
 */
@RunWith(MockitoJUnitRunner.class)
public class MapperTest {

    File sourceFile;
    File targetFile;
    CollectingMetaData collectingSourceMetaData;
    List<Study> lSourceStudy;
    List<Study> lTargetStudy;
    List<Structure> lStructure;
    List<Study> lStructure1;
    List<EmptyStructure> lEmptyStructure;
    List<MissingStructure> lMissingStructure;
    boolean areTheyMatch;

    @Before
    public void initTestDependencies() {
        sourceFile = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\src\\test\\Study1.xml");
        collectingSourceMetaData = new CollectingMetaData(sourceFile);
        lSourceStudy = collectingSourceMetaData.collectingMetaDataFromFile();
        lStructure = new ArrayList();
        lTargetStudy = new ArrayList();
        lEmptyStructure = new ArrayList();
        lMissingStructure = new ArrayList();
        lStructure1 = new ArrayList();
        areTheyMatch = true;
    }

    public void preSuccessfulMapping() {
        lTargetStudy = collectingSourceMetaData.collectingMetaDataFromFile();
    }

    public void preFailMapping_DifferentStudyUPID() {
        targetFile = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\src\\test\\Study2.xml");
        CollectingMetaData collectingTargetMetaData = new CollectingMetaData(targetFile);
        lTargetStudy = collectingTargetMetaData.collectingMetaDataFromFile();
    }

    public void preFailMapping_TargetStudy_GT_SourceStudy() {
        //Study3.xml is the Study1.xml with additional items
        targetFile = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\src\\test\\Study3.xml");
        CollectingMetaData collectingTargetMetaData = new CollectingMetaData(targetFile);
        lTargetStudy = collectingTargetMetaData.collectingMetaDataFromFile();
    }

    public void preFailMapping_TargetStudy_ST_SourceStudy() {
        //Study4.xml is the Study1.xml after removing same items
        targetFile = new File("C:\\Users\\sa841\\Documents\\NetBeansProjects\\OC\\src\\test\\Study4.xml");
        collectingSourceMetaData = new CollectingMetaData(targetFile);
        CollectingMetaData collectingTargetMetaData = new CollectingMetaData(targetFile);
        lTargetStudy = collectingTargetMetaData.collectingMetaDataFromFile();
    }

    public void mockMappingMethod() {
        if (lSourceStudy.get(0).getStudyUPID().equals(lTargetStudy.get(0).getStudyUPID())) {
            List<StudyEvent> lSourceStudyEvent = lSourceStudy.get(0).getlStudyEvent();
            List<StudyEvent> lTargetStudyEvent = lTargetStudy.get(0).getlStudyEvent();
            for (int k = 0; k < lSourceStudyEvent.size(); k++) {
                for (int l = 0; l < lTargetStudyEvent.size(); l++) {
                    if (lSourceStudyEvent.get(k).getEventName().equals(lTargetStudyEvent.get(l).getEventName())) {
                        List<StudyEventForm> lSourceStudyEventFrom = lSourceStudyEvent.get(k).getlStudyEventForm();
                        List<StudyEventForm> lTargetStudyEventFrom = lTargetStudyEvent.get(l).getlStudyEventForm();
                        for (int m = 0; m < lSourceStudyEventFrom.size(); m++) {
                            for (int n = 0; n < lTargetStudyEventFrom.size(); n++) {
                                if (lSourceStudyEventFrom.get(m).getFormName().equals(lTargetStudyEventFrom.get(n).getFormName())) {
                                    List<ItemGroup> lSourceItemGroup = lSourceStudyEventFrom.get(m).getlItemGroup();
                                    List<ItemGroup> lTargetItemGroup = lTargetStudyEventFrom.get(n).getlItemGroup();
                                    for (int o = 0; o < lSourceItemGroup.size(); o++) {
                                        for (int p = 0; p < lTargetItemGroup.size(); p++) {
                                            if (lSourceItemGroup.get(o).getItemGroupName().equals(lTargetItemGroup.get(p).getItemGroupName())) {
                                                List<Item> lSourceItem = lSourceItemGroup.get(o).getlItem();
                                                List<Item> lTargetItem = lTargetItemGroup.get(p).getlItem();
                                                int nItemChecked = 0;
                                                for (int q = 0; q < lSourceItem.size(); q++) {
                                                    for (int r = 0; r < lTargetItem.size(); r++) {
                                                        if (lSourceItem.get(q).getItemName().equals(lTargetItem.get(r).getItemName())) {
                                                            Structure structure = new Structure(lSourceStudy.get(0).getStudyName(), lSourceStudyEvent.get(k).getEventName(), lSourceStudyEventFrom.get(m).getFormName(), lSourceItem.get(q).getItemName());
                                                            lStructure.add(structure);
                                                            nItemChecked++;
                                                            break;
                                                        } else if (r == lTargetItem.size() - 1) {
                                                            MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(0).getStudyName(), lSourceStudyEvent.get(k).getEventName(), lSourceStudyEventFrom.get(m).getFormName(), lSourceItem.get(q).getItemName());
                                                            lMissingStructure.add(missingStructure);
                                                        }
                                                    }
                                                    if (nItemChecked < lSourceItem.size()) {
                                                        MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(0).getStudyName(), lSourceStudyEvent.get(k).getEventName(), lSourceStudyEventFrom.get(m).getFormName(), lSourceItem.get(q).getItemName());
                                                        lMissingStructure.add(missingStructure);
                                                    }
                                                }
                                                if (nItemChecked < lTargetItem.size()) {
                                                    for (int q = 0; q < lTargetItem.size(); q++) {
                                                        Structure structure = new Structure(lTargetStudy.get(0).getStudyName(), lTargetStudyEvent.get(k).getEventName(), lTargetStudyEventFrom.get(m).getFormName(), lTargetItem.get(q).getItemName());
                                                        if (!contains(lStructure, structure)) {
                                                            EmptyStructure emptyStructure = new EmptyStructure(lTargetStudy.get(0).getStudyName(), lTargetStudyEvent.get(k).getEventName(), lTargetStudyEventFrom.get(m).getFormName(), lTargetItem.get(q).getItemName());
                                                            lEmptyStructure.add(emptyStructure);
                                                        }
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }
                                } else if (n == lTargetStudyEventFrom.size() - 1) {
                                    MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(0).getStudyName(), lSourceStudyEvent.get(k).getEventName(), lSourceStudyEventFrom.get(m).getFormName());
                                    lMissingStructure.add(missingStructure);
                                }
                            }
                        }
                        break;
                    } else if (l == lTargetStudyEvent.size() - 1) {
                        MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(0).getStudyName(), lSourceStudyEvent.get(k).getEventName());
                        lMissingStructure.add(missingStructure);
                    }
                }
            }
            lStructure1.add(lSourceStudy.get(0));
        } else if (0 == lTargetStudy.size() - 1) {
            MissingStructure missingStructure = new MissingStructure(lSourceStudy.get(0).getStudyName());
            lMissingStructure.add(missingStructure);
        }

        if (!lMissingStructure.isEmpty()) {
            areTheyMatch = false;
        }
    }

    @Test
    public void mappingTest_idealCase() {
        // Given
        preSuccessfulMapping();
        // when
        mockMappingMethod();
        // then
        assertTrue(areTheyMatch);
        assertTrue(lMissingStructure.isEmpty());
        assertTrue(lEmptyStructure.isEmpty());
    }

    @Test
    public void mappingTest_DifferentStudyPUID() {
        // Given
        preFailMapping_DifferentStudyUPID();
        // when
        mockMappingMethod();
        // then 
        assertFalse(areTheyMatch);
        assertTrue(lMissingStructure.size() > 0);
    }

    @Test
    public void mappingTest_TargetStudy_GT_SourceStudy() {
        // Given
        preFailMapping_TargetStudy_GT_SourceStudy();
        // when
        mockMappingMethod();
        // then 
        assertTrue(areTheyMatch);
        assertTrue(lMissingStructure.isEmpty());
        assertFalse(lEmptyStructure.isEmpty());
    }

    @Test
    public void mappingTest_TargetStudy_ST_SourceStudy() {
        // Given
        preFailMapping_TargetStudy_ST_SourceStudy();
        // when
        mockMappingMethod();
        // then 
        assertFalse(areTheyMatch);
        assertTrue(!lMissingStructure.isEmpty());
    }

    public boolean contains(List<Structure> lStructure, Structure structure) {
        boolean contains = false;
        for (int i = 0; i < lStructure.size(); i++) {
            if (lStructure.get(i).equals(structure)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(MapperTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
        }
        System.out.println("The test was successful: " + result.wasSuccessful());
    }

}
