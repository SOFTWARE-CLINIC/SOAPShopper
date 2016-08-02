package com.github.danielpacak.soa.stockquote.service.ejb

import static org.junit.Assert.*

import javax.xml.ws.Endpoint;

import org.apache.cxf.endpoint.Server
import org.apache.cxf.jaxws.JaxWsServerFactoryBean
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import com.github.danielpacak.soa.stockquote.repository.memory.InMemoryStockQuoteRepository;

import spock.lang.Specification

// send some basic soap messaget to make sure that jaxb/jax-ws bindings/mapping are correct.
class SimpleIntegrationTest extends Specification {

  private static Server server
  private static InMemoryStockQuoteRepository repository

  @BeforeClass
  static void before() {
    repository = new InMemoryStockQuoteRepository()
    JaxWsServerFactoryBean sf = new JaxWsServerFactoryBean()
    sf.setServiceBean(new StockQuote(repository))
    sf.address = "http://localhost:8680/stock-quote"
    sf.wsdlURL =   SimpleIntegrationTest.class.getResource("/META-INF/wsdl/stock-quote.wsdl").toString()
    server = sf.create()
  }

  @Test
  def "WSDL is accessible"() throws Exception {
    given:
    URL wsdl = new URL("http://localhost:8680/stock-quote?wsdl")
    
    when:
    def connection = wsdl.openConnection()
    
    then:
    200 == connection.responseCode
  }
  
  @Test
  def "wrong port causes IOException"() throws Exception {
    given:
    URL wsdl = new URL("http://localhost:1111/stock-quote?wsdl")
    def connection = wsdl.openConnection()
    
    when:
    connection.responseCode
    
    then:
    thrown IOException
  }

    
  @AfterClass
  static void after() {
    server.stop()
  }
  
}
