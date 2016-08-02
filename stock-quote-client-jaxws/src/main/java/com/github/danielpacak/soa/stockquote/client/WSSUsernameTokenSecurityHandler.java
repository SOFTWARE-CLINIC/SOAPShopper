package com.github.danielpacak.soa.stockquote.client;

import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class WSSUsernameTokenSecurityHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String WSSE_PREFIX = "wsse";
    private static final String WSSE_URI = "http://schemas.xmlsoap.org/ws/2002/12/secext";
    private static final String WSU_PREFIX = "wsu";
    private static final String WSU_URI = "http://schemas.xmlsoap.org/ws/2002/12/utility";

    private final String username;
    private final String password;
    private final String organization;
    private final String domain;

    public WSSUsernameTokenSecurityHandler() {
        username = "dpacak";
        password = "dpacak1";
        organization = "PSD_Content_Hotels";
        domain = "Sabre";
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty) {

            try {
                SOAPMessage soapMessage = context.getMessage();
                SOAPPart soapPart = soapMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();

                SOAPFactory factory = SOAPFactory.newInstance();
                SOAPElement securityElement = factory.createElement("Security", WSSE_PREFIX, WSSE_URI);
                securityElement.addNamespaceDeclaration(WSU_PREFIX, WSU_URI);
                SOAPElement tokenElem = factory.createElement("UsernameToken", WSSE_PREFIX, WSSE_URI);
                SOAPElement userElem = factory.createElement("Username", WSSE_PREFIX, WSSE_URI);
                userElem.addTextNode(username);
                SOAPElement pwdElem = factory.createElement("Password", WSSE_PREFIX, WSSE_URI);
                pwdElem.addTextNode(password);
                tokenElem.addChildElement(userElem);
                tokenElem.addChildElement(pwdElem);
                SOAPElement organizationElem = tokenElem.addChildElement("Organization");
                organizationElem.addTextNode(organization);
                SOAPElement domainElem = tokenElem.addChildElement("Domain");
                domainElem.addTextNode(domain);

                SOAPHeader soapHeader = envelope.getHeader();
                if (soapHeader == null) {
                    soapHeader = envelope.addHeader();
                }
                securityElement.addChildElement(tokenElem);
                soapHeader.addChildElement(securityElement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return new TreeSet<QName>();
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {
        //
    }
}