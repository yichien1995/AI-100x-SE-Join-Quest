package com.example.chinesechess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneralMoveTest {
    
    @Test
    public void testGeneralMoveWithinPalace() {
        GameService gameService = new GameService();
        gameService.placePiece("Red", "General", 1, 5);
        
        boolean result = gameService.movePiece("Red", "General", 1, 5, 1, 4);
        assertTrue(result, "General should be able to move within palace");
    }
    
    @Test
    public void testGeneralMoveOutsidePalace() {
        GameService gameService = new GameService();
        gameService.placePiece("Red", "General", 1, 6);
        
        boolean result = gameService.movePiece("Red", "General", 1, 6, 1, 7);
        assertFalse(result, "General should not be able to move outside palace");
    }
    
    @Test
    public void testGeneralsFaceEachOther() {
        GameService gameService = new GameService();
        gameService.placePiece("Red", "General", 2, 4);
        gameService.placePiece("Black", "General", 8, 5);
        
        boolean result = gameService.movePiece("Red", "General", 2, 4, 2, 5);
        assertFalse(result, "General should not be able to move to face the opponent's general");
    }
}



