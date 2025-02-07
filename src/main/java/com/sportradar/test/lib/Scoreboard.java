package com.sportradar.test.lib;

import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.domain.MatchScores;
import com.sportradar.test.lib.validation.Validator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard {
    private static final String MATCH_KEY_SEPARATOR = " vs ";

    private final Map<String, FootballMatch> matches = new LinkedHashMap<>();
    private List<FootballMatch> sortedMatches = new ArrayList<>();
    private final Validator validator = new Validator();

    public void startMatch(String homeTeam, String awayTeam) {
        validator.validateTeams(homeTeam, awayTeam);
        String matchKey = generateMatchKey(homeTeam, awayTeam);
        validator.validateIfMatchAlreadyExists(matchKey, matches);
        matches.put(matchKey, new FootballMatch(homeTeam, awayTeam, new MatchScores(0, 0)));
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
        matches.put(matchKey, new FootballMatch(homeTeam, awayTeam, new MatchScores(homeScore, awayScore)));
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
                        .comparingInt(FootballMatch::totalScore).reversed()
                        .thenComparing(match -> new ArrayList<>(matches.values()).indexOf(match), Comparator.reverseOrder())
                )
                .toList();
    }
}
