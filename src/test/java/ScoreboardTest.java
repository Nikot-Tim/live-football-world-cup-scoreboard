import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.sportradar.test.lib.FootballMatch;
import com.sportradar.test.lib.Scoreboard;
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

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            scoreboard.startMatch("Mexico", "Canada"));

        assertEquals("Match already exists: Mexico vs Canada", exception.getMessage());
    }
}
