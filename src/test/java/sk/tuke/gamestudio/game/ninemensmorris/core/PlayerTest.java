package sk.tuke.gamestudio.game.ninemensmorris.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player1;
    @BeforeEach
    public void setUp() {
        player1 = new Player("PLAYER_1", "X");
    }

    @Test
    public void canMove_returnsTrueIfPlayerStateMoving() {
        player1.setState(PlayerState.MOVING);
        assertTrue(player1.canMove());
    }
    @Test
    public void canMove_returnsFalseIfPlayerStatePlacing() {
        player1.setState(PlayerState.PLACING);
        assertFalse(player1.canMove());
    }
    @Test
    public void incrementNumberOfPiecesLeftToPlace_increasesCountByOne() {
        int initialCount = player1.getNumberOfPiecesLeftToPlace();
        player1.incrementNumberOfPiecesLeftToPlace();
        assertEquals(initialCount + 1, player1.getNumberOfPiecesLeftToPlace());
    }

    @Test
    public void decrementNumberOfPiecesLeftToPlace_decreasesCountByOne() {
        player1.setNumberOfPiecesLeftToPlace(2);
        player1.decrementNumberOfPiecesLeftToPlace();
        assertEquals(1, player1.getNumberOfPiecesLeftToPlace());
    }

    @Test
    public void decrementNumberOfPiecesLeftToPlace_doesNotGoBelowZero() {
        player1.setNumberOfPiecesLeftToPlace(0);
        player1.decrementNumberOfPiecesLeftToPlace();
        assertEquals(0, player1.getNumberOfPiecesLeftToPlace());
    }

    @Test
    public void incrementNumberOfPiecesPlaced_increasesCountByOne() {
        int initialCount = player1.getNumberOfPiecesPlaced();
        player1.incrementNumberOfPiecesPlaced();
        assertEquals(initialCount + 1, player1.getNumberOfPiecesPlaced());
    }

    @Test
    public void decrementNumberOfPiecesPlaced_decreasesCountByOne() {
        player1.setNumberOfPiecesPlaced(2);
        player1.decrementNumberOfPiecesPlaced();
        assertEquals(1, player1.getNumberOfPiecesPlaced());
    }

    @Test
    public void decrementNumberOfPiecesPlaced_doesNotGoBelowZero() {
        player1.setNumberOfPiecesPlaced(0);
        player1.decrementNumberOfPiecesPlaced();
        assertEquals(0, player1.getNumberOfPiecesPlaced());
    }
}
