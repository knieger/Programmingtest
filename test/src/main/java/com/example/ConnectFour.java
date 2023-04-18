package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConnectFour represents a game of connect four with a game grid and players.
 */
public class ConnectFour {
    // Number of rows in game grid
    private static final int ROWS = 6;
    // Number of columns in game grid
    private static final int COLUMNS = 7;
    // Number or markers next to each other required to win the game
    private static final int COMBO_LENGTH = 4;
    // RegEx pattern to determine valid input. (e.g. A_Yelloow. C_Red)
    private static final String INPUT_FILTER = "^([A-G])_(Red|Yellow)$";

    // Represeting the valid players
    public enum Player {
        Red, Yellow
    }

    // Represeting the valid players
    public enum GameState {
        Red, Yellow, Ongoing, Draw
    }

    // Represeting the Columns in the game
    public enum Column {
        A, B, C, D, E, F, G
    }

    private final List<Observer> observers;

    private final char[][] gameGrid;
    private final int[] columnHeights;

    public static class InvalidColumnException extends Exception {
        public InvalidColumnException(String message) {
            super(message);
        }
    }

    public static class ColumnFullException extends Exception {
        public ColumnFullException(String message) {
            super(message);
        }
    }

    /**
     * Observer Interface to allow implementation of visual game state
     * representation
     */
    public interface Observer {
        /**
         * Called after every move
         *
         * @param column       The column where a piece was entered (e.g. C)
         * @param player       The player who made the move (e.g. Yellow)
         * @param gameGrid     Current gameState as an array. (e.g. [[Y, R, R], [R, Y,
         *                     R],
         *                     [R, Y, R]])
         * @param gameEndState Ongoing if the game is still running, otherwise the
         *                     winning player, Draw or Ongoing
         */
        void update(int column,
                Player player,
                char[][] gameGrid,
                GameState gameEndState);
    }

    /**
     * Registers new observers
     *
     * @param observer Observer to register.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes registered observer from the observer list.
     *
     * @param observer Observer to be removed.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of last game move, game state and if the
     * game was won.
     *
     * @param column       The column where a piece was entered (e.g. C)
     * @param player       The player who made the move (e.g. Yellow)
     * @param gameGrid     Current gameState as an array. (e.g. [[Y, R, R], [R, Y,
     *                     R],
     *                     [R, Y, R]])
     * @param gameEndState Ongoing if the game is still running, otherwise the
     *                     winning player, Draw or Ongoing
     */
    private void notifyObservers(int column,
            Player player,
            char[][] gameGrid,
            GameState gameEndState) {
        for (Observer observer : observers) {
            observer.update(column,
                    player,
                    gameGrid,
                    gameEndState);
        }
    }

    /**
     * Constructs a new connect four game with empty gameGrid.
     */
    public ConnectFour() {
        this.gameGrid = new char[ROWS][COLUMNS];
        for (char[] row : gameGrid) {
            Arrays.fill(row, ' ');
        }
        this.columnHeights = new int[COLUMNS];
        this.observers = new ArrayList<>();
    }

    /**
     * Executes a move by putting a piece in a column
     *
     * @param column The column where the piece is dropped.
     * @param player The player who drops the piece.
     * @return True if the game was won after the move, false otherwise.
     */
    private boolean dropPiece(int column,
            Player player) throws InvalidColumnException,
            ColumnFullException {
        if (column < 0 || column >= COLUMNS) {
            throw new InvalidColumnException("Tried to drop piece in non existant column: "
                    + Column.values()[column]);
        }
        if (columnHeights[column] == ROWS) {
            throw new ColumnFullException("Tried to drop a piece into already full column: "
                    + Column.values()[column]);
        }
        int row = columnHeights[column];
        gameGrid[row][column] = player.name().charAt(0);
        columnHeights[column]++;
        boolean hasWon = isGameWon(player, row, column);
        return hasWon;
    }

    /**
     * Checks if the game is won by a player after placing a piece in the game grid.
     *
     * @param player  The player who just dropped the piece.
     * @param lastRow The row index where the piece was dropped.
     * @param lastCol The column index where the piece was dropped.
     * @return True if the game is won by the player, false otherwise.
     */
    private boolean isGameWon(Player player, int lastRow, int lastCol) {
        char playerChar = player.name().charAt(0);
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
                { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };
        for (int[] dir : directions) {
            int rowDirection = dir[0];
            int colDirection = dir[1];
            // Start counting from 1 as the current position is already occupied by Player
            int count = 1;
            for (int i = 1; i < COMBO_LENGTH; i++) {
                int newRow = lastRow + i * rowDirection;
                int newCol = lastCol + i * colDirection;
                if (newRow >= 0 && newRow < ROWS
                        && newCol >= 0
                        && newCol < COLUMNS
                        && gameGrid[newRow][newCol] == playerChar) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == COMBO_LENGTH) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines the winner of the game from the list of moves
     *
     * @param moves The list of moves played in the game.
     * @return The winner of the game (Red, Yellow, Draw or Ongoing).
     * @throws IllegalArgumentException If the input move is not valid.
     */
    public GameState findWinner(List<String> moves) {
        // Using RegEx to detemrine valid input
        boolean isGameWon = false;
        Pattern movePattern = Pattern.compile(INPUT_FILTER, Pattern.CASE_INSENSITIVE);
        Player currentPlayer = null;
        int column = -1;
        for (String move : moves) {
            Matcher matcher = movePattern.matcher(move);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(
                        "Invalid input. Input must be Column Iddentifier "
                                + Column.values()[0]
                                + "-"
                                + Column.values()[Column.values().length - 1]
                                + " and player name "
                                + Player.values().toString()
                                + ", but input was: "
                                + move);
            }
            column = matcher.group(1).charAt(0) - 'A';
            currentPlayer = Player.valueOf(matcher.group(2));
            try {
                isGameWon = this.dropPiece(column, currentPlayer);
                if (isGameWon) {
                    break;
                }
            } catch (InvalidColumnException | ColumnFullException e) {
                System.err.println("Error: " + e.getMessage());
                break;
            }
            notifyObservers(column,
                    currentPlayer,
                    gameGrid,
                    GameState.Ongoing);
        }
        if (isGameWon) {
            notifyObservers(column,
                    currentPlayer,
                    gameGrid,
                    GameState.valueOf(currentPlayer.name()));
            return GameState.valueOf(currentPlayer.name());
        } else if (moves.size() == ROWS * COLUMNS) {
            notifyObservers(column,
                    currentPlayer,
                    gameGrid,
                    GameState.Draw);
            return GameState.Draw;
        } else {
            // This might have to change to Ongoing in future, if there is a possibility to
            // continue a game
            notifyObservers(column,
                    currentPlayer,
                    gameGrid,
                    GameState.Draw);
            return GameState.Ongoing;
        }
    }
}