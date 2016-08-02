package com.github.danielpacak.soa.stockquote.service.ejb;

import javax.ejb.Stateless;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.stockquote.ObjectFactory;
import com.example.stockquote.StockQuotePortType;
import com.example.stockquote.TradePriceRQ;
import com.example.stockquote.TradePriceRS;
import com.github.danielpacak.soa.stockquote.repository.StockQuoteRepository;
import com.github.danielpacak.soa.stockquote.repository.memory.InMemoryStockQuoteRepository;

@WebService(targetNamespace = "http://example.com/stockquote", endpointInterface = "com.example.stockquote.StockQuotePortType", wsdlLocation = "/META-INF/wsdl/stock-quote.wsdl")
@Stateless
public class StockQuote implements StockQuotePortType {

  private static final Logger logger = LoggerFactory.getLogger(StockQuote.class);

  private ObjectFactory objectFactory;

  private StockQuoteRepository repository; // a la service layer

  public StockQuote() {
    objectFactory = new ObjectFactory();
    repository = new InMemoryStockQuoteRepository();
  }

  public StockQuote(StockQuoteRepository repository) {
    objectFactory = new ObjectFactory();
    this.repository = repository;
  }

  @Override
  public TradePriceRS getLastTradePrice(TradePriceRQ request) {
    logger.info("Getting last trade price for {} ticker", request.getTickerSymbol());
    float price = repository.getLastTradePrice(request.getTickerSymbol());
    TradePriceRS response = objectFactory.createTradePriceRS();
    response.setPrice(price);
    return response;
  }

}
