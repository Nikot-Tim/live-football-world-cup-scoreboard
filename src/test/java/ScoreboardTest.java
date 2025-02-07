import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.assertInvalidTeamNames;
import static utils.TestUtils.assertThrowsWithMessage;
import com.sportradar.test.lib.Scoreboard;
import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
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
        scoreboard.startMatch("Mexico", "Canada");

        List<FootballMatch> matches = scoreboard.getSummary();

        assertEquals(1, matches.size());
        assertMatch(matches.get(0), "Mexico", "Canada", 0, 0);
    }

    @Test
    void shouldNotAllowDuplicateMatches() {
        scoreboard.startMatch("Mexico", "Canada");

        assertThrowsWithMessage(
                MatchAlreadyExistsException.class,
                () -> scoreboard.startMatch("Mexico", "Canada"),
                "Match already exists: Mexico vs Canada"
        );
    }

    @Test
    void shouldUpdateScoreForExistingMatch() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);

        FootballMatch match = scoreboard.getSummary().get(0);
        assertMatch(match, "Mexico", "Canada", 0, 5);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingMatch() {
        assertThrowsWithMessage(
                MatchNotFoundException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", 0, 5),
                "Match not found: Mexico vs Canada"
        );
    }

    @Test
    void shouldThrowExceptionForNegativeScores() {
        scoreboard.startMatch("Mexico", "Canada");

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", -1, 0),
                "Scores cannot be negative."
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", 0, -5),
                "Scores cannot be negative."
        );
    }

    @Test
    void shouldFinishExistingMatch() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.finishMatch("Mexico", "Canada");

        assertEquals(0, scoreboard.getSummary().size());
    }

    @Test
    void shouldThrowExceptionWhenFinishingNonExistingMatch() {
        assertThrowsWithMessage(
                MatchNotFoundException.class,
                () -> scoreboard.finishMatch("Mexico", "Canada"),
                "Match not found: Mexico vs Canada"
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
        assertInvalidTeamNames(() -> scoreboard.startMatch(null, "Canada"));
        assertInvalidTeamNames(() -> scoreboard.startMatch("Mexico", null));
        assertInvalidTeamNames(() -> scoreboard.startMatch("", "Canada"));
        assertInvalidTeamNames(() -> scoreboard.startMatch("Mexico", " "));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMatchWithEmptyOrNullTeamNames() {
        assertInvalidTeamNames(() -> scoreboard.updateScore(null, "Canada", 0, 5));
        assertInvalidTeamNames(() -> scoreboard.updateScore("Mexico", null, 0, 5));
        assertInvalidTeamNames(() -> scoreboard.updateScore("", "Canada", 0, 5));
        assertInvalidTeamNames(() -> scoreboard.updateScore("Mexico", " ", 0, 5));
    }

    @Test
    void shouldThrowExceptionWhenFinishingMatchWithEmptyOrNullTeamNames() {
        assertInvalidTeamNames(() -> scoreboard.finishMatch(null, "Canada"));
        assertInvalidTeamNames(() -> scoreboard.finishMatch("Mexico", null));
        assertInvalidTeamNames(() -> scoreboard.finishMatch("", "Canada"));
        assertInvalidTeamNames(() -> scoreboard.finishMatch("Mexico", " "));
    }

    private void assertMatch(FootballMatch match, String homeTeam, String awayTeam, int homeScore, int awayScore) {
        assertEquals(homeTeam, match.homeTeam());
        assertEquals(awayTeam, match.awayTeam());
        assertEquals(homeScore, match.scores().homeTeamScore());
        assertEquals(awayScore, match.scores().awayTeamScore());
    }

    private void assertMatchOrder(List<FootballMatch> matches, String... expectedOrder) {
        assertEquals(expectedOrder.length, matches.size());
        for (int i = 0; i < expectedOrder.length; i++) {
            assertEquals(expectedOrder[i], matches.get(i).homeTeam());
        }
    }
}
