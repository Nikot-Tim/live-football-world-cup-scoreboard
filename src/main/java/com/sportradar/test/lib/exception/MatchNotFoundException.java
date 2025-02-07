package com.sportradar.test.lib.exception;

public class MatchNotFoundException extends MatchException {
    public MatchNotFoundException(String matchKey) {
        super("Match not found: " + matchKey);
    }
}
