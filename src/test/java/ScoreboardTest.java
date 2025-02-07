import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.sportradar.test.lib.FootballMatch;
import com.sportradar.test.lib.Scoreboard;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import org.junit.jupiter.api.Test;
import java.util.List;

public class ScoreboardTest {

    @Test
    void shouldStartNewMatchWithInitialScore() {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.startMatch("Mexico", "Canada");

        List<FootballMatch> matches = scoreboard.getSummary();

        assertEquals(1, matches.size());
        assertEquals("Mexico", matches.get(0).homeTeam());
        assertEquals("Canada", matches.get(0).awayTeam());
        assertEquals(0, matches.get(0).scores().homeTeamScore());
        assertEquals(0, matches.get(0).scores().awayTeamScore());
    }

    @Test
    void shouldNotAllowDuplicateMatches() {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.startMatch("Mexico", "Canada");

        Exception exception = assertThrows(MatchAlreadyExistsException.class, () ->
            scoreboard.startMatch("Mexico", "Canada"));

        assertEquals("Match already exists: Mexico vs Canada", exception.getMessage());
    }

    @Test
    void shouldUpdateScoreForExistingMatch() {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);

        FootballMatch match = scoreboard.getSummary().get(0);

        assertEquals(0, match.scores().homeTeamScore());
        assertEquals(5, match.scores().awayTeamScore());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingMatch() {
        Scoreboard scoreboard = new Scoreboard();

        Exception exception = assertThrows(MatchNotFoundException.class, () -> {
            scoreboard.updateScore("Mexico", "Canada", 0, 5);
        });

        assertEquals("Match not found: Mexico vs Canada", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNegativeScores() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.startMatch("Mexico", "Canada");

        Exception homeTeamNegativeScoreException = assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", -1, 0));

        Exception awayTeamNegativeScoreException = assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore("Mexico", "Canada", 0, -5));

        assertEquals("Scores cannot be negative.", homeTeamNegativeScoreException.getMessage());
        assertEquals("Scores cannot be negative.", awayTeamNegativeScoreException.getMessage());
    }

    @Test
    void shouldFinishExistingMatch() {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.startMatch("Mexico", "Canada");

        scoreboard.finishMatch("Mexico", "Canada");

        List<FootballMatch> matches = scoreboard.getSummary();
        assertEquals(0, matches.size());
    }

    @Test
    void shouldThrowExceptionWhenFinishingNonExistingMatch() {
        Scoreboard scoreboard = new Scoreboard();

        Exception exception = assertThrows(MatchNotFoundException.class, () -> {
            scoreboard.finishMatch("Mexico", "Canada");
        });

        assertEquals("Match not found: Mexico vs Canada", exception.getMessage());
    }

    @Test
    void shouldReturnMatchesSortedByTotalScoreAndStartTime() {
        Scoreboard scoreboard = new Scoreboard();

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

        assertEquals(5, matches.size());
        assertEquals("Uruguay", matches.get(0).homeTeam());
        assertEquals("Spain", matches.get(1).homeTeam());
        assertEquals("Mexico", matches.get(2).homeTeam());
        assertEquals("Argentina", matches.get(3).homeTeam());
        assertEquals("Germany", matches.get(4).homeTeam());
    }

}
