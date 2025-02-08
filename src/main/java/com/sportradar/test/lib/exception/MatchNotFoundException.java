package com.sportradar.test.lib.exception;

public class MatchNotFoundException extends MatchException {
    public static final String MESSAGE_PREFIX = "Match not found: ";

    public MatchNotFoundException(String matchKey) {
        super(MESSAGE_PREFIX + matchKey);
    }
}
