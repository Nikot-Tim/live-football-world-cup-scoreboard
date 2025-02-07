package com.sportradar.test.lib.exception;

public abstract class MatchException extends RuntimeException {
    public MatchException(String message) {
        super(message);
    }
}
