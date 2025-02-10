import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.TestUtils.AWAY_TEAM_TEST_NAME;
import static utils.TestUtils.HOME_TEAM_TEST_NAME;
import static utils.TestUtils.TEST_MATCH_KEY;
import static utils.TestUtils.assertInvalidTeamNames;
import static utils.TestUtils.assertThrowsWithMessage;
import com.sportradar.test.lib.FootballWorldCupScoreboard;
import com.sportradar.test.lib.Scoreboard;
import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import com.sportradar.test.lib.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestUtils;
import java.time.Clock;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit tests for FootballWorldCupScoreboard.
 * Ensures correct match management, score updates, and sorting.
 */
class FootballWorldCupScoreboardTest {
    private Scoreboard footballWorldCupScoreboard;

    @BeforeEach
    void setUp() {
        Clock fixedClock = TestUtils.FIXED_CLOCK;
        footballWorldCupScoreboard = new FootballWorldCupScoreboard(fixedClock);
    }

    @Test
    void shouldStartNewMatchWithInitialScore() {
        footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);

        List<FootballMatch> matches = footballWorldCupScoreboard.getSummary();

        assertEquals(1, matches.size());
        assertMatch(matches.get(0), HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 0);
    }

    @Test
    void shouldNotAllowDuplicateMatches() {
        footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);

        assertThrowsWithMessage(
                MatchAlreadyExistsException.class,
                () -> footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME),
                MatchAlreadyExistsException.MESSAGE_PREFIX + TEST_MATCH_KEY
        );
    }

    @Test
    void shouldUpdateScoreForExistingMatch() {
        footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);
        footballWorldCupScoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 5);

        FootballMatch match = footballWorldCupScoreboard.getSummary().get(0);
        assertMatch(match, HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 5);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingMatch() {
        assertThrowsWithMessage(
                MatchNotFoundException.class,
                () -> footballWorldCupScoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 5),
                MatchNotFoundException.MESSAGE_PREFIX + TEST_MATCH_KEY
        );
    }

    @Test
    void shouldThrowExceptionForNegativeScores() {
        footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> footballWorldCupScoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, -1, 0),
                Validator.NEGATIVE_SCORES_ERROR
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> footballWorldCupScoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, -5),
                Validator.NEGATIVE_SCORES_ERROR
        );
    }

    @Test
    void shouldFinishExistingMatch() {
        footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);
        footballWorldCupScoreboard.finishMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);

        assertEquals(0, footballWorldCupScoreboard.getSummary().size());
    }

    @Test
    void shouldThrowExceptionWhenFinishingNonExistingMatch() {
        assertThrowsWithMessage(
                MatchNotFoundException.class,
                () -> footballWorldCupScoreboard.finishMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME),
                MatchNotFoundException.MESSAGE_PREFIX + TEST_MATCH_KEY
        );
    }

    @Test
    void shouldReturnMatchesSortedByTotalScoreAndStartTime() {
        footballWorldCupScoreboard.startMatch("Mexico", "Canada");
        footballWorldCupScoreboard.startMatch("Spain", "Brazil");
        footballWorldCupScoreboard.startMatch("Germany", "France");
        footballWorldCupScoreboard.startMatch("Uruguay", "Italy");
        footballWorldCupScoreboard.startMatch("Argentina", "Australia");

        footballWorldCupScoreboard.updateScore("Mexico", "Canada", 0, 5);
        footballWorldCupScoreboard.updateScore("Spain", "Brazil", 10, 2);
        footballWorldCupScoreboard.updateScore("Germany", "France", 2, 2);
        footballWorldCupScoreboard.updateScore("Uruguay", "Italy", 6, 6);
        footballWorldCupScoreboard.updateScore("Argentina", "Australia", 3, 1);

        List<FootballMatch> matches = footballWorldCupScoreboard.getSummary();

        assertMatchOrder(matches, "Uruguay", "Spain", "Mexico", "Argentina", "Germany");
    }

    @Test
    void shouldThrowExceptionWhenStartingMatchWithEmptyOrNullTeamNames() {
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.startMatch(null, AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, null));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.startMatch("", AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, " "));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMatchWithEmptyOrNullTeamNames() {
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.updateScore(null, AWAY_TEAM_TEST_NAME, 0, 5));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.updateScore(HOME_TEAM_TEST_NAME, null, 0, 5));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.updateScore("", AWAY_TEAM_TEST_NAME, 0, 5));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.updateScore(HOME_TEAM_TEST_NAME, " ", 0, 5));
    }

    @Test
    void shouldThrowExceptionWhenFinishingMatchWithEmptyOrNullTeamNames() {
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.finishMatch(null, AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.finishMatch(HOME_TEAM_TEST_NAME, null));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.finishMatch("", AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> footballWorldCupScoreboard.finishMatch(HOME_TEAM_TEST_NAME, " "));
    }

    @Test
    void shouldHandleConcurrentMatchStarts() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int matchNumber = i;
            executorService.submit(() -> {
                try {
                    footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME + matchNumber, AWAY_TEAM_TEST_NAME + matchNumber);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        assertEquals(threadCount, footballWorldCupScoreboard.getSummary().size());
    }

    @Test
    void shouldHandleConcurrentScoreUpdates() throws InterruptedException {
        footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int score = i;
            executorService.submit(() -> {
                try {
                    footballWorldCupScoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, score, score);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        FootballMatch match = footballWorldCupScoreboard.getSummary().get(0);
        assertTrue(match.getHomeScore() >= 0 && match.getAwayScore() >= 0);
    }

    @Test
    void shouldHandleConcurrentMatchRemovals() throws InterruptedException {
        footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    footballWorldCupScoreboard.finishMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        assertTrue(footballWorldCupScoreboard.getSummary().isEmpty());
    }

    @Test
    void shouldHandleConcurrentReadsAndWrites() throws InterruptedException {
        footballWorldCupScoreboard.startMatch(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME);
        int writerThreads = 5;
        int readerThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(writerThreads + readerThreads);
        CountDownLatch latch = new CountDownLatch(writerThreads + readerThreads);

        for (int i = 0; i < writerThreads; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < 5; j++) {
                        footballWorldCupScoreboard.updateScore(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, j, j);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < readerThreads; i++) {
            executorService.submit(() -> {
                try {
                    List<FootballMatch> summary = footballWorldCupScoreboard.getSummary();
                    assertNotNull(summary);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        FootballMatch match = footballWorldCupScoreboard.getSummary().get(0);
        assertTrue(match.getHomeScore() >= 0 && match.getAwayScore() >= 0);
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
