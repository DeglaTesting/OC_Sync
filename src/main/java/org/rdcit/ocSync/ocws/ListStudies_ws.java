/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.ocws;

import javax.faces.context.FacesContext;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.rdcit.ocSync.model.Study;
import org.rdcit.ocSync.model.User;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author sa841
 */
public class ListStudies_ws {

    User user;

    public ListStudies_ws(User user) {
        this.user = user;
    }

    private SOAPMessage createSOAPRequest() throws Exception {
         User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "https://openclinica-testing.medschl.cam.ac.uk:443/OCplay-ws/ws/studySubject/v1";

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addAttribute(new QName("xmlns:v1"), "http://openclinica.org/ws/study/v1");
        envelope.addAttribute(new QName("xmlns:bean"), "http://openclinica.org/ws/beans");

        SOAPHeader header = envelope.getHeader();
        SOAPBody body = envelope.getBody();

        SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        security.addAttribute(new QName("SOAP-ENV:mustUnderstand"), "1");
        SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
        usernameToken.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
        usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-27777511");
        SOAPElement username = usernameToken.addChildElement("Username", "wsse");
        username.addTextNode(this.user.getUser_name());
        SOAPElement password = usernameToken.addChildElement("Password", "wsse");
        password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        password.addTextNode(this.user.getPassword());
        SOAPElement createRequest = body.addChildElement("listAllRequest", "v1");

        System.out.println("Elementbody added");
        System.out.println(body.getChildElements());
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "create");

        soapMessage.saveChanges();
        return soapMessage;
    }

    public void getUserStudyList() {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            String url = "https://openclinica-testing.medschl.cam.ac.uk/OCplay-ws/ws/study/v1/studyWsdl.wsdl";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);
            NodeList nlStudies = soapResponse.getSOAPBody().getElementsByTagName("study");
            for (int i = 0; i < nlStudies.getLength(); i++) {
                NodeList nlStudy = nlStudies.item(i).getChildNodes();
                Study study = new Study();
                for (int j = 0; j < nlStudy.getLength(); j++) {
                    Node nIdentifier = nlStudy.item(j);
                    switch (nIdentifier.getNodeName()) {
                        case "identifier":
                            study.setStudy_u_p_id(nIdentifier.getTextContent());
                            break;
                        case "oid":
                            study.setStudy_oid(nIdentifier.getTextContent());
                            break;
                        case "name":
                            study.setStudy_name(nIdentifier.getTextContent());
                            break;
                        default:
                            break;
                    }
                }
                System.out.println(study.toString());
                this.user.addStudy(study);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getSessionMap().put("studyUserList", this.user.getlStudy());
    }
}
