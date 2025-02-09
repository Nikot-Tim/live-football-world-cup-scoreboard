package com.sportradar.test.lib.domain;

import java.time.Clock;
import java.time.Instant;

public record FootballMatch(String homeTeam, String awayTeam, MatchScores scores, Instant startTime) {
    public int getTotalScore() {
        return scores.homeTeamScore() + scores.awayTeamScore();
    }

    public int getHomeScore() {
        return scores.homeTeamScore();
    }

    public int getAwayScore() {
        return scores.awayTeamScore();
    }

    public static FootballMatch withScores(String homeTeam, String awayTeam, int homeScore, int awayScore, Clock clock) {
        return new FootballMatch(homeTeam, awayTeam, new MatchScores(homeScore, awayScore), Instant.now(clock));
    }
}
