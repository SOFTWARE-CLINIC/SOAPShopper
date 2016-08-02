package com.github.danielpacak.soa.stockquote.endpoint;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.danielpacak.soa.stockquote.repository.StockQuoteRepository;

/**
 * Tests for {@link StockQuoteEndpoint}.
 */
@RunWith(MockitoJUnitRunner.class)
public class StockQuoteEndpointTest {

  @Mock
  private StockQuoteRepository repository;

  @Test
  public void testGetLastTradePrice() throws Exception {
    StockQuoteEndpoint endpoint = new StockQuoteEndpoint(repository);
    Namespace namespace = Namespace.getNamespace("http://example.com/stockquote");
    Element tradePriceRequest = new Element("TradePriceRequest", namespace);
    Element price = new Element("tickerSymbol", namespace);
    price.setText("GOOG");
    tradePriceRequest.addContent(price);
    BDDMockito.given(repository.getLastTradePrice("GOOG")).willReturn(new Float(69.0));

    Element tradePriceResponse = endpoint.getLastTradePrice(tradePriceRequest);
    System.out.println(tradePriceResponse.getChild("price", namespace).getText());
  }

}
