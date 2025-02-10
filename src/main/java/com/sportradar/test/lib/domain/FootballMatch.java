package com.sportradar.test.lib.domain;

import java.time.Clock;
import java.time.Instant;

/**
 * Immutable representation of a football match.
 * Stores match details, scores, and the match start time.
 */
public record FootballMatch(String homeTeam, String awayTeam, MatchScores scores, Instant startTime) {

    /**
     * Calculates the total score of the match.
     * @return The sum of the home and away team scores.
     */
    public int getTotalScore() {
        return scores.homeTeamScore() + scores.awayTeamScore();
    }

    /**
     * Retrieves the score of the home team.
     * @return The home team's score.
     */
    public int getHomeScore() {
        return scores.homeTeamScore();
    }

    /**
     * Retrieves the score of the away team.
     * @return The away team's score.
     */
    public int getAwayScore() {
        return scores.awayTeamScore();
    }

    /**
     * Method for creating a new FootballMatch instance with specified scores.
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @param homeScore Score of the home team.
     * @param awayScore Score of the away team.
     * @param clock Clock instance for timestamping match start.
     * @return A new FootballMatch instance with the given details.
     */
    public static FootballMatch withScores(String homeTeam, String awayTeam, int homeScore, int awayScore, Clock clock) {
        return new FootballMatch(homeTeam, awayTeam, new MatchScores(homeScore, awayScore), Instant.now(clock));
    }
}
