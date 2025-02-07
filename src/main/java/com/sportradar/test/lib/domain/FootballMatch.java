package com.sportradar.test.lib.domain;

public record FootballMatch(String homeTeam, String awayTeam, MatchScores scores) {
    public int totalScore() {
        return scores.homeTeamScore() + scores.awayTeamScore();
    }
}
