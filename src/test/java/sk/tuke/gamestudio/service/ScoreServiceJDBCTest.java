package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Score;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreServiceJDBCTest {
    private ScoreServiceJDBC scoreServiceJDBC;

    @BeforeEach
    void setUp() {
        scoreServiceJDBC = new ScoreServiceJDBC();
        scoreServiceJDBC.reset();
    }

    @Test
    void addScoreShouldInsertScore() {
        Score score = new Score("game1", "player1", 100, new Date());
        assertDoesNotThrow(() -> scoreServiceJDBC.addScore(score));
    }
    @Test
    public void addScore() {
        Date date = new Date();

        scoreServiceJDBC.addScore(new Score("game1", "Dano", 1000, date));

        List<Score> scores = scoreServiceJDBC.getTopScores("game1");
        assertEquals(1, scores.size());
        assertEquals("game1", scores.get(0).getGame());
        assertEquals("Dano", scores.get(0).getPlayer());
        assertEquals(1000, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());
    }
    @Test
    public void getTopScores() {
        Date date = new Date();
        scoreServiceJDBC.addScore(new Score("game1", "Kristian", 1200, date));
        scoreServiceJDBC.addScore(new Score("game1", "Samo", 1500, date));
        scoreServiceJDBC.addScore(new Score("game1", "Dano", 1800, date));
        scoreServiceJDBC.addScore(new Score("game1", "Alex", 1000, date));

        List<Score> scores = scoreServiceJDBC.getTopScores("game1");

        assertEquals(4, scores.size());

        assertEquals("game1", scores.get(0).getGame());
        assertEquals("Dano", scores.get(0).getPlayer());
        assertEquals(1800, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());

        assertEquals("game1", scores.get(1).getGame());
        assertEquals("Samo", scores.get(1).getPlayer());
        assertEquals(1500, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedOn());

        assertEquals("game1", scores.get(2).getGame());
        assertEquals("Kristian", scores.get(2).getPlayer());
        assertEquals(1200, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedOn());
    }


    @Test
    void getTopScoresShouldReturnListOfScores() {
        String game = "game1";
        Score score = new Score("game1", "player1", 100, new Date());
        assertDoesNotThrow(() -> scoreServiceJDBC.addScore(score));
        List<Score> scores = assertDoesNotThrow(() -> scoreServiceJDBC.getTopScores(game));
        assertFalse(scores.isEmpty());
    }

    @Test
    void getTopScoresShouldReturnEmptyListWhenGameDoesNotExist() {
        String game = "nonExistingGame";
        List<Score> scores = assertDoesNotThrow(() -> scoreServiceJDBC.getTopScores(game));
        assertTrue(scores.isEmpty());
    }

    @Test
    void resetShouldDeleteAllScores1() {
        assertDoesNotThrow(() -> scoreServiceJDBC.reset());
    }
    @Test
    public void resetShouldDeleteAllScores2() {
        Score score = new Score("game1", "player1", 100, new Date());
        scoreServiceJDBC.addScore(score);
        scoreServiceJDBC.reset();
        assertEquals(0, scoreServiceJDBC.getTopScores("game1").size());
    }

}