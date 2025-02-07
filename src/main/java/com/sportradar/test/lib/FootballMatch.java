package com.sportradar.test.lib;

public record FootballMatch(String homeTeam, String awayTeam, MatchScores scores) {
    int totalScore() {
        return scores.homeTeamScore() + scores.awayTeamScore();
    }
}
