package com.telecom.rr;

/**
 *
 */
public class FrameIllegalPrivilegeException extends RuntimeException {

    private static final long serialVersionUID = -4891399880671541945L;

    public FrameIllegalPrivilegeException(String message) {
        super(message);
    }

    public FrameIllegalPrivilegeException(Throwable cause) {
        super(cause);
    }
}