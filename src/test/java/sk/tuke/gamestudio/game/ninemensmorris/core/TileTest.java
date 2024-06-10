package sk.tuke.gamestudio.game.ninemensmorris.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.ninemensmorris.core.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TileTest {

    private Field field;
    @BeforeEach
    public void setUp() {
        field = new Field();
    }

    @Test
    public void isAdjacent_returnsTrueIfTilesAreAdjacent() {
        assertTrue(field.getFieldPositionByIndex(0).isAdjacent(field.getFieldPositionByIndex(1)));
    }
    @Test
    public void isAdjacent_returnsFalseIfTilesAreNotAdjacent() {
        assertFalse(field.getFieldPositionByIndex(0).isAdjacent(field.getFieldPositionByIndex(10)));
    }
}
