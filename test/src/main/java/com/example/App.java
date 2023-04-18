package com.example;

import java.util.Arrays;
import java.util.List;

/**
 * Controller for connect four game, initializes a game, adds observers, and
 * provides the game moves.
 */
public final class App {

    private static final List<String> GAME_MOVES = Arrays.asList(
            "A_Red",
            "B_Yellow",
            "A_Red",
            "B_Yellow",
            "A_Red",
            "B_Yellow",
            "G_Red",
            "B_Yellow");

    private App() {
    }

    /**
     * Creates a game of connect four with the input specified in the GAME_MOVES
     * class variable
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        ConnectFour game = new ConnectFour();
        ConnectFourView view = new ConnectFourView();
        game.addObserver(view);
        game.findWinner(GAME_MOVES);
    }
}
