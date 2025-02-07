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
}
