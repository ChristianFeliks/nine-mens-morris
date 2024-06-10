package sk.tuke.gamestudio.game.ninemensmorris.core;

import java.util.ArrayList;
import java.util.List;

public class Field implements Cloneable<Field> {
    public final int NUM_OF_POSITIONS = 24;
    public final int NUM_OF_MILL_COMBINATIONS = 16;
    private int totalNumOfPiecesPlacedOnBoard;
    private Tile[][] millCombinations;
    private Tile[] fieldPositions;

    public Field(){
        this.totalNumOfPiecesPlacedOnBoard = 0;
        this.millCombinations = new Tile[NUM_OF_MILL_COMBINATIONS][3];
        this.fieldPositions = new Tile[NUM_OF_POSITIONS];
        initializeField();
        initializeMillCombinations();
        initializeDrawPos();
    }
    public Field(Field field){
        this.totalNumOfPiecesPlacedOnBoard = field.totalNumOfPiecesPlacedOnBoard;
        this.millCombinations = new Tile[NUM_OF_MILL_COMBINATIONS][3];
        this.fieldPositions = new Tile[NUM_OF_POSITIONS];

        for (int i = 0; i < NUM_OF_POSITIONS; i++) {
            this.fieldPositions[i] = field.fieldPositions[i].clone();
        }

        for (int i = 0; i < NUM_OF_MILL_COMBINATIONS; i++) {
            for (int j = 0; j < 3; j++) {
                int index = field.millCombinations[i][j].getIndex();
                this.millCombinations[i][j] = this.fieldPositions[index];
            }
        }
    }

    private void initializeField(){
        for (int i = 0; i < NUM_OF_POSITIONS; i++) {
            fieldPositions[i] = new Tile(i);
        }

        fieldPositions[0].setAdjacentTilesIndexes(1,9);
        fieldPositions[1].setAdjacentTilesIndexes(0,2,4);
        fieldPositions[2].setAdjacentTilesIndexes(1,14);
        fieldPositions[9].setAdjacentTilesIndexes(0,10,21);
        fieldPositions[14].setAdjacentTilesIndexes(2,13,23);
        fieldPositions[21].setAdjacentTilesIndexes(9,22);
        fieldPositions[22].setAdjacentTilesIndexes(19,21,23);
        fieldPositions[23].setAdjacentTilesIndexes(14,22);

        fieldPositions[3].setAdjacentTilesIndexes(4,10);
        fieldPositions[4].setAdjacentTilesIndexes(1,3,5,7);
        fieldPositions[5].setAdjacentTilesIndexes(4,13);
        fieldPositions[10].setAdjacentTilesIndexes(3,9,11,18);
        fieldPositions[13].setAdjacentTilesIndexes(5,12,14,20);
        fieldPositions[18].setAdjacentTilesIndexes(10,19);
        fieldPositions[19].setAdjacentTilesIndexes(16,18,20,22);
        fieldPositions[20].setAdjacentTilesIndexes(13,19);

        fieldPositions[6].setAdjacentTilesIndexes(7,11);
        fieldPositions[7].setAdjacentTilesIndexes(4,6,8);
        fieldPositions[8].setAdjacentTilesIndexes(7,12);
        fieldPositions[11].setAdjacentTilesIndexes(6,10,15);
        fieldPositions[12].setAdjacentTilesIndexes(8,13,17);
        fieldPositions[15].setAdjacentTilesIndexes(11,16);
        fieldPositions[16].setAdjacentTilesIndexes(15,17,19);
        fieldPositions[17].setAdjacentTilesIndexes(12,16);
    }

    private void initializeMillCombinations(){

        millCombinations[0][0] = fieldPositions[0];
        millCombinations[0][1] = fieldPositions[1];
        millCombinations[0][2] = fieldPositions[2];
        millCombinations[1][0] = fieldPositions[0];
        millCombinations[1][1] = fieldPositions[9];
        millCombinations[1][2] = fieldPositions[21];
        millCombinations[2][0] = fieldPositions[2];
        millCombinations[2][1] = fieldPositions[14];
        millCombinations[2][2] = fieldPositions[23];
        millCombinations[3][0] = fieldPositions[21];
        millCombinations[3][1] = fieldPositions[22];
        millCombinations[3][2] = fieldPositions[23];

        millCombinations[4][0] = fieldPositions[3];
        millCombinations[4][1] = fieldPositions[4];
        millCombinations[4][2] = fieldPositions[5];
        millCombinations[5][0] = fieldPositions[3];
        millCombinations[5][1] = fieldPositions[10];
        millCombinations[5][2] = fieldPositions[18];
        millCombinations[6][0] = fieldPositions[5];
        millCombinations[6][1] = fieldPositions[13];
        millCombinations[6][2] = fieldPositions[20];
        millCombinations[7][0] = fieldPositions[18];
        millCombinations[7][1] = fieldPositions[19];
        millCombinations[7][2] = fieldPositions[20];

        millCombinations[8][0] = fieldPositions[6];
        millCombinations[8][1] = fieldPositions[7];
        millCombinations[8][2] = fieldPositions[8];
        millCombinations[9][0] = fieldPositions[6];
        millCombinations[9][1] = fieldPositions[11];
        millCombinations[9][2] = fieldPositions[15];
        millCombinations[10][0] = fieldPositions[8];
        millCombinations[10][1] = fieldPositions[12];
        millCombinations[10][2] = fieldPositions[17];
        millCombinations[11][0] = fieldPositions[15];
        millCombinations[11][1] = fieldPositions[16];
        millCombinations[11][2] = fieldPositions[17];

        millCombinations[12][0] = fieldPositions[1];
        millCombinations[12][1] = fieldPositions[4];
        millCombinations[12][2] = fieldPositions[7];
        millCombinations[13][0] = fieldPositions[9];
        millCombinations[13][1] = fieldPositions[10];
        millCombinations[13][2] = fieldPositions[11];
        millCombinations[14][0] = fieldPositions[12];
        millCombinations[14][1] = fieldPositions[13];
        millCombinations[14][2] = fieldPositions[14];
        millCombinations[15][0] = fieldPositions[16];
        millCombinations[15][1] = fieldPositions[19];
        millCombinations[15][2] = fieldPositions[22];
    }

    private void initializeDrawPos() {
        fieldPositions[0].setDrawPosX(44);
        fieldPositions[0].setDrawPosY(44);

        fieldPositions[1].setDrawPosX(256);
        fieldPositions[1].setDrawPosY(44);

        fieldPositions[2].setDrawPosX(469);
        fieldPositions[2].setDrawPosY(44);

        fieldPositions[9].setDrawPosX(44);
        fieldPositions[9].setDrawPosY(256);

        fieldPositions[21].setDrawPosX(44);
        fieldPositions[21].setDrawPosY(469);

        fieldPositions[22].setDrawPosX(256);
        fieldPositions[22].setDrawPosY(469);

        fieldPositions[23].setDrawPosX(469);
        fieldPositions[23].setDrawPosY(469);

        fieldPositions[14].setDrawPosX(469);
        fieldPositions[14].setDrawPosY(256);



        fieldPositions[3].setDrawPosX(116);
        fieldPositions[3].setDrawPosY(116);

        fieldPositions[4].setDrawPosX(256);
        fieldPositions[4].setDrawPosY(116);

        fieldPositions[5].setDrawPosX(397);
        fieldPositions[5].setDrawPosY(116);

        fieldPositions[10].setDrawPosX(116);
        fieldPositions[10].setDrawPosY(256);

        fieldPositions[18].setDrawPosX(116);
        fieldPositions[18].setDrawPosY(397);

        fieldPositions[19].setDrawPosX(256);
        fieldPositions[19].setDrawPosY(397);

        fieldPositions[20].setDrawPosX(397);
        fieldPositions[20].setDrawPosY(397);

        fieldPositions[13].setDrawPosX(397);
        fieldPositions[13].setDrawPosY(256);



        fieldPositions[6].setDrawPosX(188);
        fieldPositions[6].setDrawPosY(188);

        fieldPositions[7].setDrawPosX(256);
        fieldPositions[7].setDrawPosY(188);

        fieldPositions[8].setDrawPosX(324);
        fieldPositions[8].setDrawPosY(188);

        fieldPositions[11].setDrawPosX(188);
        fieldPositions[11].setDrawPosY(256);

        fieldPositions[15].setDrawPosX(188);
        fieldPositions[15].setDrawPosY(324);

        fieldPositions[16].setDrawPosX(256);
        fieldPositions[16].setDrawPosY(324);

        fieldPositions[17].setDrawPosX(324);
        fieldPositions[17].setDrawPosY(324);

        fieldPositions[12].setDrawPosX(324);
        fieldPositions[12].setDrawPosY(256);
    }

    public void movePiece(int from, int to, Player playerPlacingPiece){
        if (isMoveValid(from, to, playerPlacingPiece)) {
            removePiece(from);
            // only moving so no need to substract the number of PiecesLeftToPlace
            playerPlacingPiece.incrementNumberOfPiecesLeftToPlace();
            placePiece(to, playerPlacingPiece);
        } // when SmartBot tries different moves when searching for dangerous moves, this would always print invalid move
        /*else {
            System.out.println("Move is invalid");
        }*/
    }

    public void placePiece(int to, Player playerPlacingPiece){
        //TODO check if valid and if player is in placing phase

        Tile tileTo = getFieldPositionByIndex(to);
        if (tileTo.getState() == TileState.OCCUPIED) {
            //throw new IllegalArgumentException("Field is already occupied");
            System.out.println("Field is already occupied try again");
        }

        tileTo.setState(TileState.OCCUPIED);
        tileTo.setPlayerOccupying(playerPlacingPiece);
        /*if (wasMillCreated(to, playerPlacingPiece)){
            System.out.println("Mill was created");
        }*/

        totalNumOfPiecesPlacedOnBoard++;
        playerPlacingPiece.decrementNumberOfPiecesLeftToPlace();
        playerPlacingPiece.incrementNumberOfPiecesPlaced();
    }
    public void placePieceSimulated(int to, Player playerPlacingPiece){
        Tile tileTo = getFieldPositionByIndex(to);
        tileTo.setState(TileState.OCCUPIED);
        tileTo.setPlayerOccupying(playerPlacingPiece);
    }
    public void movePieceSimulated(int from, int to, Player playerPlacingPiece){
        removePieceSimulated(from);
        placePieceSimulated(to, playerPlacingPiece);
    }
    public void removePieceSimulated(int from){
        Tile tileFrom = getFieldPositionByIndex(from);
        if (tileFrom.getState() == TileState.OCCUPIED) {
            tileFrom.setState(TileState.UNOCCUPIED);
            tileFrom.setPlayerOccupying(null);
        }
    }


    public void removePiece(int from){

        Tile tileFrom = getFieldPositionByIndex(from);
        Player playerLosingPiece = tileFrom.getPlayerOccupying();

        if (tileFrom.getState() == TileState.OCCUPIED) {
            playerLosingPiece.decrementNumberOfPiecesPlaced();
            totalNumOfPiecesPlacedOnBoard--;
        }

        tileFrom.setState(TileState.UNOCCUPIED);
        tileFrom.setPlayerOccupying(null);

        //System.out.println("After removal: " + tileFrom.getPlayerOccupying() + " " + tileFrom.getState() + " " + tileFrom.getIndex());

    }

    public boolean wasMillCreated(int position, Player playerPlacingPiece){
        Tile tile = getFieldPositionByIndex(position);
        for(int i = 0; i < NUM_OF_MILL_COMBINATIONS; i++) {
            Tile[] millCombination = getMillCombination(i);
            for (int j = 0; j < 3; j++) {
                if (millCombination[j].getIndex() == tile.getIndex()) {
                    //TODO refactor to separate methods
                    if (millCombination[0].getPlayerOccupying() == playerPlacingPiece &&
                            millCombination[1].getPlayerOccupying() == playerPlacingPiece &&
                            millCombination[2].getPlayerOccupying() == playerPlacingPiece) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public boolean isMoveValid(int from, int to, Player playerMakingMove){
        Tile tileFrom = getFieldPositionByIndex(from);
        Tile tileTo = getFieldPositionByIndex(to);
        //System.out.println("From: " + from + " To: " + to + " Player: " + playerMakingMove.getSymbol() + " " + tileFrom.getState() + " " + tileTo.getState() + " " + playerMakingMove + " " + tileFrom.getPlayerOccupying() + " "  + tileFrom.isAdjacent(tileTo));
        if(tileFrom.getState() == TileState.OCCUPIED &&
                tileTo.getState() == TileState.UNOCCUPIED &&
                tileFrom.getPlayerOccupying() == playerMakingMove &&
                tileFrom.isAdjacent(tileTo)) {
            return true;
        }
        return false;
    }

    public boolean isMoveValid(int to, Player playerMakingMove){
        Tile tileTo = getFieldPositionByIndex(to);
        //System.out.println("Player state: " + playerMakingMove.getState() + " Tile state: " + tileTo.getState() + " Number of pieces left to place: " + playerMakingMove.getNumberOfPiecesLeftToPlace());
        if(playerMakingMove.getState() == PlayerState.PLACING && tileTo.getState() == TileState.UNOCCUPIED && playerMakingMove.getNumberOfPiecesLeftToPlace() > 0){
            return true;
        } else if (playerMakingMove.getState() == PlayerState.REMOVING && tileTo.getState() == TileState.OCCUPIED && tileTo.getPlayerOccupying() != playerMakingMove){
            return true;
        }
        return false;
    }

    public boolean hasLegalMove(Player player) {
        for (int from = 0; from < NUM_OF_POSITIONS; from++) {
            Tile fromTile = getFieldPositionByIndex(from);
            if (fromTile.getState() == TileState.OCCUPIED && fromTile.getPlayerOccupying() == player) {
                for (int to = 0; to < NUM_OF_POSITIONS; to++) {
                    if (isMoveValid(from, to, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getTotalNumOfPiecesPlacedOnBoard() {
        return totalNumOfPiecesPlacedOnBoard;
    }

    public Tile[][] getMillCombinations() {
        return millCombinations;
    }

    public Tile[] getFieldPositions() {
        return fieldPositions;
    }
    public Tile getFieldPositionByIndex(int index) {
        return fieldPositions[index];
    }

    public void setTotalNumOfPiecesPlacedOnBoard(int totalNumOfPiecesPlacedOnBoard) {
        this.totalNumOfPiecesPlacedOnBoard = totalNumOfPiecesPlacedOnBoard;
    }
    private Tile[] getMillCombination(int index){
        return millCombinations[index];
    }

    public void printField() {
        System.out.println("1   "+showPos(0)+" - - - - - "+showPos(1)+" - - - - - "+showPos(2)+"   3");
        System.out.println("    |           |           |");
        System.out.println("4   |     "+showPos(3)+" - - "+showPos(4)+" - - "+showPos(5)+"     |   6");
        System.out.println("    |     |     |     |     |");
        System.out.println("7   |     | "+showPos(6)+" - "+showPos(7)+" - "+showPos(8)+" |     |   9");
        System.out.println("    |     | |       | |     |");
        System.out.println("10  "+showPos(9)+" - - "+showPos(10)+"-"+showPos(11)+" 12 13 "+showPos(12)+"-"+showPos(13)+" - - "+showPos(14) + "   15");
        System.out.println("    |     | |       | |     |");
        System.out.println("16  |     | "+showPos(15)+" - "+showPos(16)+" - "+showPos(17)+" |     |   18" );
        System.out.println("    |     |     |     |     |");
        System.out.println("19  |     "+showPos(18)+" - - "+showPos(19)+" - - "+showPos(20)+"     |   21");
        System.out.println("    |           |           |");
        System.out.println("22  "+showPos(21)+" - - - - - "+showPos(22)+" - - - - - "+showPos(23)+"   24");
    }

    private String showPos(int i) {
        Player player = fieldPositions[i].getPlayerOccupying();
        if (player != null) {
            return player.getSymbol();
        } else {
            return  "*";
        }
    }

    public List<Integer> getAvailableTiles() {
        List<Integer> availableTiles = new ArrayList<>();
        for (int i = 0; i < fieldPositions.length; i++) {
            if (fieldPositions[i].getState() == TileState.UNOCCUPIED) {
                availableTiles.add(i);
            }
        }
        return availableTiles;
    }

    /**
     * This method is used to get the tiles occupied by the opponent player.
     *
     * @param playerSymbol The symbol of the current player. This is used to identify the opponent's tiles.
     * @return A list of integers representing the indices of the tiles occupied by the opponent.
     */
    public List<Integer> getOpponentTiles(String playerSymbol) {
        List<Integer> opponentTiles = new ArrayList<>();
        for (int i = 0; i < NUM_OF_POSITIONS; i++) {
            if (fieldPositions[i].getState() == TileState.OCCUPIED && !fieldPositions[i].getPlayerOccupying().getSymbol().equals(playerSymbol)) {
                opponentTiles.add(i);
            }
        }
        return opponentTiles;
    }
    public List<Integer> getTiles(String playerSymbol) {
        List<Integer> opponentTiles = new ArrayList<>();
        for (int i = 0; i < fieldPositions.length; i++) {
            if (fieldPositions[i].getState() == TileState.OCCUPIED && fieldPositions[i].getPlayerOccupying().getSymbol().equals(playerSymbol)) {
                opponentTiles.add(i);
            }
        }
        return opponentTiles;
    }
    @Override
    public Field clone(){return new Field(this);}

    public boolean hasPlayerLost(Player currentPlayer) {
        return (currentPlayer.getNumberOfPiecesLeftToPlace() == 0 && currentPlayer.getNumberOfPiecesPlaced() < 3)
                || (this.hasLegalMove(currentPlayer) == false && currentPlayer.getState() == PlayerState.MOVING);
    }

}
