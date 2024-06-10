package sk.tuke.gamestudio.game.ninemensmorris.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBot extends Bot {
    private Random random;

    public RandomBot(String name, String symbol, Player opponent){
        super(name, symbol, opponent);
        this.random = new Random();
    }

    @Override
    public int getIndexToPlacePiece(Field gameBoard) {
        List<Integer> availableTiles = gameBoard.getAvailableTiles();
        if (availableTiles.isEmpty()) {
            return -1;
        }
        int randomIndex = random.nextInt(availableTiles.size());
        //System.out.println("RandomBot: " + getName() + " is placing piece on " + (availableTiles.get(randomIndex)));
        return availableTiles.get(randomIndex)+1;
    }

    @Override
    public int getIndexToRemovePieceOfOpponent(Field gameBoard) {
        List<Integer> opponentTiles = gameBoard.getOpponentTiles(super.getSymbol());
        if (opponentTiles.isEmpty()) {
            return -1;
        }
        int randomIndex = random.nextInt(opponentTiles.size());
        //System.out.println("RandomBot: " + getName() + " is removing piece from " + (opponentTiles.get(randomIndex)));
        return opponentTiles.get(randomIndex)+1;
    }

    @Override
    public int[] getMove(Field gameBoard) {
        List<int[]> validMoves = getValidMoves(gameBoard);
        if (validMoves.isEmpty()) {
            return null;
        }
        int randomIndex = random.nextInt(validMoves.size());
        //System.out.println("RandomBot: " + getName() + " is moving from " + (validMoves.get(randomIndex)[0]) + " to " + (validMoves.get(randomIndex)[1]));
        return validMoves.get(randomIndex);
    }

}