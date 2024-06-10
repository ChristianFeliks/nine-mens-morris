package sk.tuke.gamestudio.game.ninemensmorris;

import sk.tuke.gamestudio.game.ninemensmorris.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.ninemensmorris.core.Field;

public class Ninemensmorris {

    public Ninemensmorris() {
    }

    public static void main(String[] args) {

        Field field = new Field();
        ConsoleUI ui = new ConsoleUI(field);
        ui.play();

    }


}
