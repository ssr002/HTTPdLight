package com.gmail.lifeofreilly.httpdlight;

import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Defines the http response and supporting methods.
 *
 * @author Seth Reilly
 * @version 1.0, April 2013
 */
class Response {
    private OutputStream output;
    private PrintStream pStream;
    private Request request;

    /**
     * Sole constructor.
     *
     * @param r the incoming http request.
     * @param o the output stream for response.
     */
    public Response(Request r, OutputStream o) {
        request = r;
        output = o;
        pStream = new PrintStream(new BufferedOutputStream(output), true);
    }

    /**
     * Send http header.
     *
     * @param status the respnse code and message.
     * @param file   the file requested.
     */
    public void sendHeader(String status, File file) {
        long length = 0;
        String contentType = null;
        SimpleDateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        StringBuilder headers = new StringBuilder();
        String CRLF = new String("\r\n");
        if (file != null) {
            if (file.exists()) {
                length = file.length();
                contentType = getContentType(file);
            }
        }
        headers.append("HTTP/1.1 ").append(status).append(CRLF);
        headers.append("Date: ").append(httpDateFormat.format(new Date())).append(CRLF);
        headers.append("Server: HTTPdLight").append(CRLF);
        headers.append("Allow: GET, HEAD").append(CRLF);
        headers.append("Connection: close").append(CRLF);
        headers.append("Content-Length: ").append(length).append(CRLF);
        if (contentType != null) {
            headers.append("Content-Type: ").append(contentType).append(CRLF);
        }
        pStream.println(headers);
    }

    /**
     * Send http body.
     *
     * @param name description.
     * @param file the file requested.
     * @throws IOException if InputStream read throws an exception.
     */
    public void sendBody(File file) throws IOException {
        InputStream fStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fStream.read(buffer)) != -1) {
            pStream.write(buffer, 0, bytesRead);
        }
        fStream.close();
    }

    /**
     * Get the content type of requested file.
     *
     * @param file the file requested.
     * @return content type.
     */
    public String getContentType(File file) {
        String fileName = file.getName();
        String contentType;
        if (fileName.endsWith(".htm") || fileName.endsWith(".html"))
            contentType = "text/html";
        else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
            contentType = "image/jpeg";
        else if (fileName.endsWith(".png"))
            contentType = "image/png";
        else if (fileName.endsWith(".gif"))
            contentType = "image/gif";
        else if (fileName.endsWith(".pdf"))
            contentType = "application/pdf";
        else if (fileName.endsWith(".ico"))
            contentType = "image/x-icon";
        else
            contentType = "text/plain";
        return contentType;
    }
}