package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppTest {
    private ConnectFour game;

    @BeforeEach
    public void setUp() {
        game = new ConnectFour();
    }

    @Test
    public void testFindWinnerYellow() {
        List<String> moves = Arrays.asList(
                "A_Red",
                "B_Yellow",
                "A_Red",
                "B_Yellow",
                "A_Red",
                "B_Yellow",
                "G_Red",
                "B_Yellow");
        assertEquals(ConnectFour.GameState.Yellow, game.findWinner(moves));
    }

    @Test
    public void testFindWinnerRed() {
        List<String> moves = Arrays.asList(
                "A_Red",
                "B_Yellow",
                "A_Red",
                "B_Yellow",
                "A_Red",
                "B_Yellow",
                "A_Red");
        assertEquals(ConnectFour.GameState.Red, game.findWinner(moves));
    }

    @Test
    public void testFindWinnerDraw() {
        List<String> moves = Arrays.asList(
                "A_Red", "B_Yellow", "A_Red", "B_Yellow", "B_Red", "A_Yellow", "B_Red",
                "A_Yellow", "A_Red", "B_Yellow", "A_Red", "B_Yellow", "C_Red", "D_Yellow",
                "C_Red", "D_Yellow", "D_Red", "C_Yellow", "D_Red", "C_Yellow", "C_Red",
                "D_Yellow", "C_Red", "D_Yellow", "E_Red", "F_Yellow", "E_Red", "F_Yellow",
                "F_Red", "E_Yellow", "F_Red", "E_Yellow", "E_Red", "F_Yellow", "E_Red",
                "F_Yellow", "G_Red", "G_Yellow", "G_Red", "G_Yellow", "G_Red", "G_Yellow");
        assertEquals(ConnectFour.GameState.Draw, game.findWinner(moves));
    }

    @Test
    public void testInvalidMoveFormat() {
        List<String> moves = Arrays.asList(
                "A_Red",
                "B_Yellow",
                "A_Red",
                "B_Yellow",
                "A_Red",
                "B_Yellow",
                "G_Red",
                ".MSG");
        assertThrows(IllegalArgumentException.class, () -> game.findWinner(moves));
    }

    @Test
    public void testNotFinished() {
        List<String> moves = Arrays.asList(
                "A_Red",
                "B_Yellow");
        assertEquals(ConnectFour.GameState.Ongoing, game.findWinner(moves));
    }
}