import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.AWAY_TEAM_TEST_NAME;
import static utils.TestUtils.HOME_TEAM_TEST_NAME;
import static utils.TestUtils.TEST_MATCH_KEY;
import static utils.TestUtils.assertInvalidTeamNames;
import static utils.TestUtils.assertThrowsWithMessage;
import com.sportradar.test.lib.Scoreboard;
import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import com.sportradar.test.lib.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class ScoreboardTest {
    private Scoreboard scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    void shouldStartNewMatchWithInitialScore() {
        scoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);

        List<FootballMatch> matches = scoreboard.getSummary();

        assertEquals(1, matches.size());
        assertMatch(matches.get(0), HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 0);
    }

    @Test
    void shouldNotAllowDuplicateMatches() {
        scoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);

        assertThrowsWithMessage(
                MatchAlreadyExistsException.class,
                () -> scoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME),
                MatchAlreadyExistsException.MESSAGE_PREFIX + TEST_MATCH_KEY
        );
    }

    @Test
    void shouldUpdateScoreForExistingMatch() {
        scoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);
        scoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 5);

        FootballMatch match = scoreboard.getSummary().get(0);
        assertMatch(match, HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 5);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingMatch() {
        assertThrowsWithMessage(
                MatchNotFoundException.class,
                () -> scoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 5),
                MatchNotFoundException.MESSAGE_PREFIX + TEST_MATCH_KEY
        );
    }

    @Test
    void shouldThrowExceptionForNegativeScores() {
        scoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, -1, 0),
                Validator.NEGATIVE_SCORES_ERROR
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, -5),
                Validator.NEGATIVE_SCORES_ERROR
        );
    }

    @Test
    void shouldFinishExistingMatch() {
        scoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);
        scoreboard.finishMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);

        assertEquals(0, scoreboard.getSummary().size());
    }

    @Test
    void shouldThrowExceptionWhenFinishingNonExistingMatch() {
        assertThrowsWithMessage(
                MatchNotFoundException.class,
                () -> scoreboard.finishMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME),
                MatchNotFoundException.MESSAGE_PREFIX + TEST_MATCH_KEY
        );
    }

    @Test
    void shouldReturnMatchesSortedByTotalScoreAndStartTime() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");

        scoreboard.updateScore("Mexico", "Canada", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        scoreboard.updateScore("Germany", "France", 2, 2);
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        List<FootballMatch> matches = scoreboard.getSummary();

        assertMatchOrder(matches, "Uruguay", "Spain", "Mexico", "Argentina", "Germany");
    }

    @Test
    void shouldThrowExceptionWhenStartingMatchWithEmptyOrNullTeamNames() {
        assertInvalidTeamNames(() -> scoreboard.startMatch(null, AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> scoreboard.startMatch(HOME_TEAM_TEST_NAME, null));
        assertInvalidTeamNames(() -> scoreboard.startMatch("", AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> scoreboard.startMatch(HOME_TEAM_TEST_NAME, " "));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMatchWithEmptyOrNullTeamNames() {
        assertInvalidTeamNames(() -> scoreboard.updateScore(null, AWAY_TEAM_TEST_NAME, 0, 5));
        assertInvalidTeamNames(() -> scoreboard.updateScore(HOME_TEAM_TEST_NAME, null, 0, 5));
        assertInvalidTeamNames(() -> scoreboard.updateScore("", AWAY_TEAM_TEST_NAME, 0, 5));
        assertInvalidTeamNames(() -> scoreboard.updateScore(HOME_TEAM_TEST_NAME, " ", 0, 5));
    }

    @Test
    void shouldThrowExceptionWhenFinishingMatchWithEmptyOrNullTeamNames() {
        assertInvalidTeamNames(() -> scoreboard.finishMatch(null, AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> scoreboard.finishMatch(HOME_TEAM_TEST_NAME, null));
        assertInvalidTeamNames(() -> scoreboard.finishMatch("", AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> scoreboard.finishMatch(HOME_TEAM_TEST_NAME, " "));
    }

    private void assertMatch(FootballMatch match, String homeTeam, String awayTeam, int homeScore, int awayScore) {
        assertEquals(homeTeam, match.homeTeam());
        assertEquals(awayTeam, match.awayTeam());
        assertEquals(homeScore, match.getHomeScore());
        assertEquals(awayScore, match.getAwayScore());
    }

    private void assertMatchOrder(List<FootballMatch> matches, String... expectedOrder) {
        assertEquals(expectedOrder.length, matches.size());
        for (int i = 0; i < expectedOrder.length; i++) {
            assertEquals(expectedOrder[i], matches.get(i).homeTeam());
        }
    }
}
