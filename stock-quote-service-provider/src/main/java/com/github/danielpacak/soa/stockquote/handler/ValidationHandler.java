package com.github.danielpacak.soa.stockquote.handler;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class ValidationHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        System.out.println("Validating " + (isInboundMessage(context) ? "inbound" : "outbound") + " message...");
        return true;
    }

    private boolean isInboundMessage(SOAPMessageContext context) {
        Boolean outboundMessage = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        return !outboundMessage;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {
        // Do nothing here.
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
