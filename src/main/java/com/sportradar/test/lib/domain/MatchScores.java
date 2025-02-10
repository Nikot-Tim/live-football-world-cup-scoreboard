package com.sportradar.test.lib.domain;

/**
 * Immutable record representing the scores of a football match.
 * Stores individual scores for both home and away teams.
 *
 * @param homeTeamScore The score of the home team.
 * @param awayTeamScore The score of the away team.
 */
public record MatchScores(int homeTeamScore, int awayTeamScore) {
}
