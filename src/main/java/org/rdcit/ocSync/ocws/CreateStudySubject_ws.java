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
public class CreateStudySubject_ws {

    String studyPUID;
    String sitePUID;
    String subjectID;
    String subjectGendre;
    String subjectDateOfBirth;
    String enrollmentDate;
    String message = "";
    LogStructure logStructure;

    public CreateStudySubject_ws(String studyPUID, String subjectID, String subjectGendre, String subjectDateOfBirth) {
        this.studyPUID = studyPUID;
        this.subjectID = subjectID;
        this.subjectGendre = subjectGendre;
        this.subjectDateOfBirth = subjectDateOfBirth;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        this.enrollmentDate = dateFormat.format(date);
        this.sitePUID = "";

    }

    public CreateStudySubject_ws(String studyPUID, String sitePUID, String subjectID, String subjectGendre, String subjectDateOfBirth) {
        this.studyPUID = studyPUID;
        this.sitePUID = sitePUID;
        this.subjectID = subjectID;
        this.subjectGendre = subjectGendre;
        this.subjectDateOfBirth = subjectDateOfBirth;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        this.enrollmentDate = dateFormat.format(date);

    }

    public SOAPMessage createSOAPRequest() {
        User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        SOAPMessage soapResponse = null;
        try {
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
            SOAPElement isStudySubjectRequest = body.addChildElement("createRequest", "v1");
            SOAPElement studySubject = isStudySubjectRequest.addChildElement("studySubject", "v1");
            SOAPElement label = studySubject.addChildElement("label", "bean");
            label.addTextNode(this.subjectID);
            SOAPElement enrollmentDate = studySubject.addChildElement("enrollmentDate", "bean");
            enrollmentDate.addTextNode(this.enrollmentDate);
            SOAPElement subject = studySubject.addChildElement("subject", "bean");
            SOAPElement gender = subject.addChildElement("gender", "bean");
            gender.addTextNode(this.subjectGendre);
            SOAPElement dateOfBirth = subject.addChildElement("dateOfBirth", "bean");
            dateOfBirth.addTextNode(this.subjectDateOfBirth);
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
            soapMessage.writeTo(System.out);
            System.out.println();
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            soapResponse = soapConnection.call(soapMessage, serverURI);
            setLogStructure(soapResponse);
            //  soapResponse.writeTo(System.out);
        } catch (SOAPException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return soapResponse;
    }

    public LogStructure getLogStructure() {
        return logStructure;
    }

    public void setLogStructure(SOAPMessage soapResponse) {
        try {
            NodeList nlODM = soapResponse.getSOAPBody().getElementsByTagName("createResponse");
            Node nResult = nlODM.item(0);
            String sResult = nResult.getTextContent();
            if (sResult.equals("Success")) {
                this.logStructure = new LogStructure("Success", "The subject '" + this.subjectID + "' is created successfully into '" + this.studyPUID + "'.");
            } else {
                Node nError = soapResponse.getSOAPBody().getElementsByTagName("error").item(0);
                this.logStructure = new LogStructure("Error", nError.getTextContent());
            }
        } catch (SOAPException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
