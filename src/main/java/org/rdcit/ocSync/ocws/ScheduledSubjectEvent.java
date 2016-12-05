/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.rdcit.ocSync.controller.Connect;

/**
 *
 * @author sa841
 */
public class ScheduledSubjectEvent {

    String eventOID;
    String subjectID;

    public ScheduledSubjectEvent(String eventOID, String subjectID) {
        this.eventOID = eventOID;
        this.subjectID = subjectID;
    }

    public String getScheduledSubjectEventNumber() {
        String scheduledEventNumber = "0";
        try {
            Connect connect = new Connect();
            PreparedStatement prepStmt = connect.openConnection().prepareStatement("SELECT max(study_event.sample_ordinal) as last_event\n"
                    + "FROM public.study_subject INNER JOIN public.study_event ON study_subject.study_subject_id = study_event.study_subject_id\n"
                    + "INNER JOIN public.study_event_definition ON  study_event.study_event_definition_id =   study_event_definition.study_event_definition_id\n"
                    + "AND study_subject.label = '" + this.subjectID + "' AND study_event_definition.oc_oid = '" + this.eventOID + "';");
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                scheduledEventNumber = rs.getString("1");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return scheduledEventNumber;
    }

}
