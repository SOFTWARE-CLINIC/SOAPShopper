package com.github.danielpacak.soa.stockquote.webclient;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;

import com.example.stockquote.ObjectFactory;
import com.example.stockquote.StockQuotePortType;
import com.example.stockquote.StockQuoteService;
import com.example.stockquote.TradePriceRQ;
import com.example.stockquote.TradePriceRS;

public class StockQuoteClientServlet extends HttpServlet {

    private static final long serialVersionUID = 2309272669311319689L;

    @WebServiceRef(value = StockQuoteService.class, wsdlLocation = "http://localhost:8088/mockStockQuoteSoapBinding?WSDL")
    private StockQuotePortType stockQuotePortType;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tickerSymbol = req.getParameter("tickerSymbol");
        if (tickerSymbol == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The 'tickerSymbol' request parameter is required.");
            return;
        }
        ObjectFactory of = new ObjectFactory();
        TradePriceRQ tradePriceRequest = of.createTradePriceRQ();
        tradePriceRequest.setTickerSymbol(tickerSymbol);
        TradePriceRS tradePriceResponse = stockQuotePortType.getLastTradePrice(tradePriceRequest);
        PrintWriter out = resp.getWriter();
        out.println(tradePriceResponse.getPrice());
    }

}
