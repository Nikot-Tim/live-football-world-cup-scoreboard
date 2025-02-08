package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUtils {

    public static final String HOME_TEAM_TEST_NAME = "HomeTeamTestName";
    public static final String AWAY_TEAM_TEST_NAME = "AwayTeamTestName";
    public static final String TEST_MATCH_KEY = "HomeTeamTestName vs AwayTeamTestName";

    public static void assertThrowsWithMessage(Class<? extends Throwable> exceptionClass, Runnable action, String expectedMessage) {
        Exception exception = (Exception) assertThrows(exceptionClass, action::run);
        assertEquals(expectedMessage, exception.getMessage());
    }

    public static void assertInvalidTeamNames(Runnable action) {
        assertThrowsWithMessage(IllegalArgumentException.class, action, "Team names must not be null or empty.");
    }
}
