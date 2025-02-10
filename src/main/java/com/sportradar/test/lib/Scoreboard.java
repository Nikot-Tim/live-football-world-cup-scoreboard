package com.sportradar.test.lib;

import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import java.util.List;

/**
 * Interface for a live football scoreboard.
 * Provides methods for managing football matches, updating scores, and retrieving summaries.
 */
public interface Scoreboard {
    /**
     * Starts a new match with an initial score of 0-0.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @throws IllegalArgumentException if the match data is not correct.
     * @throws MatchAlreadyExistsException if the match already exists.
     */
    void startMatch(String homeTeam, String awayTeam);

    /**
     * Updates the score of an existing match.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @param homeScore Updated home team score.
     * @param awayScore Updated away team score.
     * @throws IllegalArgumentException if the match data is not correct.
     * @throws MatchNotFoundException if the match does not exist.
     */
    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

    /**
     * Finishes match and removes it from the scoreboard.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @throws IllegalArgumentException if the match data is not correct.
     * @throws MatchNotFoundException if the match does not exist.
     */
    void finishMatch(String homeTeam, String awayTeam);

    /**
     * Retrieves a summary of matches sorted by total score and start time.
     * @return A new list containing the sorted matches.
     */
    List<FootballMatch> getSummary();
}
