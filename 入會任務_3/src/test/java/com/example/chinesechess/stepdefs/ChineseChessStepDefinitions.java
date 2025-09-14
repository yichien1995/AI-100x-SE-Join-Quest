package com.example.chinesechess.stepdefs;

import com.example.chinesechess.GameService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import static org.junit.jupiter.api.Assertions.*;

public class ChineseChessStepDefinitions {
    
    private GameService gameService;
    private boolean moveResult;
    private boolean gameOver;
    
    @Given("the board is empty except for a Red General at \\({int}, {int}\\)")
    public void the_board_is_empty_except_for_a_red_general_at(int row, int col) {
        gameService = new GameService();
        gameService.placePiece("Red", "General", row, col);
    }
    
    @Given("the board is empty except for a Red Guard at \\({int}, {int}\\)")
    public void the_board_is_empty_except_for_a_red_guard_at(int row, int col) {
        gameService = new GameService();
        gameService.placePiece("Red", "Guard", row, col);
    }
    
    @Given("the board is empty except for a Red Rook at \\({int}, {int}\\)")
    public void the_board_is_empty_except_for_a_red_rook_at(int row, int col) {
        gameService = new GameService();
        gameService.placePiece("Red", "Rook", row, col);
    }
    
    @Given("the board is empty except for a Red Horse at \\({int}, {int}\\)")
    public void the_board_is_empty_except_for_a_red_horse_at(int row, int col) {
        gameService = new GameService();
        gameService.placePiece("Red", "Horse", row, col);
    }
    
    @Given("the board is empty except for a Red Cannon at \\({int}, {int}\\)")
    public void the_board_is_empty_except_for_a_red_cannon_at(int row, int col) {
        gameService = new GameService();
        gameService.placePiece("Red", "Cannon", row, col);
    }
    
    @Given("the board is empty except for a Red Elephant at \\({int}, {int}\\)")
    public void the_board_is_empty_except_for_a_red_elephant_at(int row, int col) {
        gameService = new GameService();
        gameService.placePiece("Red", "Elephant", row, col);
    }
    
    @Given("the board is empty except for a Red Soldier at \\({int}, {int}\\)")
    public void the_board_is_empty_except_for_a_red_soldier_at(int row, int col) {
        gameService = new GameService();
        gameService.placePiece("Red", "Soldier", row, col);
    }
    
    @Given("the board has:")
    public void the_board_has(DataTable dataTable) {
        gameService = new GameService();
        
        // Parse the data table and place pieces
        dataTable.asMaps().forEach(row -> {
            String piece = row.get("Piece");
            String position = row.get("Position");
            
            // Parse position like "(2, 4)"
            String[] coords = position.replace("(", "").replace(")", "").split(", ");
            int rowNum = Integer.parseInt(coords[0]);
            int colNum = Integer.parseInt(coords[1]);
            
            // Extract color and type from piece name like "Red General"
            String[] parts = piece.split(" ");
            String color = parts[0];
            String type = parts[1];
            
            gameService.placePiece(color, type, rowNum, colNum);
        });
    }
    
    @When("Red moves the General from \\({int}, {int}\\) to \\({int}, {int}\\)")
    public void red_moves_the_general_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = gameService.movePiece("Red", "General", fromRow, fromCol, toRow, toCol);
        gameOver = gameService.isGameOver();
    }
    
    @When("Red moves the Guard from \\({int}, {int}\\) to \\({int}, {int}\\)")
    public void red_moves_the_guard_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = gameService.movePiece("Red", "Guard", fromRow, fromCol, toRow, toCol);
        gameOver = gameService.isGameOver();
    }
    
    @When("Red moves the Rook from \\({int}, {int}\\) to \\({int}, {int}\\)")
    public void red_moves_the_rook_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = gameService.movePiece("Red", "Rook", fromRow, fromCol, toRow, toCol);
        gameOver = gameService.isGameOver();
    }
    
    @When("Red moves the Horse from \\({int}, {int}\\) to \\({int}, {int}\\)")
    public void red_moves_the_horse_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = gameService.movePiece("Red", "Horse", fromRow, fromCol, toRow, toCol);
        gameOver = gameService.isGameOver();
    }
    
    @When("Red moves the Cannon from \\({int}, {int}\\) to \\({int}, {int}\\)")
    public void red_moves_the_cannon_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = gameService.movePiece("Red", "Cannon", fromRow, fromCol, toRow, toCol);
        gameOver = gameService.isGameOver();
    }
    
    @When("Red moves the Elephant from \\({int}, {int}\\) to \\({int}, {int}\\)")
    public void red_moves_the_elephant_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = gameService.movePiece("Red", "Elephant", fromRow, fromCol, toRow, toCol);
        gameOver = gameService.isGameOver();
    }
    
    @When("Red moves the Soldier from \\({int}, {int}\\) to \\({int}, {int}\\)")
    public void red_moves_the_soldier_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = gameService.movePiece("Red", "Soldier", fromRow, fromCol, toRow, toCol);
        gameOver = gameService.isGameOver();
    }
    
    @Then("the move is legal")
    public void the_move_is_legal() {
        assertTrue(moveResult, "Expected move to be legal");
    }
    
    @Then("the move is illegal")
    public void the_move_is_illegal() {
        assertFalse(moveResult, "Expected move to be illegal");
    }
    
    @Then("Red wins immediately")
    public void red_wins_immediately() {
        assertTrue(gameOver, "Expected Red to win immediately");
    }
    
    @Then("the game is not over just from that capture")
    public void the_game_is_not_over_just_from_that_capture() {
        assertFalse(gameOver, "Expected game to continue after capture");
    }
}
