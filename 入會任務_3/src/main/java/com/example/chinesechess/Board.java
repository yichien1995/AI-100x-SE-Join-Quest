package com.example.chinesechess;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int ROWS = 10;
    private static final int COLS = 9;
    private Piece[][] board;
    
    public Board() {
        this.board = new Piece[ROWS][COLS];
    }
    
    public void placePiece(Piece piece) {
        board[piece.getRow() - 1][piece.getCol() - 1] = piece;
    }
    
    public Piece getPiece(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row - 1][col - 1];
        }
        return null;
    }
    
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = getPiece(fromRow, fromCol);
        if (piece != null) {
            board[fromRow - 1][fromCol - 1] = null;
            piece.setPosition(toRow, toCol);
            board[toRow - 1][toCol - 1] = piece;
        }
    }
    
    public boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= ROWS && col >= 1 && col <= COLS;
    }
    
    public List<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] != null) {
                    pieces.add(board[row][col]);
                }
            }
        }
        return pieces;
    }
}



