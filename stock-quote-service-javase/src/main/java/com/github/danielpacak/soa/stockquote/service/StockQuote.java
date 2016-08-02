package com.github.danielpacak.soa.stockquote.service;

import javax.jws.WebService;

import com.example.stockquote.ObjectFactory;
import com.example.stockquote.StockQuotePortType;
import com.example.stockquote.TradePriceRQ;
import com.example.stockquote.TradePriceRS;
import com.github.danielpacak.soa.stockquote.repository.StockQuoteRepository;

@WebService(name = "StockQuote", targetNamespace = "http://example.com/stockquote", endpointInterface = "com.example.stockquote.StockQuotePortType")
public class StockQuote implements StockQuotePortType {

  private final ObjectFactory objectFactory;
  private final StockQuoteRepository repository;

  public StockQuote(ObjectFactory objectFactory, StockQuoteRepository repository) {
    this.objectFactory = objectFactory;
    this.repository = repository;
  }

  @Override
  public TradePriceRS getLastTradePrice(TradePriceRQ request) {
    String tickerSymbol = request.getTickerSymbol();
    float tradePrice = repository.getLastTradePrice(tickerSymbol);
    TradePriceRS response = objectFactory.createTradePriceRS();
    response.setPrice(tradePrice);
    return response;
  }

}
