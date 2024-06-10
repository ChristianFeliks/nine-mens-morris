package sk.tuke.gamestudio.game.ninemensmorris.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tile implements Cloneable<Tile>{
    private int index;
    private TileState state;
    private Player playerOccupying;
    private List<Integer> adjacentTilesIndexes;

    private int drawPosX;
    private int drawPosY;

    public int getDrawPosX() {
        return drawPosX;
    }

    public void setDrawPosX(int drawPosX) {
        this.drawPosX = drawPosX;
    }

    public int getDrawPosY() {
        return drawPosY;
    }

    public void setDrawPosY(int drawPosY) {
        this.drawPosY = drawPosY;
    }


    public Tile (int index) {
        this.index = index;
        this.state = TileState.UNOCCUPIED;
        this.playerOccupying = null;
        this.adjacentTilesIndexes = new ArrayList<>();
    }
    public Tile(Tile tile){
        this.index = tile.index;
        this.state = tile.state;
        this.playerOccupying = tile.playerOccupying; // assuming playerOccupying is immutable or has a copy constructor
        this.adjacentTilesIndexes = new ArrayList<>(tile.adjacentTilesIndexes);
    }

    public int getIndex() {
        return index;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public Player getPlayerOccupying() {
        return playerOccupying;
    }

    public void setPlayerOccupying(Player playerOccupying) {
        this.playerOccupying = playerOccupying;
        if (this.playerOccupying != null) this.setState(TileState.OCCUPIED);
    }

    public List<Integer> getAdjacentTilesIndexes() {
        return adjacentTilesIndexes;
    }

    public void setAdjacentTilesIndexes(int pos1, int pos2, int pos3, int pos4) {
        this.adjacentTilesIndexes.clear();
        this.adjacentTilesIndexes.add(pos1);
        this.adjacentTilesIndexes.add(pos2);
        this.adjacentTilesIndexes.add(pos3);
        this.adjacentTilesIndexes.add(pos4);
    }

    public void setAdjacentTilesIndexes(int pos1, int pos2, int pos3) {
        this.adjacentTilesIndexes.clear();
        this.adjacentTilesIndexes.add(pos1);
        this.adjacentTilesIndexes.add(pos2);
        this.adjacentTilesIndexes.add(pos3);
    }

    public void setAdjacentTilesIndexes(int pos1, int pos2) {
        this.adjacentTilesIndexes.clear();
        this.adjacentTilesIndexes.add(pos1);
        this.adjacentTilesIndexes.add(pos2);
    }
    public boolean isAdjacent(Tile tile){
        List<Integer> adjacentTiles = tile.getAdjacentTilesIndexes();
        //System.out.println("adjacent length: "+adjacentTiles.size());
        /*for (int i = 0; i < adjacentTiles.size(); i++) {
            System.out.println(adjacentTiles.get(i));
        }*/
        for (int i = 0; i < adjacentTiles.size(); i++) {
            if (adjacentTiles.get(i) == this.getIndex()) {
                return true;
            }
        }
        return false;
    }
    @Override
    public Tile clone(){return new Tile(this);}
}
