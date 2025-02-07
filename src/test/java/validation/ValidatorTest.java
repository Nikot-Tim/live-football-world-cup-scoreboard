package validation;

import static utils.TestUtils.assertInvalidTeamNames;
import static utils.TestUtils.assertThrowsWithMessage;
import com.sportradar.test.lib.domain.FootballMatch;
import com.sportradar.test.lib.domain.MatchScores;
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
                () -> validator.validateMatchExists("Mexico vs Canada", matches),
                "Match not found: Mexico vs Canada"
        );
    }

    @Test
    void shouldThrowExceptionWhenMatchAlreadyExists() {
        matches.put("Mexico vs Canada", new FootballMatch("Mexico", "Canada", new MatchScores(0, 0)));

        assertThrowsWithMessage(
                MatchAlreadyExistsException.class,
                () -> validator.validateIfMatchAlreadyExists("Mexico vs Canada", matches),
                "Match already exists: Mexico vs Canada"
        );
    }

    @Test
    void shouldThrowExceptionForNegativeScores() {
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> validator.validateScores(-1, 1),
                "Scores cannot be negative."
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> validator.validateScores(1, -1),
                "Scores cannot be negative."
        );
    }

    @Test
    void shouldThrowExceptionForNullOrEmptyTeamNames() {
        assertInvalidTeamNames(() -> validator.validateTeams(null, "Canada"));
        assertInvalidTeamNames(() -> validator.validateTeams("Mexico", null));
        assertInvalidTeamNames(() -> validator.validateTeams("", "Canada"));
        assertInvalidTeamNames(() -> validator.validateTeams("Mexico", ""));
    }

}
