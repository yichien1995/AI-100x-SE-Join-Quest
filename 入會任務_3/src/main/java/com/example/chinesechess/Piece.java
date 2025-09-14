package com.example.chinesechess;

public class Piece {
    private String color;
    private String type;
    private int row;
    private int col;
    
    public Piece(String color, String type, int row, int col) {
        this.color = color;
        this.type = type;
        this.row = row;
        this.col = col;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getType() {
        return type;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}



