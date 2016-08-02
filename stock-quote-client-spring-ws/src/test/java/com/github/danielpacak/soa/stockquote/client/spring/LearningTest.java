package com.github.danielpacak.soa.stockquote.client.spring;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringSource;

public class LearningTest {

  @Test
  @Ignore
  public void testMe() throws Exception {
    final String messageHeader = "<eb:MessageHeader eb:version=\"2.0\" xmlns:eb=\"http://www.ebxml.org/namespaces/messageHeader\">"
    + "<eb:ConversationId>hotelexport@hrs.de</eb:ConversationId>"
    + "<eb:From>"
    + "<eb:PartyId type=\"urn:x12.org:IO5:01\">hotelexport.hrs.de</eb:PartyId>"
    + "</eb:From>"
    + "<eb:To>"
    + "<eb:PartyId type=\"urn:x12.org:IO5:01\">webservices.sabre.com</eb:PartyId>"
    + "</eb:To>"
    + "<eb:CPAId>C1PH</eb:CPAId>"
    + "<eb:Service eb:type=\"sabreXML\">Session</eb:Service>"
    + "<eb:Action>SessionCreateRQ</eb:Action>"
    + "<eb:MessageData>"
    + "<eb:MessageId>mid:1@hrs.de</eb:MessageId>"
    + "<eb:Timestamp>2014-02-26T11:15:12Z</eb:Timestamp>"
    + "<eb:TimeToLive>2014-02-26T11:15:12Z</eb:TimeToLive>"
    + "</eb:MessageData>"
    + "</eb:MessageHeader>";
    final String securityHeader = "<wsse:Security xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2002/12/secext\" xmlns:wsu=\"http://schemas.xmlsoap.org/ws/2002/12/utility\">"
        + "<wsse:UsernameToken>"
          + "<wsse:Username>dpacak</wsse:Username>"
          + "<wsse:Password>dpacak</wsse:Password>"
          + "<Organization>PSD_Content_Hotels</Organization>"
          + "<Domain>Sabre</Domain>"
        + "</wsse:UsernameToken>"
    + "</wsse:Security>";
    
    String body = "<eb:Manifest eb:version=\"2.0\" xmlns:eb=\"http://www.ebxml.org/namespaces/messageHeader\">"
    + "<eb:Reference xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"cid:SessionCreateRQ\" xlink:type=\"simple\" />"
    + "</eb:Manifest>";

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    WebServiceTemplate template = new WebServiceTemplate();
    template.setDefaultUri("http://localhost:8088");
    //template.setDefaultUri("http://localhost:8088/mockStockQuoteSoapBinding");
    //template.setDefaultUri("http://localhost:8080/stock-quote-service-ejb/StockQuote");

    StreamSource source = new StreamSource(new StringReader(body));
    StreamResult result = new StreamResult(System.out);

    WebServiceMessageCallback messageCallback = new WebServiceMessageCallback() {

      @Override
      public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
        SoapMessage soapMessage = (SoapMessage) message;
        //soapMessage.setSoapAction("GetLastTradePrice");
        
        SoapHeader soapHeader = soapMessage.getSoapHeader();
        StringSource messageHeaderSource = new StringSource(messageHeader);
        StringSource securityHeaderSource = new StringSource(securityHeader);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(messageHeaderSource, soapHeader.getResult());
        transformer.transform(securityHeaderSource, soapHeader.getResult());
      }
    };

    template.sendSourceAndReceiveToResult(source, messageCallback, result);
  }

}
