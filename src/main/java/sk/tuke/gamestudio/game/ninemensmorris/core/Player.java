package sk.tuke.gamestudio.game.ninemensmorris.core;


public class Player{

    public Player(String name, String symbol) {
        this.state = PlayerState.PLACING;
        this.name = name;
        this.symbol = symbol;
        numberOfPiecesPlaced = 0;
    }

    private String name;
    private PlayerState state;
    private String symbol;
    private int numberOfPiecesLeftToPlace = 9;
    private int numberOfPiecesPlaced;

    public String getName() {
        return name;
    }
    public String getSymbol() {
        return symbol;
    }

    public boolean canMove() {
        if (state == PlayerState.MOVING) {
            return true;
        }
        return false;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public int getNumberOfPiecesLeftToPlace() {
        return numberOfPiecesLeftToPlace;
    }

    public void setNumberOfPiecesLeftToPlace(int numberOfPiecesLeftToPlace) {
        this.numberOfPiecesLeftToPlace = numberOfPiecesLeftToPlace;
    }

    public void incrementNumberOfPiecesLeftToPlace() {
        this.numberOfPiecesLeftToPlace++;
    }

    public void decrementNumberOfPiecesLeftToPlace() {
        if (this.numberOfPiecesLeftToPlace > 0) {
            this.numberOfPiecesLeftToPlace--;
        }
    }

    public int getNumberOfPiecesPlaced() {
        return numberOfPiecesPlaced;
    }

    public void setNumberOfPiecesPlaced(int numberOfPiecesPlaced) {
        this.numberOfPiecesPlaced = numberOfPiecesPlaced;
    }

    public void incrementNumberOfPiecesPlaced() {
        this.numberOfPiecesPlaced++;
    }

    public void decrementNumberOfPiecesPlaced() {
        if (this.numberOfPiecesPlaced > 0) {
            this.numberOfPiecesPlaced--;
        }
    }

}
