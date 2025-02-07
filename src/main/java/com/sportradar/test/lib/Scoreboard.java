package com.sportradar.test.lib;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard {
    private static final String MATCH_KEY_SEPARATOR = " vs ";

    private final Map<String, FootballMatch> matches = new LinkedHashMap<>();

    public void startMatch(String homeTeam, String awayTeam) {
        String matchKey = generateMatchKey(homeTeam, awayTeam);
        if (matches.containsKey(matchKey)) {
            throw new IllegalArgumentException("Match already exists: " + matchKey);
        }
        matches.put(matchKey, new FootballMatch(homeTeam, awayTeam, new MatchScores(0, 0)));
    }

    public List<FootballMatch> getSummary() {
        return matches.values().stream()
                .sorted(Comparator
                        .comparingInt(FootballMatch::totalScore)
                        .reversed()
                        .thenComparing(match -> new ArrayList<>(matches.values()).indexOf(match), Comparator.reverseOrder())
                )
                .toList();
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }

        String matchKey = generateMatchKey(homeTeam, awayTeam);
        if (!matches.containsKey(matchKey)) {
            throw new IllegalArgumentException("Match not found: " + matchKey);
        }

        matches.put(matchKey, new FootballMatch(homeTeam, awayTeam, new MatchScores(homeScore, awayScore)));
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        String matchKey = generateMatchKey(homeTeam, awayTeam);
        if (matches.remove(matchKey) == null) {
            throw new IllegalArgumentException("Match not found: " + matchKey);
        }
    }

    private String generateMatchKey(String homeTeam, String awayTeam) {
        return homeTeam + MATCH_KEY_SEPARATOR + awayTeam;
    }
}
