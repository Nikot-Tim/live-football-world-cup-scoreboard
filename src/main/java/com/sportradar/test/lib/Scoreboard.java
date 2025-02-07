package com.sportradar.test.lib;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {
    private final List<FootballMatch> matches = new ArrayList<>();

    public void startMatch(String homeTeam, String awayTeam) {
        matches.add(new FootballMatch(homeTeam, awayTeam, new MatchScores(0, 0)));
    }

    public List<FootballMatch> getSummary() {
        return matches;
    }
}
