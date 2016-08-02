package com.github.danielpacak.soa.stockquote.client;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;

import com.example.stockquote.ObjectFactory;
import com.example.stockquote.StockQuotePortType;
import com.example.stockquote.StockQuoteService;
import com.example.stockquote.TradePriceRQ;
import com.example.stockquote.TradePriceRS;

public class StockQuoteClient {

    private StockQuotePortType stockQuotePortType;

    public StockQuoteClient(URL wsdlLocation, Handler<?>... handlers) {
        List<Handler> handlerChain = new LinkedList<Handler>();
        handlerChain.addAll(Arrays.asList(handlers));

        StockQuoteService stockQuoteService = new StockQuoteService(wsdlLocation);
        stockQuotePortType = stockQuoteService.getStockQuotePort();
        SOAPBinding binding = (SOAPBinding) ((BindingProvider) stockQuotePortType).getBinding();
        binding.setHandlerChain(handlerChain);
        binding.setMTOMEnabled(true);
    }

    public float getLastTradePrice(String tickerSymbol) {
        ObjectFactory of = new ObjectFactory();
        TradePriceRQ request = of.createTradePriceRQ();
        request.setTickerSymbol(tickerSymbol);

        TradePriceRS response = stockQuotePortType.getLastTradePrice(request);
        return response.getPrice();
    }

    public static void main(String[] args) throws Exception {
        // URL wsdlLocation = new URL("http://localhost:8680/stock-quote?wsdl");
        URL wsdlLocation = new URL("http://localhost:8088/mockStockQuoteSoapBinding?wsdl");
        StockQuoteClient client = new StockQuoteClient(wsdlLocation, new EBMessageHeaderHandler(),
                new WSSUsernameTokenSecurityHandler());
        System.out.println(client.getLastTradePrice("GOOG"));
    }

}
