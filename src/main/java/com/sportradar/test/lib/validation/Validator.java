package com.sportradar.test.lib.validation;

import com.sportradar.test.lib.FootballMatch;
import com.sportradar.test.lib.exception.MatchNotFoundException;
import java.util.Map;

public class Validator {

    public void validateMatchExists(String matchKey, Map<String, FootballMatch> matches) {
        if (!matches.containsKey(matchKey)) {
            throw new MatchNotFoundException(matchKey);
        }
    }

    public void validateIfMatchAlreadyExists(String matchKey, Map<String, FootballMatch> matches) {
    }


}
