/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocws;

import java.io.IOException;
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
public class IsStudySubject_ws {

    String studyPUID;
    String subjectID;
    String sitePUID;
    LogStructure logStructure;

    public IsStudySubject_ws(String studyPUID, String subjectID, String sitePUID) {
        this.studyPUID = studyPUID;
        this.subjectID = subjectID;
        this.sitePUID = sitePUID;
    }

    public IsStudySubject_ws(String studyPUID, String subjectID) {
        this.studyPUID = studyPUID;
        this.subjectID = subjectID;
        this.sitePUID = "";
    }

    public SOAPMessage createSOAPRequest() {
        SOAPMessage soapResponse = null;
        try {
            User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            String serverURI = "https://openclinica-testing.medschl.cam.ac.uk/OCplay-ws/ws/studySubject/v1/studySubjectWsdl.wsdl";

            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addAttribute(new QName("xmlns:v1"), "http://openclinica.org/ws/studySubject/v1");
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
            SOAPElement isStudySubjectRequest = body.addChildElement("isStudySubjectRequest", "v1");
            SOAPElement studySubject = isStudySubjectRequest.addChildElement("studySubject", "v1");
            SOAPElement label = studySubject.addChildElement("label", "bean");
            label.addTextNode(this.subjectID);
            SOAPElement studyRef = studySubject.addChildElement("studyRef", "bean");
            SOAPElement identifier = studyRef.addChildElement("identifier", "bean");
            identifier.addTextNode(this.studyPUID);
            if (!this.sitePUID.isEmpty()) {
                SOAPElement siteRef = studySubject.addChildElement("siteRef", "bean");
                SOAPElement siteRefIdentifier = siteRef.addChildElement("identifier", "bean");
                siteRefIdentifier.addTextNode(this.sitePUID);
                studyRef.addChildElement(siteRef);
            }
            System.out.println("Elementbody added");
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "create");
            soapMessage.saveChanges();

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            soapMessage.writeTo(System.out);
            System.out.println();
            soapResponse = soapConnection.call(soapMessage, serverURI);
            // setLogStructure(soapResponse);
            System.out.print("Request SOAP Message = ");
            soapResponse.writeTo(System.out);
            System.out.println();
        } catch (SOAPException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return soapResponse;
    }

    public boolean isStudySubjectExist(SOAPMessage soapResponse) {
        boolean exist = false;
        try {
            NodeList nlODM = soapResponse.getSOAPBody().getElementsByTagName("result");
            Node nResult = nlODM.item(0);
            String sResult = nResult.getTextContent();
            if (sResult.equals("Success")) {
                exist = true;
                this.logStructure = new LogStructure("Success", " The subject '" + this.subjectID + "' exists already in the study ' " + this.studyPUID + "'.");
            } else {
                Node nError = soapResponse.getSOAPBody().getElementsByTagName("error").item(0);
                this.logStructure = new LogStructure("Error", nError.getTextContent());
            }
        } catch (SOAPException ex) {
            this.logStructure = new LogStructure("Error", "EXCEPTIOJN");
            System.out.println(ex.getMessage());
        }
        return exist;
    }

    public String getStudySubjectOID(SOAPMessage soapResponse) {
        String subjectOID = "";
        try {
            NodeList nlODM = soapResponse.getSOAPBody().getElementsByTagName("result");
            Node nResult = nlODM.item(0);
            String sResult = nResult.getTextContent();
            if (sResult.equals("Success")) {
                NodeList nlSubjectOID = soapResponse.getSOAPBody().getElementsByTagName("subjectOID");
                Node nSubjectOID = nlSubjectOID.item(0);
                subjectOID = nSubjectOID.getTextContent();
                this.logStructure = new LogStructure("Success", " The subject: " + this.subjectID + " exists already in the study: " + this.studyPUID);
            } else {
                Node nError = soapResponse.getSOAPBody().getElementsByTagName("error").item(0);
                this.logStructure = new LogStructure("Error", nError.getTextContent());
            }
        } catch (SOAPException ex) {
            System.out.println(ex.getMessage());
        }
        return subjectOID;
    }

    public LogStructure getLogStructure() {
        return logStructure;
    }

    public void setLogStructure(SOAPMessage soapResponse) {
        try {
            NodeList nlODM = soapResponse.getSOAPBody().getElementsByTagName("result");
            Node nResult = nlODM.item(0);
            String sResult = nResult.getTextContent();
            if (sResult.equals("Success")) {
                this.logStructure = new LogStructure("Success", " The subject: " + this.subjectID + " exists already in the study: " + this.studyPUID);
            } else {
                Node nError = soapResponse.getSOAPBody().getElementsByTagName("error").item(0);
                this.logStructure = new LogStructure("Error", nError.getTextContent());
            }
        } catch (SOAPException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
