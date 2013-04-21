package com.gmail.lifeofreilly.httpdlight;

import org.apache.log4j.Logger;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Defines the HTTPdLight server and supporting methods.
 *
 * @author Seth Reilly
 * @version 1.0, April 2013
 */
public class HTTPdLight implements Runnable {
    private int serverPort;
    private final static String webroot = "webroot";
    private final static String indexPage = "index.html";
    private final static String notFoundPage = "404.html";
    private final static Logger log = Logger.getLogger(HTTPdLight.class);

    /**
     * Sole constructor.
     *
     * @param p port with a range of 1 to 65535.
     */
    public HTTPdLight(int p) {
        serverPort = p;
    }

    /**
     * Starts a new HTTPdLight server using supplied port.
     * Initializes default content if it doesn't exist.
     * Opens server uri in default web browser if desktop support is available.
     *
     * @param args requires a single argument [port], which must be an integer from 1 to 65535.
     */
    public static void main(String[] args) {
        if (isValidPort(args[0])) {
            Integer port = Integer.parseInt(args[0]);
            HTTPdLight httpd = new HTTPdLight(port);
            initializeDefaultContent();
            new Thread(httpd).start();
            System.out.println("See Logs: " + System.getProperty("user.dir") + "/logs/");
            System.out.println("Press CTRL-C to shutdown...");
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI("http://localhost:" + port));
                } else {
                    log.warn("Desktop.isDesktopSupported() returned false. Browser will not be launched.");
                }
            } catch (URISyntaxException ex) {
                log.error("Exception occured while generating root uri: ", ex);
            } catch (IOException ex) {
                log.error("Exception occured while launching default browser: ", ex);
            }
        } else {
            System.out.println("Usage: hHTTPdLight [port]");
            System.out.println("Please provide a valid port");
            System.exit(0);
        }
    }

    /**
     * Starts listening for incoming requests using a cached thread pool.
     */
    @Override
    public void run() {
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            log.info("HTTPdLight is accepting requests on port: " + serverPort);
            log.info("webroot: " + System.getProperty("user.dir") + File.separator + webroot);
            while (true) {
                Socket socket = serverSocket.accept();
                pool.submit(new Connection(socket, webroot, indexPage, notFoundPage));
            }
        } catch (IOException ex) {
            log.fatal("Exception occured while establishing server socket on port " + serverPort + ", please select an available port: ", ex);
        }
        pool.shutdown();
    }

    /**
     * Verifies that a string represents a valid port (integer from 1 to 65535).
     *
     * @param p the string to verify.
     * @return true if the string represents a valid port.
     */
    public static boolean isValidPort(String p) {
        try {
            int port = Integer.parseInt(p);
            if (port >= 1 && port <= 65535) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Initializes default webroot directory if it doesn't exist.
     * Initializes default index page if it doesn't exist.
     * Initializes default 404 error page if it doesn't exist.
     * The webroot directory will be created in the current working directory.
     */
    public static void initializeDefaultContent() {
        File root = new File(webroot);
        if (!root.exists()) {
            root.mkdir();
            log.info("Initialized default webroot directory: " + root.getAbsolutePath());
        }
        initializeFile(webroot + File.separator + indexPage, "<!DOCTYPE html><html><head><title>HTTPdLight</title></head><body>Welcome to HTTPdLight</body></html>");
        initializeFile(webroot + File.separator + notFoundPage, "<!DOCTYPE html><html><head><title>404</title></head><body>404 - Page not found</body></html>");
    }

    /**
     * Initializes file if it doesn't exist.
     *
     * @param path    the file path.
     * @param content the file content.
     */
    private static void initializeFile(String path, String content) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (content != null) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(content);
                    writer.close();
                } else {
                    file.createNewFile();
                }
                log.info("Initialized default content: " + path);
            } catch (IOException ex) {
                log.error("Exception occured while initializing default file: " + path, ex);
            }
        }
    }
}