package com.sportradar.test.lib;

import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.validation.Validator;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard {
    private static final String MATCH_KEY_SEPARATOR = " vs ";

    private final Map<String, FootballMatch> matches = new HashMap<>();
    private List<FootballMatch> sortedMatches = new ArrayList<>();
    private final Validator validator = new Validator();
    private final Clock clock;

    public Scoreboard(Clock clock) {
        this.clock = clock;
    }

    public void startMatch(String homeTeam, String awayTeam) {
        validator.validateTeams(homeTeam, awayTeam);
        String matchKey = generateMatchKey(homeTeam, awayTeam);
        validator.validateIfMatchAlreadyExists(matchKey, matches);
        matches.put(matchKey, FootballMatch.withScores(homeTeam, awayTeam,0, 0, clock));
        updateSortedMatches();
    }

    public List<FootballMatch> getSummary() {
        return sortedMatches;
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validator.validateTeams(homeTeam, awayTeam);
        validator.validateScores(homeScore, awayScore);
        String matchKey = generateMatchKey(homeTeam, awayTeam);
        validator.validateMatchExists(matchKey, matches);
        matches.put(matchKey, FootballMatch.withScores(homeTeam, awayTeam, homeScore, awayScore, clock));
        updateSortedMatches();
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        validator.validateTeams(homeTeam, awayTeam);
        String matchKey = generateMatchKey(homeTeam, awayTeam);
        validator.validateMatchExists(matchKey, matches);
        matches.remove(matchKey);
        updateSortedMatches();
    }

    private String generateMatchKey(String homeTeam, String awayTeam) {
        return homeTeam + MATCH_KEY_SEPARATOR + awayTeam;
    }

    private void updateSortedMatches() {
        sortedMatches = matches.values().stream()
                .sorted(Comparator
                        .comparingInt(FootballMatch::getTotalScore).reversed()
                        .thenComparing(FootballMatch::startTime, Comparator.reverseOrder())
                )
                .toList();
    }
}
