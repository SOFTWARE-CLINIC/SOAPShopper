package com.github.danielpacak.soa.stockquote.repository.memory;

import java.util.HashMap;
import java.util.Map;

import com.github.danielpacak.soa.stockquote.repository.StockQuoteRepository;

public class InMemoryStockQuoteRepository implements StockQuoteRepository {

    private Map<String, Float> tickerSymbolToPrice;

    public InMemoryStockQuoteRepository() {
        tickerSymbolToPrice = new HashMap<String, Float>();
        tickerSymbolToPrice.put("GOOG", 13.3f);
        tickerSymbolToPrice.put("MSF", 14.2f);
    }

    @Override
    public float getLastTradePrice(String tickerSymbol) {
        checkTickerSymbol(tickerSymbol);
        return tickerSymbolToPrice.get(tickerSymbol);
    }

    private void checkTickerSymbol(String tickerSymbol) {
        if (!tickerSymbolToPrice.containsKey(tickerSymbol)) {
            throw new IllegalArgumentException("Unknown ticker symbol [" + tickerSymbol + "].");
        }
    }

}
