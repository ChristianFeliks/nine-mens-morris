package sk.tuke.gamestudio.game.ninemensmorris.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sk.tuke.gamestudio.game.ninemensmorris.core.Field;
import sk.tuke.gamestudio.game.ninemensmorris.core.Player;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Field field;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        field = new Field();
        player1 = new Player("PLAYER_1", "X");
        player2 = new Player("PLAYER_2", "O");
    }

    @Test
    public void wasMillCreated1_returnsTrueIfMillWasCreated() {
        field.placePiece(0, player1);
        field.placePiece(1, player1);
        field.placePiece(2, player1);
        field.printField();
        assertTrue(field.wasMillCreated(2, player1));

    }
    @Test
    public void wasMillCreated2_returnsTrueIfMillWasCreated() {
        field.placePiece(0, player1);
        field.placePiece(1, player1);
        field.placePiece(2, player1);
        field.printField();
        assertTrue(field.wasMillCreated(1, player1));
    }
    @Test
    public void wasMillCreated3_returnsTrueIfMillWasCreated() {
        field.placePiece(0, player1);
        field.placePiece(1, player1);
        field.placePiece(2, player1);
        field.printField();
        assertTrue(field.wasMillCreated(0, player1));
    }

    @Test
    public void wasMillCreated_returnsFalseIfWrongIndex() {
        field.placePiece(0, player1);
        field.placePiece(1, player1);
        field.placePiece(2, player1);
        field.printField();
        assertFalse(field.wasMillCreated(9, player1));
    }



    /*@Test
    public void placePiece_onOccupiedField() {
        field.placePiece(0, player1);
        field.printField();
        assertThrows(IllegalArgumentException.class, () -> field.placePiece(0, player1));
    }*/
    @Test
    public void placePiece_onUnoccupiedField() {
        field.placePiece(0, player1);
        field.printField();
        assertEquals(player1, field.getFieldPositionByIndex(0).getPlayerOccupying());
    }

    @Test
    public void placePiece_increasesTotalNumOfPiecesPlacedOnBoard() {
        field.placePiece(0, player1);
        field.printField();
        assertEquals(1, field.getTotalNumOfPiecesPlacedOnBoard());
    }




    @Test
    public void removePiece_decreasesTotalNumOfPiecesPlacedOnBoard() {
        field.placePiece(0, player1);
        field.removePiece(0);
        field.printField();
        assertEquals(0, field.getTotalNumOfPiecesPlacedOnBoard());
    }
    @Test
    public void removePiece_fromOccupiedTile(){
        field.placePiece(0, player1);
        field.removePiece(0);
        field.printField();
        assertNull(field.getFieldPositionByIndex(0).getPlayerOccupying());
        assertEquals(0, field.getTotalNumOfPiecesPlacedOnBoard());
        assertEquals(0, player1.getNumberOfPiecesPlaced());
        assertEquals(8, player1.getNumberOfPiecesLeftToPlace());
    }
    @Test
    public void removePiece_fromUnoccupiedTile(){
        field.removePiece(0);
        field.printField();
        assertNull(field.getFieldPositionByIndex(0).getPlayerOccupying());
        assertEquals(0, field.getTotalNumOfPiecesPlacedOnBoard());
        assertEquals(0, player1.getNumberOfPiecesPlaced());
        assertEquals(9, player1.getNumberOfPiecesLeftToPlace());
    }



    @Test
    public void wasMillCreated_returnsFalseIfNoMill() {
        field.placePiece(0, player1);
        field.placePiece(1, player1);
        field.printField();
        assertFalse(field.wasMillCreated(0, player1));
    }

    @Test
    public void wasMillCreated_returnsFalseIfMillByAnotherPlayer() {
        field.placePiece(0, player2);
        field.placePiece(1, player2);
        field.placePiece(2, player2);
        field.printField();
        assertFalse(field.wasMillCreated(2, player1));
    }



    @Test
    public void isMoveValid_returnsFalseWhenMovingToOccupiedPosition() {
        field.placePiece(0, player1);
        field.placePiece(1, player1);
        field.printField();
        assertFalse(field.isMoveValid(0, 1, player1));
    }
    @Test
    public void isMoveValid_returnsFalseWhenMovingFromUnoccupiedPosition() {
        field.printField();
        assertFalse(field.isMoveValid(0, 1, player1));
    }

    @Test
    public void isMoveValid_returnsFalseForNonAdjacentMove() {
        field.placePiece(0, player1);
        field.printField();
        assertFalse(field.isMoveValid(0, 2, player1));
    }
    @Test
    public void isAdjacent(){
        field.placePiece(0, player1);
        field.placePiece(2, player1);
        field.printField();
        assertFalse(field.getFieldPositionByIndex(2).isAdjacent(field.getFieldPositionByIndex(0)));
        assertFalse(field.getFieldPositionByIndex(0).isAdjacent(field.getFieldPositionByIndex(2)));
        assertFalse(field.getFieldPositionByIndex(23).isAdjacent(field.getFieldPositionByIndex(0)));
        assertTrue(field.getFieldPositionByIndex(0).isAdjacent(field.getFieldPositionByIndex(9)));


    }

    @Test
    public void isMoveValid_returnsTrueForAdjacentMove() {
        field.placePiece(3, player1);
        field.printField();
        assertTrue(field.isMoveValid(3, 4, player1));
    }



    @Test
    public void movePiece_movesPieceForValidMove() {
        field.placePiece(0, player1);
        field.movePiece(0, 1, player1);
        field.printField();
        assertEquals(player1, field.getFieldPositionByIndex(1).getPlayerOccupying());
        assertNull(field.getFieldPositionByIndex(0).getPlayerOccupying());
    }

    @Test
    public void movePiece_doesNotMovePieceForInvalidMove() {
        field.placePiece(0, player1);
        field.printField();
        field.movePiece(0, 2, player1);
        field.printField();
        assertEquals(player1, field.getFieldPositionByIndex(0).getPlayerOccupying());
        assertNull(field.getFieldPositionByIndex(2).getPlayerOccupying());
    }

    @Test
    public void hasLegalMove_returnsTrueWhenPlayerHasLegalMove() {
        field.placePiece(0, player1);
        assertTrue(field.hasLegalMove(player1));
    }

    @Test
    public void hasLegalMove_returnsFalseWhenPlayerHasNoPiecesOnBoard() {
        assertFalse(field.hasLegalMove(player1));
    }

    @Test
    public void hasLegalMove_returnsFalseWhenBoardIsFull() {
        for (int i = 0; i < 24; i++) {
            field.placePiece(i, player2);
        }
        field.printField();
        assertFalse(field.hasLegalMove(player2));
    }

    @Test
    public void hasLegalMove_returnsFalseInGameSituation() {
        field.placePiece(0, player1);
        field.placePiece(1, player1);
        field.placePiece(2, player1);
        field.placePiece(3, player1);
        field.placePiece(4, player1);
        field.placePiece(5, player1);

        field.placePiece(9, player2);
        field.placePiece(10, player2);
        field.placePiece(11, player2);
        field.placePiece(6, player2);
        field.placePiece(7, player2);
        field.placePiece(8, player2);
        field.placePiece(12, player2);
        field.placePiece(13, player2);
        field.placePiece(14, player2);

        field.printField();
        assertFalse(field.hasLegalMove(player1));
    }
}