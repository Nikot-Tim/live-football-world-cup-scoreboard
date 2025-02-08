package com.sportradar.test.lib.validation;

import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import java.util.Map;

public class Validator {

    public static final String NEGATIVE_SCORES_ERROR = "Scores cannot be negative.";
    public static final String INVALID_TEAM_NAME_ERROR = "Team names must not be null or empty.";

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
            throw new IllegalArgumentException(NEGATIVE_SCORES_ERROR);
        }
    }

    public void validateTeams(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.isBlank() || awayTeam.isBlank()) {
            throw new IllegalArgumentException(INVALID_TEAM_NAME_ERROR);
        }
    }
}
