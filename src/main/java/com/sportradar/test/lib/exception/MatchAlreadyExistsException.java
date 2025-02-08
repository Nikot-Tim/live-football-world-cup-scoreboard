package com.sportradar.test.lib.exception;

public class MatchAlreadyExistsException extends MatchException {
    public static final String MESSAGE_PREFIX = "Match already exists: ";

    public MatchAlreadyExistsException(String matchKey) {
        super(MESSAGE_PREFIX + matchKey);
    }
}
