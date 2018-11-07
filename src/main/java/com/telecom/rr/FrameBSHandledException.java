package com.telecom.rr;

/**
 *
 */
public class FrameBSHandledException extends RuntimeException {

    private static final long serialVersionUID = -4891399880671541945L;

    public FrameBSHandledException(String message) {
        super(message);
    }

    public FrameBSHandledException(Throwable cause) {
        super(cause);
    }
}
