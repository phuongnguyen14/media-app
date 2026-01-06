package com.mediaapp.exception;

/**
 * Unauthorized Exception
 * Thrown when user doesn't have permission for an action
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String action, String resource) {
        super(String.format("Not authorized to %s %s", action, resource));
    }
}
