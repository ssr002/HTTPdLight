package com.gmail.lifeofreilly.httpdlight;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Defines the http request and supporting methods.
 *
 * @author Seth Reilly
 * @version 1.0, April 2013
 */
class Request {
    private String[] tokens;
    private HashMap<String, String> headers;

    /**
     * Sole constructor.
     *
     * @param tokens  http request line tokens
     * @param headers a hashmap of the http header fields.
     */
    public Request(String[] tokens, HashMap<String, String> headers) {
        this.tokens = tokens;
        this.headers = headers;
    }

    /**
     * Get the http method.
     *
     * @return the http method
     */
    public String getMethod() {
        return tokens[0];
    }

    /**
     * Get the requested file path.
     *
     * @return the requested file path.
     */
    public String getPath() {
        return tokens[1];
    }

    /**
     * Converts http request to string for output to logs.
     *
     * @return http request converted to string.
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(Arrays.toString(tokens));
        string.append(headers.toString());
        return string.toString();
    }
}