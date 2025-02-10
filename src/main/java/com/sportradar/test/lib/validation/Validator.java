package com.sportradar.test.lib.validation;

import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import java.util.Map;

/**
 * Utility class for validating match-related data.
 * Ensures that match existence, team names, and scores meet the required conditions.
 */
public class Validator {

    /** Error message for negative scores. */
    public static final String NEGATIVE_SCORES_ERROR = "Scores cannot be negative.";

    /** Error message for invalid team names. */
    public static final String INVALID_TEAM_NAME_ERROR = "Team names must not be null or empty.";

    /**
     * Validates whether a match exists in the scoreboard.
     * @param matchKey Unique match key.
     * @param matches Map of all ongoing matches.
     * @throws MatchNotFoundException if the match is not found.
     */
    public void validateIfMatchCanBeFound(String matchKey, Map<String, FootballMatch> matches) {
        if (!matches.containsKey(matchKey)) {
            throw new MatchNotFoundException(matchKey);
        }
    }

    /**
     * Ensures that a match does not already exist before adding it.
     * @param matchKey Unique match key.
     * @param matches Map of all ongoing matches.
     * @throws MatchAlreadyExistsException if the match already exists.
     */
    public void validateIfMatchAlreadyExists(String matchKey, Map<String, FootballMatch> matches) {
        if (matches.containsKey(matchKey)) {
            throw new MatchAlreadyExistsException(matchKey);
        }
    }

    /**
     * Ensures that match scores are valid (non-negative).
     * @param homeScore Home team's score.
     * @param awayScore Away team's score.
     * @throws IllegalArgumentException if any score is negative.
     */
    public void validateScores(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException(NEGATIVE_SCORES_ERROR);
        }
    }

    /**
     * Validates that team names are valid and non-empty.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @throws IllegalArgumentException if any team name is null or empty.
     */
    public void validateTeams(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.isBlank() || awayTeam.isBlank()) {
            throw new IllegalArgumentException(INVALID_TEAM_NAME_ERROR);
        }
    }
}
