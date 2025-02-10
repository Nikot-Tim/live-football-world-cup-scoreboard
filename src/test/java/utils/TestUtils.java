package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Utility class for test constants and helper methods.
 * Provides reusable test data and assertions for unit tests.
 */
public class TestUtils {

    public static final String HOME_TEAM_TEST_NAME = "HomeTeamTestName";
    public static final String AWAY_TEAM_TEST_NAME = "AwayTeamTestName";
    public static final String TEST_MATCH_KEY = "HomeTeamTestName vs AwayTeamTestName";
    public static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.of("UTC"));

    public static void assertThrowsWithMessage(Class<? extends Throwable> exceptionClass, Runnable action, String expectedMessage) {
        Exception exception = (Exception) assertThrows(exceptionClass, action::run);
        assertEquals(expectedMessage, exception.getMessage());
    }

    public static void assertInvalidTeamNames(Runnable action) {
        assertThrowsWithMessage(IllegalArgumentException.class, action, "Team names must not be null or empty.");
    }
}
