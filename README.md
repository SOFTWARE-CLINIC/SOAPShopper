# soa-seed-stock-quote

> Demonstrates different ways to implement the same thing, i.e. a JAX-WS compliant SOAP Web Service in Java

[![Build Status](https://travis-ci.org/SOFTWARE-CLINIC/soa-seed-stock-quote.svg?branch=master)](https://travis-ci.org/SOFTWARE-CLINIC/soa-seed-stock-quote)
[![Dependency Status](https://www.versioneye.com/user/projects/57a18e333d8eb6004f9bcf2a/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57a18e333d8eb6004f9bcf2a)

* [Start from WSDL Using a Service Endpoint Interface (SEI)](#start-from-wsdl-using-a-service-endpoint-interface-sei)
* [Java SE Deployment with javax.xml.ws.Endpoint](#java-se-deployment-with-javaxxmlwsendpoint)

```xml
<?xml version="1.0"?>
<definitions name="StockQuoteService" targetNamespace="http://example.com/stockquote" xmlns:tns="http://example.com/stockquote"
  xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns="http://schemas.xmlsoap.org/wsdl/">
  <types>
    <schema xmlns="http://www.w3.org/2001/XMLSchema" targetNamespace="http://example.com/stockquote"
      elementFormDefault="qualified">
      <element name="TradePriceRequest">
        <complexType>
          <all>
            <element name="tickerSymbol" type="string" />
          </all>
        </complexType>
      </element>
      <element name="TradePriceResponse">
        <complexType>
          <all>
            <element name="price" type="float" />
          </all>
        </complexType>
      </element>
    </schema>
  </types>

  <message name="GetLastTradePriceInput">
    <part name="body" element="tns:TradePriceRequest" />
  </message>

  <message name="GetLastTradePriceOutput">
    <part name="body" element="tns:TradePriceResponse" />
  </message>

  <portType name="StockQuotePortType">
    <operation name="GetLastTradePrice">
      <input message="tns:GetLastTradePriceInput" />
      <output message="tns:GetLastTradePriceOutput" />
    </operation>
  </portType>

  <binding name="StockQuoteSoapBinding" type="tns:StockQuotePortType">
    <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http" />
    <operation name="GetLastTradePrice">
      <soap:operation soapAction="GetLastTradePrice" />
      <input>
        <soap:body use="literal" />
      </input>
      <output>
        <soap:body use="literal" />
      </output>
    </operation>
  </binding>

  <service name="StockQuoteService">
    <port name="StockQuotePort" binding="tns:StockQuoteSoapBinding">
      <soap:address location="SERVICE_ENDPOINT_URL_PLACEHOLDER" />
    </port>
  </service>

</definitions>
```

## Start from WSDL Using a Service Endpoint Interface (SEI)

When starting from an existing WSDL document in JAX-WS, the most straightforward way to implement a Web service that
conforms to the WSDL contract is to use a *service endpoint interface* (SEI). A SEI is a Java interface mapped from a
`wsdl:portType` using the JAX-WS WSDL to Java mapping and JAXB XML to Java mapping. A SEI is specified using the
`@WebService.endpointInterface` attribute, as shown in the example below.

```java
@WebService(portName = "StockQuotePort",
    serviceName = "StockQuoteService",
    targetNamespace = "http://example.com/stockquote",
    endpointInterface = "com.example.stockquote.StockQuotePortType",
    wsdlLocation = "/META-INF/wsdl/stock-quote.wsdl")
public class StockQuoteBean implements StockQuotePortType {

  @Resource
  private WebServiceContext context;

  @Override
  public TradePriceResponse getLastTradePrice(TradePriceRequest request) {
    // ... //
  }

}
```

The class `StockQuoteBean`, shown in the example above, implements the SEI `StockQuotePortType`. In the terminology of
WS-Metadata, `StockQuoteBean` is a *service implementation bean* (SIB). A SIB contains the business logic of a Web
service. It must be annotated with either `@WebService` or `@WebServiceProvider`.

Another thing to notice is the *dependency injection* defined by the `@Resource` annotation.
The `javax.xml.ws.WebServiceContext` interface makes it possible for a SIB to access contextual information pertaining
to the request being served. You can use the `WebServiceContext` to access the `javax.xml.soap.SOAPMessageContext`.
This enables the SIB to access the results of processing that took place in the handler chain (e.g. the security profile
for the user who sent the message).

The following example shows the `StockQuotePortType` SEI that has been generated from the WSDL.

```java
@WebService(name = "StockQuotePortType", targetNamespace = "http://example.com/stockquote")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({ObjectFactory.class})
public interface StockQuotePortType {

  @WebMethod(operationName = "GetLastTradePrice", action = "GetLastTradePrice")
  @WebResult(name = "TradePriceResponse", targetNamespace = "http://example.com/stockquote", partName = "body")
  public TradePriceResponse getLastTradePrice(
    @WebParam(name = "TradePriceRequest", targetNamespace = "http://example.com/stockquote", partName = "body")
    TradePriceRequest body);

}
```

The [stock-quote-service-sei](/stock-quote-service-sei) module defines the `StockQuoteBean` SIB. After deployment the
Web service is available at [http://localhost:7001/stock-quote-service-sei/stock-quote](http://localhost:7001/stock-quote-service-sei/stock-quote)
(see the mapping in `web.xml`).

## Providers and XML Processing without JAXB

Sometimes you might not want to work with JAXB-generated classes. You might want to implement the business logic
directly against the XML. XML processing can sometimes improve performance - especially if you can implement the
business logic using an XSLT stylesheet. It also provides a degree of insulation from changes in the WSDL.

To deploy a Web service that enables you to work with XML messages directly - without the JAXB binding - you
use the `javax.xml.ws.Provider<T>` interface. The `Provider<T>` interface defines a single method:

```java
T invoke(T request);
```

http://localhost:7001/stock-quote-service-provider/stock-quote-provider

## Java SE Deployment with `javax.xml.ws.Endpoint`

The [stock-quote-service-javase](/stock-quote-service-javase) module defines the `StockQuotePublisher` class
which is a Java SE application that publishes the Stock Quote Web Service at
[http://localhost:8680/stock-quote?wsdl](http://localhost:8680/stock-quote?wsdl).

## Maven Modules

stock-quote-service-sei - An example of how to create, deploy, and invoke a JAX-WS
endpoint using an existing WSDL contract and a service endpoint interface (SEI).

http://192.168.1.100:7001/stock-quote-service-spring-ws/stock-quote.wsdl
