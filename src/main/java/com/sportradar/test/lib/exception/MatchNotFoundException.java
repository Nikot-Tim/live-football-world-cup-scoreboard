package com.sportradar.test.lib.exception;

/**
 * Exception thrown when attempting to access a match that does not exist.
 * This ensures that operations such as updating or removing a match
 * are only performed on existing matches.
 */
public class MatchNotFoundException extends MatchException {

    /** Prefix for the exception message. */
    public static final String MESSAGE_PREFIX = "Match not found: ";

    /**
     * Constructs a new MatchNotFoundException with a message containing the match key.
     * @param matchKey The unique identifier of the match that was not found.
     */
    public MatchNotFoundException(String matchKey) {
        super(MESSAGE_PREFIX + matchKey);
    }
}
