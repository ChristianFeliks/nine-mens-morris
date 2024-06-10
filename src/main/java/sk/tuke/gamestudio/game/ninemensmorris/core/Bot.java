package sk.tuke.gamestudio.game.ninemensmorris.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Bot extends Player{

    protected Player opponent;
    public Bot(String name, String symbol, Player opponent) {
        super(name, symbol);
        this.opponent = opponent;
    }

    public abstract int getIndexToPlacePiece(Field gameBoard);

    public abstract int getIndexToRemovePieceOfOpponent(Field gameBoard);

    public List<int[]> getValidMoves(Field gameBoard) {
        List<int[]> validMoves = new ArrayList<>();
        for (int from = 0; from < gameBoard.NUM_OF_POSITIONS; from++) {
            Tile fromTile = gameBoard.getFieldPositionByIndex(from);
            if (fromTile.getState() == TileState.OCCUPIED && fromTile.getPlayerOccupying() == this) {
                for (int to = 0; to < gameBoard.NUM_OF_POSITIONS; to++) {
                    if (gameBoard.isMoveValid(from, to, this)) {
                        validMoves.add(new int[]{from+1, to+1}); // need to add 1 to the indices else ConsoleUI will mess up
                    }
                }
            }
        }

        /*System.out.println("Valid moves:");
        for (int[] move : validMoves) {
            System.out.println("From: " + (move[0]) + " To: " + (move[1]));
        }*/

        return validMoves;
    }

    protected List<int[]> getValidMovesOfOpponent(Field gameBoard) {
        List<int[]> validMoves = new ArrayList<>();
        for (int from = 0; from < gameBoard.NUM_OF_POSITIONS; from++) {
            Tile fromTile = gameBoard.getFieldPositionByIndex(from);
            if (fromTile.getState() == TileState.OCCUPIED && fromTile.getPlayerOccupying() == opponent) {
                for (int to = 0; to < gameBoard.NUM_OF_POSITIONS; to++) {
                    if (gameBoard.isMoveValid(from, to, opponent)) {
                        validMoves.add(new int[]{from + 1, to + 1}); // need to add 1 to the indices else ConsoleUI will mess up
                    }
                }
            }
        }
        /*System.out.println("Valid moves:");
        for (int[] move : validMoves) {
            System.out.println("From: " + (move[0]) + " To: " + (move[1]));
        }*/
        return validMoves;
    }
    public abstract int[] getMove(Field gameBoard);



}
