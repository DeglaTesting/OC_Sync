/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.rdcit.ocSync.map.ToDocument;
import org.rdcit.ocSync.model.User;
import org.w3c.dom.Document;

/**
 *
 * @author sa841
 */
public class ImportData_ws {

    File file;
    Document doc;

    public ImportData_ws(Document doc) {
        this.doc = doc;
            }

    public String fileToString() {
        String content = "";
        try {
            FileReader fr = new FileReader(this.file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
            content = sb.toString();
            System.out.println(content);
        } catch (IOException ex) {
            Logger.getLogger(ImportData_ws.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return content; 
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
            envelope.addAttribute(new QName("xmlns:v1"), "http://openclinica.org/ws/data/v1");
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
            SOAPElement importRequest = body.addChildElement("importRequest", "v1");
            SOAPElement odm = importRequest.addChildElement("odm");
           // SOAPElement ODM = odm.addChildElement("ODM");
            ToDocument toDocument= new ToDocument();
            //System.out.println(toDocument.toString(this.doc));
            String content = toDocument.toString(this.doc).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
            odm.addTextNode(content);
            System.out.println("Elementbody added");
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "create");
            soapMessage.saveChanges();
            soapMessage.writeTo(System.out);
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            soapResponse = soapConnection.call(soapMessage, serverURI);
            System.out.println("################# CREATE IMPORT DATA REQUEST  ################################################");
            System.out.print("Request SOAP Message = ");
            soapResponse.writeTo(System.out);
            System.out.println();
            System.out.println("################# CREATE IMPORT DATA REQUEST ################################################");
        } catch (SOAPException | IOException ex) {
            System.out.println(ex.getMessage());
        }
        return soapResponse;
    }

}
