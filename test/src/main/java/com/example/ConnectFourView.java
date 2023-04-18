package com.example;

/**
 * ConnectFourView class implements the Observer interface and printing the
 * winner of a game in the console
 */
public class ConnectFourView implements ConnectFour.Observer {

    /**
     * Updates the game state and includes game state information after each move.
     *
     * @param column       The column where a piece was entered (e.g. C)
     * @param player       The player who made the move (e.g. Yellow)
     * @param gameGrid     Current gameState as an array. (e.g. [[Y, R, R], [R, Y,
     *                     R], [R, Y, R]])
     * @param endGameState Ongoing if the game is still running, otherwise the
     *                     winning player, Draw or NotFinished
     */
    @Override
    public void update(int column,
            ConnectFour.Player player,
            char[][] gameGrid,
            ConnectFour.GameState endGameState) {
        if (endGameState != ConnectFour.GameState.Ongoing) {
            if (endGameState != ConnectFour.GameState.Ongoing) {
                System.out.println(endGameState.name());
            }
        }
    }
}