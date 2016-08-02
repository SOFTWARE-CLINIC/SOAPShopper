package com.github.danielpacak.soa.stockquote.service;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;

import org.w3c.dom.Document;

@WebServiceProvider(serviceName = "StockQuoteService",
        portName = "StockQuotePort",
        targetNamespace = "http://example.com/stockquote",
        wsdlLocation = "/META-INF/wsdl/stock-quote.wsdl")
@ServiceMode(Service.Mode.PAYLOAD)
@HandlerChain(file = "handler-chains.xml")
public class StockQuoteProvider implements Provider<Source> {

    @Resource
    private WebServiceContext context;

    @Override
    public Source invoke(Source payload) {
        System.out.println("In StockQuoteProvider...");
        String ns = "http://example.com/stockquote";
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();

            Document respDoc = docBuilder.newDocument();
            Document payloadDoc = docBuilder.newDocument();
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(payload, new DOMResult(payloadDoc));

            // TODO Implement request validation and return response with DOM API.
            return payload;
        } catch (ParserConfigurationException e) {
            throw new WebServiceException(e);
        } catch (TransformerConfigurationException e) {
            throw new WebServiceException(e);
        } catch (TransformerException e) {
            throw new WebServiceException(e);
        }
    }

}
