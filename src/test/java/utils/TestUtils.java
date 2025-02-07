package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUtils {

    public static void assertThrowsWithMessage(Class<? extends Throwable> exceptionClass, Runnable action, String expectedMessage) {
        Exception exception = (Exception) assertThrows(exceptionClass, action::run);
        assertEquals(expectedMessage, exception.getMessage());
    }

    public static void assertInvalidTeamNames(Runnable action) {
        assertThrowsWithMessage(IllegalArgumentException.class, action, "Team names must not be null or empty.");
    }
}
