package com.sportradar.test.lib.validation;

import com.sportradar.test.lib.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import java.util.Map;

public class Validator {

    public void validateMatchExists(String matchKey, Map<String, FootballMatch> matches) {
        if (!matches.containsKey(matchKey)) {
            throw new MatchNotFoundException(matchKey);
        }
    }

    public void validateIfMatchAlreadyExists(String matchKey, Map<String, FootballMatch> matches) {
        if (matches.containsKey(matchKey)) {
            throw new MatchAlreadyExistsException(matchKey);
        }
    }

    public void validateScores(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }
    }

    public void validateTeams(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.isBlank() || awayTeam.isBlank()) {
            throw new IllegalArgumentException("Team names must not be null or empty.");
        }
    }

}
