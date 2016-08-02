package com.github.danielpacak.soa.stockquote.repository;

public interface StockQuoteRepository {

    // TODO Use BigDecimal for monetary data?
    float getLastTradePrice(String tickerSymbol);

}
