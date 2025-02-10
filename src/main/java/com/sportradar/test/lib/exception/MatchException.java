package com.sportradar.test.lib.exception;

/**
 * Base exception for match-related errors in the scoreboard.
 * This abstract class provides a foundation for specific match-related exceptions.
 */
public abstract class MatchException extends RuntimeException {

    /**
     * Constructs a new MatchException with the specified message.
     * @param message The detail message describing the exception.
     */
    public MatchException(String message) {
        super(message);
    }
}
