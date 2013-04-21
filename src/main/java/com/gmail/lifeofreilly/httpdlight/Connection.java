package com.gmail.lifeofreilly.httpdlight;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.net.Socket;
import java.util.HashMap;

/**
 * Defines the connection handler for HTTPdLight.
 *
 * @author Seth Reilly
 * @version 1.0, April 2013
 */
class Connection implements Runnable {
    private Socket socket;
    private BufferedReader input;
    private OutputStream output;
    private String webroot;
    private String indexPage;
    private String notFoundPage;
    private final static Logger log = Logger.getLogger(Connection.class);
    private final static Logger logActivity = Logger.getLogger("activityLogger");

    /**
     * Sole constructor.
     *
     * @param s        the accepted socket connection.
     * @param root     the webroot directory.
     * @param index    the name of the defalt index page
     * @param notFound the name of the default 404 error page
     */
    protected Connection(Socket s, String root, String index, String notFound) {
        socket = s;
        webroot = root;
        indexPage = index;
        notFoundPage = notFound;
    }

    /**
     * Establishes I/O streams and processes request.
     */
    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = socket.getOutputStream();
        } catch (IOException ex) {
            log.error("Exception occured establishing connection: ", ex);
            return;
        }

        try {
            sendResponse(parseRequest());
        } catch (IOException ex) {
            log.error("Exception occured while processing request: ", ex);
        }

        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException ex) {
            log.error("Exception occured closing connection: ", ex);
        }
    }

    /**
     * Parses request and generates a Request object.
     *
     * @return the Request.
     * @throws IOException if error is thrown while parsing the request.
     */
    private Request parseRequest() throws IOException {
        String headerLine;
        HashMap<String, String> headers = new HashMap<String, String>();
        String initialLine = input.readLine();
        String[] requestTokens = initialLine.split("\\s+");
        logActivity.info(socket.getInetAddress() + " - " + initialLine);
        while ((headerLine = input.readLine()) != null) {
            if (!headerLine.isEmpty()) {
                String[] headerTokens = headerLine.split(":\\s+");
                String key = headerTokens[0];
                String value = headerTokens[1];
                headers.put(key, value);
            } else {
                break;
            }
        }
        Request request = new Request(requestTokens, headers);
        return request;
    }


    /**
     * Sends http response for supported methods.
     * Only supports GET and HEAD.
     *
     * @param request the http request.
     * @throws IOException if error is thrown while processing the request
     */
    private void sendResponse(Request request) throws IOException {
        Response response = new Response(request, output);
        log.debug("Processing Request: " + request.toString());
        String filePath = preparePath(request.getPath());
        File file = new File(webroot + File.separator + filePath);
        if (request.getMethod().equalsIgnoreCase("HEAD")) {
            if (file.exists()) {
                response.sendHeader(ResponseCode.OK.getMessage(), file);
                log.debug("HEAD Request Processed: " + ResponseCode.OK.getMessage());
            } else {
                file = new File(webroot + File.separator + notFoundPage);
                response.sendHeader(ResponseCode.NOT_FOUND.getMessage(), file);
                log.debug("HEAD Request Processed: " + ResponseCode.NOT_FOUND.getMessage());
            }
        } else if (request.getMethod().equalsIgnoreCase("GET")) {
            if (file.exists()) {
                response.sendHeader(ResponseCode.OK.getMessage(), file);
                response.sendBody(file);
                log.debug("GET Request Processed: " + ResponseCode.OK.getMessage());
            } else {
                file = new File(webroot + File.separator + notFoundPage);
                response.sendHeader(ResponseCode.NOT_FOUND.getMessage(), file);
                response.sendBody(file);
                log.debug("GET Request Processed: " + ResponseCode.NOT_FOUND.getMessage());
            }
        } else {
            response.sendHeader(ResponseCode.NOT_IMPLEMENTED.getMessage(), null);
            log.debug("Request Processed: " + ResponseCode.NOT_IMPLEMENTED.getMessage());
        }
    }

    /**
     * Scrub the incoming path.
     * Sets path to index page if webroot requested.
     * Sets path to index page if request is null or contains a directory traversal attempt.
     *
     * @param path requested path.
     * @return scrubbed path.
     */
    private String preparePath(String path) {
        log.debug("Original Path: " + path);
        if (path.equals("/") || path == null || path.contains("..")) {
            path = indexPage;
        }
        path.replace('/', File.separator.charAt(0));
        log.debug("Prepared Path: " + path);
        return path;
    }

}