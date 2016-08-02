package com.github.danielpacak.soa.stockquote.service.ejb;

import static org.junit.Assert.assertEquals;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.danielpacak.soa.stockquote.service.ejb.StockQuote;

/**
 * This is just to test whether it's possible to deploy a web service implemented as a stateless session bean in a unit
 * test and shoot a valid or invalid request and assert on a response.
 * 
 * @author pacak.daniel@gmail.com
 */
public class SimpleIntegrationTest {

  private static Server server;

  @BeforeClass
  public static void before() {
    JaxWsServerFactoryBean sf = new JaxWsServerFactoryBean();
    sf.setServiceBean(new StockQuote());
    // FIXME Choose TCP/IP port randomly & check its availability.
    sf.setAddress("http://localhost:8680/stock-quote");
    sf.setWsdlURL(SimpleIntegrationTest.class.getResource("/META-INF/wsdl/stock-quote.wsdl").toString());
    server = sf.create();
  }

  @Test
  public void verifyWsdlIsAccessible() throws Exception {
    URL wsdl = new URL("http://localhost:8680/stock-quote?wsdl");
    HttpURLConnection connection = (HttpURLConnection) wsdl.openConnection();
    assertEquals(200, connection.getResponseCode());
  }

  @AfterClass
  public static void after() {
    server.stop();
  }

}
