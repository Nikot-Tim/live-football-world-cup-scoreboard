package com.sportradar.test.lib;

import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import com.sportradar.test.lib.validation.Validator;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe implementation of a Live Football World Cup Scoreboard.
 * This class allows adding, updating, and removing matches while ensuring
 * safe concurrent access using ConcurrentHashMap and synchronized sorting.
 */
public class FootballWorldCupScoreboard implements Scoreboard {
    private static final String MATCH_KEY_SEPARATOR = " vs ";

    // Thread-safe data structure for match storage
    private final Map<String, FootballMatch> matches = new ConcurrentHashMap<>();

    // Stores the sorted list of matches, updated after each modification
    private List<FootballMatch> sortedMatches = new ArrayList<>();

    // Validator for edge cases
    private final Validator validator = new Validator();

    // Clock instance for
    private final Clock clock;

    /**
     * Constructor to initialize the Scoreboard with a Clock instance.
     * @param clock Clock instance used to record match start time.
     */
    public FootballWorldCupScoreboard(Clock clock) {
        this.clock = clock;
    }

    /**
     * Starts a new match with an initial score of 0-0.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @throws IllegalArgumentException if the match data is not correct.
     * @throws MatchAlreadyExistsException if the match already exists.
     */
    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        String matchKey = validateTeamsAndGenerateMatchKey(homeTeam, awayTeam);
        validator.validateIfMatchAlreadyExists(matchKey, matches);
        matches.put(matchKey, FootballMatch.withScores(homeTeam, awayTeam,0, 0, clock));
        updateSortedMatches();
    }

    /**
     * Retrieves a summary of matches sorted by total score and start time.
     * @return A new list containing the sorted matches.
     */
    @Override
    public List<FootballMatch> getSummary() {
        // Ensures thread safety by returning a copy
        return new ArrayList<>(sortedMatches);
    }

    /**
     * Updates the score of an existing match.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @param homeScore Updated home team score.
     * @param awayScore Updated away team score.
     * @throws IllegalArgumentException if the match data is not correct.
     * @throws MatchNotFoundException if the match does not exist.
     */
    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validator.validateScores(homeScore, awayScore);
        String matchKey = validateTeamsAndGenerateMatchKey(homeTeam, awayTeam);
        validator.validateIfMatchCanBeFound(matchKey, matches);
        matches.put(matchKey, FootballMatch.withScores(homeTeam, awayTeam, homeScore, awayScore, clock));
        updateSortedMatches();
    }

    /**
     * Finishes match and removes it from the scoreboard.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @throws IllegalArgumentException if the match data is not correct.
     * @throws MatchNotFoundException if the match does not exist.
     */
    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        String matchKey = validateTeamsAndGenerateMatchKey(homeTeam, awayTeam);
        validator.validateIfMatchCanBeFound(matchKey, matches);
        matches.remove(matchKey);
        updateSortedMatches();
    }

    /**
     * Validates team names and generates a unique key for a match.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @return A unique match key.
     */
    private String validateTeamsAndGenerateMatchKey(String homeTeam, String awayTeam) {
        validator.validateTeams(homeTeam, awayTeam);
        return homeTeam + MATCH_KEY_SEPARATOR + awayTeam;
    }

    /**
     * Thread-safe updates a sorted list of matches based on total score and start time.
     * This method ensures that the matches remain correctly ordered.
     */
    private synchronized void updateSortedMatches() {
        sortedMatches = matches.values().stream()
                .sorted(Comparator
                        .comparingInt(FootballMatch::getTotalScore).reversed()
                        .thenComparing(FootballMatch::startTime, Comparator.reverseOrder())
                )
                .toList();
    }
}
