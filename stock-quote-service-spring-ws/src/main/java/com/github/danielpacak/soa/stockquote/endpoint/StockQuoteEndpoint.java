package com.github.danielpacak.soa.stockquote.endpoint;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.xpath.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.github.danielpacak.soa.stockquote.repository.StockQuoteRepository;

@Endpoint
public class StockQuoteEndpoint {

  private static final String NAMESPACE_URI = "http://example.com/stockquote";

  private StockQuoteRepository repository;

  private Namespace namespace;
  private XPath tickerSymbolExpression;

  @Autowired
  public StockQuoteEndpoint(StockQuoteRepository repository) throws JDOMException {
    this.repository = repository;
    namespace = Namespace.getNamespace("sq", NAMESPACE_URI);
    tickerSymbolExpression = XPath.newInstance("//sq:tickerSymbol");
    tickerSymbolExpression.addNamespace(namespace);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TradePriceRequest")
  @ResponsePayload
  public Element getLastTradePrice(@RequestPayload Element tradePriceRequest) {
    try {
      String tickerSymbol = tickerSymbolExpression.valueOf(tradePriceRequest);

      Float tradePrice = repository.getLastTradePrice(tickerSymbol);

      Element tradePriceResponse = new Element("TradePriceResponse", namespace);
      Element price = new Element("price", namespace);
      price.setText(String.valueOf(tradePrice));
      tradePriceResponse.addContent(price);

      return tradePriceResponse;
    } catch (JDOMException e) {
      throw new IllegalStateException("Error while evaluating ticker symbol!", e);
    }

  }

}
