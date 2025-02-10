# Live Football World Cup Scoreboard Library

## Content
1. [Description of requirements](#description-of-requirements)
2. [Features and usage](#features-and-usage)
3. [Thread Safety](#thread-safety)
4. [Testing](#testing)
5. [Notes and assumptions](#notes-and-assumptions)


---

## Description of requirements

Live Football World Cup Scoreboard library shows all the ongoing matches and their scores.
The scoreboard supports the following operations:
1. Start a new match, assuming initial score 0 – 0 and adding it the scoreboard.
   This should capture following parameters:
   - a. Home team
   - b. Away team
2. Update score. This should receive a pair of absolute scores: home team score and away
   team score.
3. Finish match currently in progress. This removes a match from the scoreboard.
4. Get a summary of matches in progress ordered by their total score. The matches with the
   same total score will be returned ordered by the most recently started match in the
   scoreboard.

---

## Features and usage
### Initializing the scoreboard
```java
Clock clock = Clock.systemUTC();
Scoreboard scoreboard = new FootballWorldCupScoreboard(clock);
```

### Starting a match
```java
scoreboard.startMatch("Mexico", "Canada");
```

### Updating a match score
```java
scoreboard.updateScore("Mexico", "Canada", 0, 5);
```

### Finishing a match
```java
scoreboard.finishMatch("Mexico", "Canada");
```

### Retrieving the match summary
```java
List<FootballMatch> matches = scoreboard.getSummary();
```

---

## Thread Safety
This implementation ensures safe concurrent access using:
- **`ConcurrentHashMap`** – Handles match storage with high performance under concurrent updates.
- **Synchronized sorting** – Ensures match ordering remains consistent.
- **Immutable data records (`FootballMatch`, `MatchScores`)** – Guarantees that match objects cannot be modified once created.

---

## Testing
The library was developed using the TDD approach. 
It includes JUnit 5 tests that verify all functionality. 
To run the tests, use:
```sh
mvn test
```

---

## Notes and assumptions
Due to the lack of requirements, it was difficult to guess which methods would be most heavily loaded, so the implementation was designed according to one of the “Keep it simple” requirements.
But I would like to point out:
- in case of high load on update and match completion methods it is possible to remove validations on match existence, though it doesn't require much time, still if it is required the performance can be improved;
- might consider replacing ArrayList with PriorityQueue;
- development through deep abstractions and interfaces seemed redundant at this stage;
- the chosen approach for implementing thread safety is also chosen to be the easiest to implement and can be modified with the addition of new requirements;
- logging was considered but not added; the library should not force users to depend on other libraries, but even if you don't use a third-party library for logging, you shouldn't force users to use the library's logging format; instead, exceptions are thrown and users are free to decide what format to use for logging;
- unit-testing covers the developed functionality, but integration tests can be added in future iterations if necessary.

---

