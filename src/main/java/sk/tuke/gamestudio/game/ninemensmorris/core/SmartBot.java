package sk.tuke.gamestudio.game.ninemensmorris.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartBot extends Bot {

    private Random random;

    public SmartBot(String name, String symbol, Player opponent) {
        super(name, symbol, opponent);
        this.random = new Random();
    }

    @Override
    public int getIndexToPlacePiece(Field gameBoard) {
        List<Integer> occupiedTiles = getOccupiedTiles(gameBoard);
        List<Integer> unoccupiedAdjacentTiles = getUnoccupiedAdjacentTiles(gameBoard, occupiedTiles);

        // mill-forming move strategy
        if (getIndexToFormMill(gameBoard) != -1) {
            //System.out.println("Mill-forming move found. Placing piece on the tile.");
            return getIndexToFormMill(gameBoard) + 1;
        }

        // block strategy
        if (getIndexToBlockPlacing(gameBoard) != -1) {
            //System.out.println("Opponent's Mill-forming move found. Blocking the tile.");
            return getIndexToBlockPlacing(gameBoard) + 1;
        }

        // place piece adjacent strategy
        if (!unoccupiedAdjacentTiles.isEmpty()) {
            int randomIndex = random.nextInt(unoccupiedAdjacentTiles.size());
            //System.out.println("Placing piece on unoccupied adjacent tile" + (unoccupiedAdjacentTiles.get(randomIndex) + 1));
            return unoccupiedAdjacentTiles.get(randomIndex) + 1;
        }

        // random strategy
        List<Integer> availableTiles = gameBoard.getAvailableTiles();
        if (!availableTiles.isEmpty()) {
            int randomIndex = random.nextInt(availableTiles.size());
            //System.out.println("Placing piece on random tile " + (availableTiles.get(randomIndex) + 1));
            return availableTiles.get(randomIndex) + 1;
        }
        return -1;
    }
    private List<Integer> getOccupiedTiles(Field gameBoard) {
        List<Integer> occupiedTiles = new ArrayList<>();
        for (int i = 0; i < gameBoard.NUM_OF_POSITIONS; i++) {
            if (gameBoard.getFieldPositions()[i].getPlayerOccupying() == this) {
                occupiedTiles.add(i);
            }
        }
        return occupiedTiles;
    }

    private List<Integer> getUnoccupiedAdjacentTiles(Field gameBoard, List<Integer> occupiedTiles) {
        List<Integer> unoccupiedAdjacentTiles = new ArrayList<>();
        for (Integer occupiedTile : occupiedTiles) {
            List<Integer> adjacentTilesIndexes = gameBoard.getFieldPositions()[occupiedTile].getAdjacentTilesIndexes();
            for (int adjacentTileIndex : adjacentTilesIndexes) {
                if (adjacentTileIndex != -1 && gameBoard.getFieldPositions()[adjacentTileIndex].getState() == TileState.UNOCCUPIED) {
                    unoccupiedAdjacentTiles.add(adjacentTileIndex);
                }
            }
        }
        return unoccupiedAdjacentTiles;
    }

    /**
     * This method is used to determine the index of the opponent's piece that the bot should remove.
     * The bot will first try to remove a piece that is part of a potential mill (two pieces of the opponent in a row).
     * If no such piece is found, the bot will fall back to removing a random piece of the opponent.
     */
    @Override
    public int getIndexToRemovePieceOfOpponent(Field gameBoard) {
        List<Integer> opponentTiles = gameBoard.getOpponentTiles(super.getSymbol());

        for (Integer tileIndex : opponentTiles) {
            Tile tile = gameBoard.getFieldPositions()[tileIndex];
            for (int i = 0; i < gameBoard.NUM_OF_MILL_COMBINATIONS; i++) {
                Tile[] millCombination = gameBoard.getMillCombinations()[i];
                if (millCombination[0] == tile || millCombination[1] == tile || millCombination[2] == tile) {
                    int occupiedCount = 0;
                    for (Tile millTile : millCombination) {
                        if (millTile.getState() == TileState.OCCUPIED && millTile.getPlayerOccupying() == opponent) {
                            occupiedCount++;
                        }
                    }
                    if (occupiedCount == 2) {
                        return tileIndex + 1;
                    }
                }
            }
        }
        // fall back to the random strategy
        if (!opponentTiles.isEmpty()) {
            int randomIndex = random.nextInt(opponentTiles.size());

            return opponentTiles.get(randomIndex) + 1;
        }
        return -1;
    }

    @Override
    public int[] getMove(Field gameBoard) {
        List<int[]> validMoves = getValidMoves(gameBoard);
        if (validMoves.isEmpty()) {
            return null;
        }
        // mill-forming move strategy
        for (int[] move : validMoves) {
            Field simulatedGameBoard = gameBoard.clone();
            simulatedGameBoard.movePieceSimulated(move[0]-1, move[1]-1, this);

            if (simulatedGameBoard.wasMillCreated(move[1] - 1, this)) {
                return new int[]{move[0], move[1]};
            }
        }
        // blocking strategy
        if (getTileIndexToBlock(gameBoard)[0] != -1) {
            System.out.println("Opponent's Mill-forming move found. Blocking the tile.");
            return new int[]{getTileIndexToBlock(gameBoard)[0], getTileIndexToBlock(gameBoard)[1]};
        }
        // fall back to the random strategy
        int[] randomMove = validMoves.get(random.nextInt(validMoves.size()));
        return new int[]{randomMove[0], randomMove[1]};
    }


    private List<int[]> getDangerousMoves(Field gameBoard, Player opponent) {
        List<int[]> dangerousMoves = new ArrayList<>();
        List<int[]> opponentMoves = getValidMovesOfOpponent(gameBoard);

        for (int[] move : opponentMoves) {
            Field simulatedGameBoard = gameBoard.clone();
            simulatedGameBoard.movePieceSimulated(move[0]-1, move[1]-1, opponent);

            if (simulatedGameBoard.wasMillCreated(move[1]-1, opponent)) {
                move[0] -= 1;
                move[1] -= 1;
                dangerousMoves.add(move);
            }
        }

        return dangerousMoves;
    }

    public int[] getTileIndexToBlock(Field gameBoard) {
        List<int[]> dangerousMoves = getDangerousMoves(gameBoard, opponent);

        if (!dangerousMoves.isEmpty()) {
            List<Integer> botTiles = gameBoard.getTiles(this.getSymbol());

            for (int[] dangerousMove : dangerousMoves) {
                for (Integer botTile : botTiles) {
                    List<Integer> adjacentTilesIndexes = gameBoard.getFieldPositions()[botTile].getAdjacentTilesIndexes();
                    for (int adjacentTileIndex : adjacentTilesIndexes) {
                        if (adjacentTileIndex == dangerousMove[1]) {
                            return new int[]{botTile, dangerousMove[1]};
                        }
                    }
                }
            }
        }
        return new int[]{-1, -1};
    }

    public int getIndexToBlockPlacing(Field gameBoard) {
        List<Integer> availableTiles = gameBoard.getAvailableTiles();
        for (Integer availableTile : availableTiles) {
            Field simulatedGameBoard = gameBoard.clone();
            simulatedGameBoard.placePieceSimulated(availableTile, opponent);

            if (simulatedGameBoard.wasMillCreated(availableTile, opponent)) {
                return availableTile;
            }
        }
        // If no dangerous tiles are found
        return -1;
    }
    public int getIndexToFormMill(Field gameBoard) {
    List<Integer> availableTiles = gameBoard.getAvailableTiles();
    for (Integer availableTile : availableTiles) {
        Field simulatedGameBoard = gameBoard.clone();
        simulatedGameBoard.placePieceSimulated(availableTile, this);

        if (simulatedGameBoard.wasMillCreated(availableTile, this)) {
            return availableTile;
        }
    }
    // If no mill-forming tiles are found
    return -1;
}
}

