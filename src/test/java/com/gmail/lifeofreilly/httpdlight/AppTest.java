package com.gmail.lifeofreilly.httpdlight;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Defines the Integration Tests for HTTPdLight.
 *
 * @author Seth Reilly
 * @version 1.0, April 2013
 */
public class AppTest extends TestCase {
    private static String CRLF = "\r\n";
    private static String host = "127.0.0.1";
    private static int port = 50001;
    private static boolean running = false;

    /**
     * Creates the test case.
     *
     * @param testName name of the test case.
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * Builds test suite dynamically by extracing test methods.
     *
     * @return the suite of tests being tested.
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * HTTPdLight Integration Test - Verify that an http connection can be established.
     * Test requires a running HTTPdLight server instance - see setUp().
     */
    public void test_IT_HTTPdLight_openConnection_OK() {
        assertEquals("OK", httpRequest("http://" + host + ":" + port));
    }

    /**
     * HTTPdLight Integration Test - Verify 404 response when requested resource doesn't exist.
     * Tests requires a running HTTPdLight server instance - see setUp().
     */
    public void test_IT_HTTPdLight_openConnection_NotFound() {
        assertEquals("Not Found", httpRequest("http://" + host + ":" + port + "/nopagehere.html"));
    }

    /**
     * Starts an instance of HTTPdLight for integration tests.
     */
    public void setUp() {
        if (!running) {
            System.out.println("Starting HTTPdLight test instance on port: " + port);
            System.out.println("Working directory: " + System.getProperty("user.dir"));
            System.out.println("Test Server Logs: " + System.getProperty("user.dir") + "/logs/");
            HTTPdLight httpd = new HTTPdLight(port);
            HTTPdLight.initializeDefaultContent();
            new Thread(httpd).start();
            running = true;
        }
    }

    /**
     * Opens an http connection and returns the response message
     *
     * @param url the test url.
     * @return the http response message
     */
    public static String httpRequest(String url) {
        String response = new String();
        try {
            URL testURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) testURL.openConnection();
            response = connection.getResponseMessage();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}