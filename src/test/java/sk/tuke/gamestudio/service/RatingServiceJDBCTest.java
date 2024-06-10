package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RatingServiceJDBCTest {
    private RatingServiceJDBC ratingServiceJDBC;

    @BeforeEach
    void setUp() {
        ratingServiceJDBC = new RatingServiceJDBC();
        ratingServiceJDBC.reset();
    }

    @Test
    void setRatingShouldInsertRating() {
        Rating rating = new Rating("game1", "player1", 5, new Date());
        assertDoesNotThrow(() -> ratingServiceJDBC.setRating(rating));
        assertEquals(5, assertDoesNotThrow(() -> ratingServiceJDBC.getRating("game1", "player1")));
    }

    @Test
    void getAverageRatingShouldReturnAverageRating() {
        String game = "game1";
        assertDoesNotThrow(() -> ratingServiceJDBC.getAverageRating(game));
    }
    @Test
    void getAverageRating() {
        Rating rating1 = new Rating("game1", "player1", 5, new Date());
        ratingServiceJDBC.setRating(rating1);
        Rating rating2 = new Rating("game1", "player1", 3, new Date());
        ratingServiceJDBC.setRating(rating2);
        assertDoesNotThrow(() -> ratingServiceJDBC.getAverageRating("game1"));
        assertEquals(4, assertDoesNotThrow(() -> ratingServiceJDBC.getAverageRating("game1")));
    }
    @Test
    void getAverageRatingShouldReturnZeroWhenGameDoesNotExist() {
        String game = "nonExistingGame";
        assertEquals(0, assertDoesNotThrow(() -> ratingServiceJDBC.getAverageRating(game)));
    }

    @Test
    void getRatingShouldReturnRating() {
        String game = "game1";
        String player = "player1";
        assertDoesNotThrow(() -> ratingServiceJDBC.getRating(game, player));
    }

    @Test
    void getRatingShouldReturnZeroWhenGameOrPlayerDoesNotExist() {
        String game = "nonExistingGame";
        String player = "nonExistingPlayer";
        assertEquals(0, assertDoesNotThrow(() -> ratingServiceJDBC.getRating(game, player)));
    }

    @Test
    void resetShouldDeleteAllRatings() {
        assertDoesNotThrow(() -> ratingServiceJDBC.reset());
        assertEquals(0, assertDoesNotThrow(() -> ratingServiceJDBC.getAverageRating("game1")));
    }

}