package com.gmail.lifeofreilly.httpdlight;

/**
 * Defines an enum of HTTP reponse codes used by HTTPdLight.
 * This is not a comprehensive list of HTTP reponse codes.
 *
 * @author  Seth Reilly
 * @version 1.0, April 2013
 */
enum ResponseCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    NOT_IMPLEMENTED(501, "Not Implemented");

    private final int code;
    private final String reason;

    /**
     * Sole constructor.
     *
     * @param code http status code
     * @param reason http status reason
     */
    private ResponseCode(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    /**
     * Gets http code.
     *
     * @return the http code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets http reason.
     *
     * @return the http reason.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Gets full http message. cdoe + reason
     *
     * @return http status message.
     */
    public String getMessage() {
        return code + " " + reason;
    }
}