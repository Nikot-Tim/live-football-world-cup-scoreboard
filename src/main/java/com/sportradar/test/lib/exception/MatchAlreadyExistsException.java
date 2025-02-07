package com.sportradar.test.lib.exception;

public class MatchAlreadyExistsException extends MatchException {
    public MatchAlreadyExistsException(String matchKey) {
        super("Match already exists: " + matchKey);
    }
}
