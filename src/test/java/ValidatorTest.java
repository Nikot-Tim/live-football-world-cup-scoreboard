import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.sportradar.test.lib.FootballMatch;
import com.sportradar.test.lib.MatchScores;
import com.sportradar.test.lib.exception.MatchAlreadyExistsException;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import com.sportradar.test.lib.validation.Validator;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import java.util.Map;

class ValidatorTest {

    @Test
    void shouldThrowExceptionWhenMatchNotExists() {
        Validator validator = new Validator();
        Map<String, FootballMatch> matches = new LinkedHashMap<>();

        Exception exception = assertThrows(MatchNotFoundException.class, () -> {
            validator.validateMatchExists("Mexico vs Canada", matches);
        });

        assertEquals("Match not found: Mexico vs Canada", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMatchAlreadyExists() {
        Validator validator = new Validator();
        Map<String, FootballMatch> matches = new LinkedHashMap<>();
        matches.put("Mexico vs Canada", new FootballMatch("Mexico", "Canada", new MatchScores(0, 0)));

        Exception exception = assertThrows(MatchAlreadyExistsException.class, () -> {
            validator.validateIfMatchAlreadyExists("Mexico vs Canada", matches);
        });

        assertEquals("Match already exists: Mexico vs Canada", exception.getMessage());
    }
}
