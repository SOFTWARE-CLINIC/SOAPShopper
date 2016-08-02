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

public class EBMessageHeaderHandler implements SOAPHandler<SOAPMessageContext> {
    private static final String EB_PREFIX = "eb";
    private static final String EB_URI = "http://www.ebxml.org/namespaces/messageHeader";

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty) {

            try {
                SOAPMessage soapMessage = context.getMessage();
                SOAPPart soapPart = soapMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();

                SOAPFactory factory = SOAPFactory.newInstance();
                SOAPElement messageHeaderElement = factory.createElement("MessageHeader", EB_PREFIX, EB_URI);

                SOAPElement conversationIdElem = factory.createElement("ConversationId", EB_PREFIX, EB_URI);
                conversationIdElem.addTextNode("ABC123@clientURL.com");
                messageHeaderElement.addChildElement(conversationIdElem);

                SOAPHeader soapHeader = envelope.getHeader();
                if (soapHeader == null) {
                    soapHeader = envelope.addHeader();
                }

                soapHeader.addChildElement(messageHeaderElement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public Set<QName> getHeaders() {
        return new TreeSet<QName>();
    }

    @Override
    public void close(MessageContext context) {
        // Do nothing here.
    }

}
