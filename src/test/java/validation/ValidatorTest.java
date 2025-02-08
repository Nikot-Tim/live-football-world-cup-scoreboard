package validation;

import static utils.TestUtils.AWAY_TEAM_TEST_NAME;
import static utils.TestUtils.HOME_TEAM_TEST_NAME;
import static utils.TestUtils.TEST_MATCH_KEY;
import static utils.TestUtils.assertInvalidTeamNames;
import static utils.TestUtils.assertThrowsWithMessage;
import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import com.sportradar.test.lib.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import java.util.Map;

class ValidatorTest {
    private Validator validator;
    private Map<String, FootballMatch> matches;

    @BeforeEach
    void setUp() {
        validator = new Validator();
        matches = new LinkedHashMap<>();
    }

    @Test
    void shouldThrowExceptionWhenMatchNotExists() {
        assertThrowsWithMessage(
                MatchNotFoundException.class,
                () -> validator.validateMatchExists(TEST_MATCH_KEY, matches),
                MatchNotFoundException.MESSAGE_PREFIX + TEST_MATCH_KEY
        );
    }

    @Test
    void shouldThrowExceptionWhenMatchAlreadyExists() {
        matches.put(TEST_MATCH_KEY, FootballMatch.withScores(HOME_TEAM_TEST_NAME, AWAY_TEAM_TEST_NAME, 0, 0));

        assertThrowsWithMessage(
                MatchAlreadyExistsException.class,
                () -> validator.validateIfMatchAlreadyExists(TEST_MATCH_KEY, matches),
                MatchAlreadyExistsException.MESSAGE_PREFIX + TEST_MATCH_KEY
        );
    }

    @Test
    void shouldThrowExceptionForNegativeScores() {
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> validator.validateScores(-1, 1),
                Validator.NEGATIVE_SCORES_ERROR
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> validator.validateScores(1, -1),
                Validator.NEGATIVE_SCORES_ERROR
        );
    }

    @Test
    void shouldThrowExceptionForNullOrEmptyTeamNames() {
        assertInvalidTeamNames(() -> validator.validateTeams(null, AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> validator.validateTeams(HOME_TEAM_TEST_NAME, null));
        assertInvalidTeamNames(() -> validator.validateTeams("", AWAY_TEAM_TEST_NAME));
        assertInvalidTeamNames(() -> validator.validateTeams(HOME_TEAM_TEST_NAME, ""));
    }

}
