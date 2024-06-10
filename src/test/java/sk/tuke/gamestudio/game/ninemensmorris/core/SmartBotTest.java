package sk.tuke.gamestudio.game.ninemensmorris.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.ninemensmorris.core.Field;
import sk.tuke.gamestudio.game.ninemensmorris.core.Player;
import sk.tuke.gamestudio.game.ninemensmorris.core.SmartBot;

import static org.junit.jupiter.api.Assertions.*;

public class SmartBotTest {
    private SmartBot smartBot;
    private Player opponent;
    private Field gameBoard;

    @BeforeEach
    public void setUp() {
        opponent = new Player("Opponent", "X");
        smartBot = new SmartBot("SmartBot", "O", opponent);
        gameBoard = new Field();
    }

    @Test
    public void testGetTileIndexToBlock() {
        gameBoard.placePiece(9, opponent);
        gameBoard.placePiece(11, opponent);
        gameBoard.placePiece(18, opponent);
        gameBoard.placePiece(3, smartBot);

        int[] result = smartBot.getTileIndexToBlock(gameBoard);
        gameBoard.printField();

        assertArrayEquals(new int[]{3, 10}, result);
    }
    @Test
    public void testGetTileIndexToBlock_notAdjacent() {
        gameBoard.placePiece(9, opponent);
        gameBoard.placePiece(11, opponent);
        gameBoard.placePiece(18, opponent);
        gameBoard.placePiece(4, smartBot);

        int[] result = smartBot.getTileIndexToBlock(gameBoard);
        gameBoard.printField();

        assertArrayEquals(new int[]{-1, -1}, result);
    }

    @Test
    public void testGetIndexToBlockPlacing() {
        gameBoard.placePiece(9, opponent);
        gameBoard.placePiece(11, opponent);
        gameBoard.placePiece(18, opponent);
        gameBoard.placePiece(3, smartBot);

        int result = smartBot.getIndexToBlockPlacing(gameBoard);
        gameBoard.printField();

        assertEquals(10, result);
    }
    @Test
    public void testGetIndexToFormMill(){
        gameBoard.placePiece(9, smartBot);
        gameBoard.placePiece(11, smartBot);
        gameBoard.placePiece(18, smartBot);
        gameBoard.placePiece(3, opponent);

        int result = smartBot.getIndexToFormMill(gameBoard);
        gameBoard.printField();

        assertEquals(10, result);
    }
    @Test
    public void testIndexToPlacePiece_BlockingStrategy() {
        gameBoard.placePiece(9, opponent);
        gameBoard.placePiece(11, opponent);
        gameBoard.placePiece(18, opponent);
        gameBoard.placePiece(3, smartBot);

        int result = smartBot.getIndexToPlacePiece(gameBoard);
        gameBoard.printField();

        assertEquals(11, result);
    }
    @Test
    public void testIndexToPlacePiece_FormingMillStrategy() {
        gameBoard.placePiece(9, smartBot);
        gameBoard.placePiece(11, smartBot);
        gameBoard.placePiece(18, smartBot);
        gameBoard.placePiece(3, opponent);

        int result = smartBot.getIndexToPlacePiece(gameBoard);
        gameBoard.printField();

        assertEquals(11, result);
    }
    @Test
    public void testGetIndexToRemovePieceOfOpponent(){
        gameBoard.placePiece(0, opponent);
        gameBoard.placePiece(1, opponent);

        int result = smartBot.getIndexToRemovePieceOfOpponent(gameBoard);
        gameBoard.printField();

        assertTrue(result == 1 || result == 2);
    }

    @Test
    public void testGetMove_millForming(){
        gameBoard.placePiece(0, smartBot);
        gameBoard.placePiece(1, smartBot);
        gameBoard.placePiece(14, smartBot);

        int[] result = smartBot.getMove(gameBoard);
        gameBoard.printField();

        assertTrue(result[0] == 15 || result[1] == 3);
    }
    @Test
    public void testGetMove_blockingStrategy(){
        gameBoard.placePiece(9, opponent);
        gameBoard.placePiece(11, opponent);
        gameBoard.placePiece(18, opponent);
        gameBoard.placePiece(3, smartBot);

        int[] result = smartBot.getMove(gameBoard);
        System.out.println("From: " + (result[0]) + " To: " + (result[1]));
        gameBoard.printField();

        assertTrue(result[0] == 3 || result[1] == 10);
    }
}