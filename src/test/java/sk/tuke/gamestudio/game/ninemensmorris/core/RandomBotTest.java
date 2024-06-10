package sk.tuke.gamestudio.game.ninemensmorris.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.ninemensmorris.core.Field;
import sk.tuke.gamestudio.game.ninemensmorris.core.Player;
import sk.tuke.gamestudio.game.ninemensmorris.core.RandomBot;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RandomBotTest{

    private Field field;
    private RandomBot bot;
    private Player player1;

    @BeforeEach
    public void setUp() {
        field = new Field();
        player1 = new Player("PLAYER_1", "X");
        bot = new RandomBot("Bot", "O", player1);
    }

    @Test
    public void testGetIndexToPlacePiece_findLastEmptyTile() {
        for (int i = 0; i < field.NUM_OF_POSITIONS-1; i++) {
            field.placePiece(i, player1);
        }
        int index = bot.getIndexToPlacePiece(field);
        field.printField();
        System.out.println("Index: " + index);
        assertTrue(index == 24);
    }

    @Test
    public void testGetIndexToPlacePiece_fieldIsFull() {
        for (int i = 0; i < field.NUM_OF_POSITIONS; i++) {
            field.placePiece(i, player1);
        }
        int index = bot.getIndexToPlacePiece(field);
        field.printField();
        System.out.println("Index: " + index);
        assertTrue(index == -1 );
    }

    @Test
    public void testGetIndexToRemovePieceOfOpponent() {
        field.placePiece(0, player1);
        int index = bot.getIndexToRemovePieceOfOpponent(field);
        field.printField();
        System.out.println("Index: " + index);
        assertEquals(1, index);
    }
    @Test
    public void testGetIndexToRemovePieceOfOpponent_opponentHasNoTiles() {
        int index = bot.getIndexToRemovePieceOfOpponent(field);
        field.printField();
        System.out.println("Index: " + index);
        assertEquals(-1, index);
    }

    @Test
    public void testGetValidMoves() {
        field.placePiece(0, bot);
        List<int[]> validMoves = bot.getValidMoves(field);
        field.printField();
        assertFalse(validMoves.isEmpty());
        for (int[] move : validMoves) {
            assertTrue(move[0] >= 0 && move[0] < field.NUM_OF_POSITIONS);
            assertTrue(move[1] >= 0 && move[1] < field.NUM_OF_POSITIONS);
        }
    }
    @Test
    public void testGetValidMoves_noValidMoves() {
        field.placePiece(0, bot);
        for (int i = 1; i < field.NUM_OF_POSITIONS; i++) {
            field.placePiece(i, player1);
        }
        List<int[]> validMoves = bot.getValidMoves(field);
        field.printField();
        assertTrue(validMoves.isEmpty());
    }

}