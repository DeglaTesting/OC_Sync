/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocws;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.rdcit.ocSync.model.LogStructure;
import org.rdcit.ocSync.model.User;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sa841
 */
public class ScheduleSubjectEvent_ws {

    String subjectID;
    String studyPUID;
    String sitePUID;
    String eventOID;
    String startDate;
    String location;
    LogStructure logStructure;

    public ScheduleSubjectEvent_ws(String subjectID, String studyPUID, String eventOID) {
        this.subjectID = subjectID;
        this.studyPUID = studyPUID;
        this.eventOID = eventOID;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        this.startDate = dateFormat.format(date);
        this.location = " ";
        this.sitePUID = "";
    }

    public ScheduleSubjectEvent_ws(String subjectID, String studyPUID, String sitePUID, String eventOID) {
        this.subjectID = subjectID;
        this.studyPUID = studyPUID;
        this.sitePUID = sitePUID;
        this.eventOID = eventOID;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        this.startDate = dateFormat.format(date);
        this.location = " ";
    }

    public SOAPMessage createSOAPRequest() {
        SOAPMessage soapResponse = null;
        try {
            User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            String serverURI = "https://openclinica-testing.medschl.cam.ac.uk/OCplay-ws/ws/event/v1/eventWsdl.wsdl";

            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addAttribute(new QName("xmlns:v1"), "http://openclinica.org/ws/study/v1");
            envelope.addAttribute(new QName("xmlns:v11"), "http://openclinica.org/ws/event/v1");
            envelope.addAttribute(new QName("xmlns:bean"), "http://openclinica.org/ws/beans");

            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
            security.addAttribute(new QName("SOAP-ENV:mustUnderstand"), "1");
            SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
            usernameToken.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
            usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-27777511");
            SOAPElement username = usernameToken.addChildElement("Username", "wsse");
            username.addTextNode(user.getUser_name());
            SOAPElement password = usernameToken.addChildElement("Password", "wsse");
            password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
            password.addTextNode(user.getPassword());
            SOAPElement scheduleRequest = body.addChildElement("scheduleRequest", "v11");
            SOAPElement event = scheduleRequest.addChildElement("event", "v11");
            SOAPElement studySubjectRef = event.addChildElement("studySubjectRef", "bean");
            SOAPElement label = studySubjectRef.addChildElement("label", "bean");
            label.addTextNode(this.subjectID);
            SOAPElement studyRef = event.addChildElement("studyRef", "v11");
            SOAPElement identifier = studyRef.addChildElement("identifier", "bean");
            identifier.addTextNode(this.studyPUID);
            if (!this.sitePUID.isEmpty()) {
                SOAPElement siteRef = studyRef.addChildElement("siteRef", "v11");
                SOAPElement siteRefIdentifier = siteRef.addChildElement("identifier", "bean");
                siteRefIdentifier.addTextNode(this.sitePUID);
            }
            SOAPElement eventDefinitionOID = event.addChildElement("eventDefinitionOID", "bean");
            eventDefinitionOID.addTextNode(this.eventOID);
            SOAPElement selocation = event.addChildElement("location", "bean");
            selocation.addTextNode(this.location);
            SOAPElement sestartDate = event.addChildElement("startDate", "bean");
            sestartDate.addTextNode(this.startDate);
            System.out.println("Elementbody added");
            System.out.println(body.getChildElements());
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "create");
            soapMessage.saveChanges();

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            soapResponse = soapConnection.call(soapMessage, serverURI);
            setLogStructure(soapResponse);
            System.out.print("Request SOAP Message = ");
            soapResponse.writeTo(System.out);
        } catch (SOAPException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return soapResponse;
    }

    /*   public boolean isScheduled(SOAPMessage soapResponse) {
        boolean scheduled = false;
        try {
            NodeList nlODM = soapResponse.getSOAPBody().getElementsByTagName("result");
            Node nResult = nlODM.item(0);
            String sResult = nResult.getTextContent();
            if (sResult.equals("Success")) {
                scheduled = true;
            } else {
                Node nError = soapResponse.getSOAPBody().getElementsByTagName("error").item(0);
                this.message = nError.getTextContent();
            }
        } catch (SOAPException ex) {
            System.out.println(ex.getMessage());
        }
        return scheduled;
    }*/
    public void setLogStructure(SOAPMessage soapResponse) {
        try {
            NodeList nlODM = soapResponse.getSOAPBody().getElementsByTagName("result");
            Node nResult = nlODM.item(0);
            String sResult = nResult.getTextContent();
            if (sResult.equals("Success")) {
                this.logStructure = new LogStructure("Success", "The '" + this.eventOID + "' is scheduled succefully in  the study '" + this.studyPUID + "' for '" + this.subjectID+"'.");
            } else {
                Node nError = soapResponse.getSOAPBody().getElementsByTagName("error").item(0);
                this.logStructure = new LogStructure("Error", nError.getTextContent());
            }
        } catch (SOAPException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public LogStructure getLogStructure() {
        return logStructure;
    }



    /*  public String getEventOID(SOAPMessage soapResponse) {
        String  eventOID = "";
        try {
            NodeList nlODM = soapResponse.getSOAPBody().getElementsByTagName("result");
            Node nResult = nlODM.item(0);
            String sResult = nResult.getTextContent();
            if (sResult.equals("Success")) {
                scheduled = true;
            }
        } catch (SOAPException ex) {
            System.out.println(ex.getMessage());
        }
        return scheduled;
    }*/
    public static void main(String[] args) {
        ScheduleSubjectEvent_ws scheduleSubjectevent_ws = new ScheduleSubjectEvent_ws("subjectID", "testingStudy", "SE_FIRSTEVENT_1107");
        scheduleSubjectevent_ws.createSOAPRequest();
    }
}
