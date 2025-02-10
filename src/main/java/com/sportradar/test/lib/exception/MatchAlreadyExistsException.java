package com.sportradar.test.lib.exception;

/**
 * Exception thrown when attempting to start a match that already exists.
 * This ensures that duplicate matches are not added to the scoreboard.
 */
public class MatchAlreadyExistsException extends MatchException {

    /** Prefix for the exception message. */
    public static final String MESSAGE_PREFIX = "Match already exists: ";

    /**
     * Constructs a new MatchAlreadyExistsException with a message containing the match key.
     * @param matchKey The unique identifier of the match that already exists.
     */
    public MatchAlreadyExistsException(String matchKey) {
        super(MESSAGE_PREFIX + matchKey);
    }
}
