package com.github.danielpacak.soa.stockquote.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.example.stockquote.ObjectFactory;
import com.example.stockquote.TradePriceRQ;
import com.example.stockquote.TradePriceRS;
import com.github.danielpacak.soa.stockquote.repository.StockQuoteRepository;

public class StockQuoteBeanTest {

    @Test
    public void testGetLastTradePrice() {
        StockQuoteRepository repository = mock(StockQuoteRepository.class);
        ObjectFactory of = new ObjectFactory();
        TradePriceRQ request = of.createTradePriceRQ();
        request.setTickerSymbol("GOOG");
        given(repository.getLastTradePrice("GOOG")).willReturn(6.9f);

        StockQuoteBean stockQuote = new StockQuoteBean(repository);
        TradePriceRS response = stockQuote.getLastTradePrice(request);
        assertEquals(6.9f, response.getPrice(), 0.01);
    }

}
