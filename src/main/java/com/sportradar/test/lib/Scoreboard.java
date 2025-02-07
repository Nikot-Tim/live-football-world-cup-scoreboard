package com.sportradar.test.lib;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {
    private final List<FootballMatch> matches = new ArrayList<>();

    public void startMatch(String homeTeam, String awayTeam) {
        String matchKey = homeTeam + " vs " + awayTeam;

        if (matches.stream().anyMatch(match ->
                (match.homeTeam().equalsIgnoreCase(homeTeam) && match.awayTeam().equalsIgnoreCase(awayTeam)) ||
                        (match.homeTeam().equalsIgnoreCase(awayTeam) && match.awayTeam().equalsIgnoreCase(homeTeam))
        )) {
            throw new IllegalArgumentException("Match already exists: " + matchKey);
        }

        matches.add(new FootballMatch(homeTeam, awayTeam, new MatchScores(0, 0)));
    }

    public List<FootballMatch> getSummary() {
        return matches;
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }
        matches.stream()
                .filter(match -> match.homeTeam().equalsIgnoreCase(homeTeam) && match.awayTeam().equalsIgnoreCase(awayTeam))
                .findFirst()
                .ifPresentOrElse(
                        match -> {
                            int index = matches.indexOf(match);
                            matches.set(index, new FootballMatch(homeTeam, awayTeam, new MatchScores(homeScore, awayScore)));
                        },
                        () -> {
                            throw new IllegalArgumentException("Match not found: " + homeTeam + " vs " + awayTeam);
                        }
                );
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        matches.removeIf(match ->
                match.homeTeam().equalsIgnoreCase(homeTeam) &&
                        match.awayTeam().equalsIgnoreCase(awayTeam)
        );
    }
}
