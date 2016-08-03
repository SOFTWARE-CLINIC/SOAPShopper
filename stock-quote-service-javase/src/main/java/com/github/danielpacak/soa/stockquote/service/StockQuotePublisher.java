package com.github.danielpacak.soa.stockquote.service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Binding;
import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;

import com.example.stockquote.ObjectFactory;
import com.github.danielpacak.soa.stockquote.repository.StockQuoteRepository;
import com.github.danielpacak.soa.stockquote.repository.memory.InMemoryStockQuoteRepository;

public class StockQuotePublisher {

    public static void main(String[] args) {
        new StockQuotePublisher().publish();
    }

    public void publish() {
        ObjectFactory objectFactory = new ObjectFactory();
        StockQuoteRepository repository = new InMemoryStockQuoteRepository();
        StockQuote stockQuote = new StockQuote(objectFactory, repository);
        Endpoint endpoint = Endpoint.create(stockQuote);
        Binding binding = endpoint.getBinding();
        List<Handler> handlerChain = new LinkedList<Handler>();
        handlerChain.add(new ValidateCredentialsHandler());
        binding.setHandlerChain(handlerChain);

        List<Source> metadata = new ArrayList<Source>(1);
        metadata.add(getWsdlAsSource("/META-INF/wsdl/stock-quote.wsdl"));
        endpoint.setMetadata(metadata);

        endpoint.publish("http://localhost:8680/stock-quote");
    }

    private Source getWsdlAsSource(String classPath) {
        URL wsdlLocation = getClass().getResource(classPath);
        if (wsdlLocation == null) {
            throw new IllegalArgumentException("Cannot find WSDL under the class path [" + classPath + "]");
        }
        InputStream wsdlStream = getClass().getResourceAsStream(classPath);
        return new StreamSource(wsdlStream, wsdlLocation.toExternalForm());
    }

}
