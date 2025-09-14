package com.example.chinesechess;

public class GameService {
    private Board board;
    private boolean gameOver;
    
    public GameService() {
        this.board = new Board();
        this.gameOver = false;
    }
    
    public void placePiece(String color, String pieceType, int row, int col) {
        Piece piece = new Piece(color, pieceType, row, col);
        board.placePiece(piece);
    }
    
    public boolean movePiece(String color, String pieceType, int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);
        
        // Check if piece exists and belongs to the correct color and type
        if (piece == null || !piece.getColor().equals(color) || !piece.getType().equals(pieceType)) {
            return false;
        }
        
        boolean isValidMove = false;
        
        // Validate the move based on piece type
        if (pieceType.equals("General")) {
            isValidMove = isValidGeneralMove(piece, fromRow, fromCol, toRow, toCol);
        } else if (pieceType.equals("Guard")) {
            isValidMove = isValidGuardMove(piece, fromRow, fromCol, toRow, toCol);
        } else if (pieceType.equals("Rook")) {
            isValidMove = isValidRookMove(piece, fromRow, fromCol, toRow, toCol);
        } else if (pieceType.equals("Horse")) {
            isValidMove = isValidHorseMove(piece, fromRow, fromCol, toRow, toCol);
        } else if (pieceType.equals("Cannon")) {
            isValidMove = isValidCannonMove(piece, fromRow, fromCol, toRow, toCol);
        } else if (pieceType.equals("Elephant")) {
            isValidMove = isValidElephantMove(piece, fromRow, fromCol, toRow, toCol);
        } else if (pieceType.equals("Soldier")) {
            isValidMove = isValidSoldierMove(piece, fromRow, fromCol, toRow, toCol);
        }
        
        if (isValidMove) {
            // Check if this move captures a General before moving
            Piece capturedPiece = board.getPiece(toRow, toCol);
            if (capturedPiece != null && capturedPiece.getType().equals("General")) {
                gameOver = true;
            }
            
            // Execute the move
            board.movePiece(fromRow, fromCol, toRow, toCol);
        }
        
        return isValidMove;
    }
    
    private boolean isValidGeneralMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        return isValidGeneralStep(fromRow, fromCol, toRow, toCol) &&
               isWithinPalace(piece.getColor(), toRow, toCol) &&
               !wouldGeneralsFaceEachOtherAfterMove(fromRow, fromCol, toRow, toCol);
    }
    
    private boolean isValidGeneralStep(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        return rowDiff + colDiff == 1; // Must move exactly one step
    }
    
    private boolean isWithinPalace(String color, int row, int col) {
        if (color.equals("Red")) {
            // Red General's palace: rows 1-3, cols 4-6
            return row >= 1 && row <= 3 && col >= 4 && col <= 6;
        } else {
            // Black General's palace: rows 8-10, cols 4-6
            return row >= 8 && row <= 10 && col >= 4 && col <= 6;
        }
    }
    
    private boolean isValidGuardMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        return isValidGuardStep(fromRow, fromCol, toRow, toCol) &&
               isWithinPalace(piece.getColor(), toRow, toCol);
    }
    
    private boolean isValidGuardStep(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        // Guard can only move diagonally one step
        return rowDiff == 1 && colDiff == 1;
    }
    
    private boolean isValidRookMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        return isValidRookStep(fromRow, fromCol, toRow, toCol) &&
               isPathClear(fromRow, fromCol, toRow, toCol);
    }
    
    private boolean isValidRookStep(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        // Rook can only move horizontally or vertically
        return (rowDiff == 0 && colDiff > 0) || (rowDiff > 0 && colDiff == 0);
    }
    
    private boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow == toRow) {
            // Moving horizontally
            int startCol = Math.min(fromCol, toCol);
            int endCol = Math.max(fromCol, toCol);
            for (int col = startCol + 1; col < endCol; col++) {
                if (board.getPiece(fromRow, col) != null) {
                    return false;
                }
            }
        } else if (fromCol == toCol) {
            // Moving vertically
            int startRow = Math.min(fromRow, toRow);
            int endRow = Math.max(fromRow, toRow);
            for (int row = startRow + 1; row < endRow; row++) {
                if (board.getPiece(row, fromCol) != null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isValidHorseMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        return isValidHorseStep(fromRow, fromCol, toRow, toCol) &&
               !isHorseBlocked(fromRow, fromCol, toRow, toCol);
    }
    
    private boolean isValidHorseStep(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        // Horse moves in L-shape: 2 steps in one direction, 1 step perpendicular
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private boolean isHorseBlocked(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;
        
        // Check the "leg" position that could block the horse
        int legRow = fromRow;
        int legCol = fromCol;
        
        if (Math.abs(rowDiff) == 2) {
            // Moving 2 rows, check the adjacent row
            legRow = fromRow + (rowDiff > 0 ? 1 : -1);
            legCol = fromCol;
        } else if (Math.abs(colDiff) == 2) {
            // Moving 2 columns, check the adjacent column
            legRow = fromRow;
            legCol = fromCol + (colDiff > 0 ? 1 : -1);
        }
        
        return board.getPiece(legRow, legCol) != null;
    }
    
    private boolean isValidCannonMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        return isValidRookStep(fromRow, fromCol, toRow, toCol) &&
               isValidCannonPath(fromRow, fromCol, toRow, toCol);
    }
    
    private boolean isValidCannonPath(int fromRow, int fromCol, int toRow, int toCol) {
        int pieceCount = countPiecesInPath(fromRow, fromCol, toRow, toCol);
        Piece targetPiece = board.getPiece(toRow, toCol);
        
        if (targetPiece == null) {
            // Moving to empty square - path must be clear
            return pieceCount == 0;
        } else {
            // Capturing - must jump exactly one piece
            return pieceCount == 1;
        }
    }
    
    private int countPiecesInPath(int fromRow, int fromCol, int toRow, int toCol) {
        int count = 0;
        
        if (fromRow == toRow) {
            // Moving horizontally
            int startCol = Math.min(fromCol, toCol);
            int endCol = Math.max(fromCol, toCol);
            for (int col = startCol + 1; col < endCol; col++) {
                if (board.getPiece(fromRow, col) != null) {
                    count++;
                }
            }
        } else if (fromCol == toCol) {
            // Moving vertically
            int startRow = Math.min(fromRow, toRow);
            int endRow = Math.max(fromRow, toRow);
            for (int row = startRow + 1; row < endRow; row++) {
                if (board.getPiece(row, fromCol) != null) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    private boolean isValidElephantMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        return isValidElephantStep(fromRow, fromCol, toRow, toCol) &&
               !hasCrossedRiver(piece.getColor(), toRow) &&
               !isElephantBlocked(fromRow, fromCol, toRow, toCol);
    }
    
    private boolean isValidElephantStep(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        // Elephant moves 2 steps diagonally
        return rowDiff == 2 && colDiff == 2;
    }
    
    private boolean hasCrossedRiver(String color, int row) {
        if (color.equals("Red")) {
            // Red elephant cannot cross river (row 6 and above)
            return row >= 6;
        } else {
            // Black elephant cannot cross river (row 5 and below)
            return row <= 5;
        }
    }
    
    private boolean isElephantBlocked(int fromRow, int fromCol, int toRow, int toCol) {
        // Check if the midpoint is blocked
        int midRow = (fromRow + toRow) / 2;
        int midCol = (fromCol + toCol) / 2;
        return board.getPiece(midRow, midCol) != null;
    }
    
    private boolean isValidSoldierMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        return isValidSoldierStep(piece, fromRow, fromCol, toRow, toCol);
    }
    
    private boolean isValidSoldierStep(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);
        
        if (piece.getColor().equals("Red")) {
            // Red soldier moves forward (increasing row numbers)
            if (fromRow < 6) {
                // Before crossing river - can only move forward
                return rowDiff == 1 && colDiff == 0;
            } else {
                // After crossing river - can move forward or sideways
                return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
            }
        } else {
            // Black soldier moves forward (decreasing row numbers)
            if (fromRow > 5) {
                // Before crossing river - can only move forward
                return rowDiff == -1 && colDiff == 0;
            } else {
                // After crossing river - can move forward or sideways
                return (rowDiff == -1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
            }
        }
    }
    
    private boolean wouldGeneralsFaceEachOtherAfterMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Find both generals
        Piece redGeneral = null;
        Piece blackGeneral = null;
        
        for (Piece piece : board.getAllPieces()) {
            if (piece.getType().equals("General")) {
                if (piece.getColor().equals("Red")) {
                    redGeneral = piece;
                } else {
                    blackGeneral = piece;
                }
            }
        }
        
        if (redGeneral != null && blackGeneral != null) {
            // Calculate where each general would be after the move
            int redRow = redGeneral.getRow();
            int redCol = redGeneral.getCol();
            int blackRow = blackGeneral.getRow();
            int blackCol = blackGeneral.getCol();
            
            // If the moving piece is the red general, update its position
            if (redGeneral.getRow() == fromRow && redGeneral.getCol() == fromCol) {
                redRow = toRow;
                redCol = toCol;
            }
            // If the moving piece is the black general, update its position
            if (blackGeneral.getRow() == fromRow && blackGeneral.getCol() == fromCol) {
                blackRow = toRow;
                blackCol = toCol;
            }
            
            // Check if they would be in the same column after the move
            if (redCol == blackCol) {
                // Check if there are any pieces between them
                for (int row = Math.min(redRow, blackRow) + 1; row < Math.max(redRow, blackRow); row++) {
                    if (board.getPiece(row, redCol) != null) {
                        return false; // There's a piece between them
                    }
                }
                return true; // They would face each other
            }
        }
        
        return false;
    }
    
    private boolean generalsFaceEachOther(int col) {
        // Check if Red and Black generals are in the same column with no pieces between them
        Piece redGeneral = null;
        Piece blackGeneral = null;
        
        for (Piece piece : board.getAllPieces()) {
            if (piece.getType().equals("General")) {
                if (piece.getColor().equals("Red")) {
                    redGeneral = piece;
                } else {
                    blackGeneral = piece;
                }
            }
        }
        
        if (redGeneral != null && blackGeneral != null && 
            redGeneral.getCol() == col && blackGeneral.getCol() == col) {
            // Check if there are any pieces between them
            int redRow = redGeneral.getRow();
            int blackRow = blackGeneral.getRow();
            
            for (int row = Math.min(redRow, blackRow) + 1; row < Math.max(redRow, blackRow); row++) {
                if (board.getPiece(row, col) != null) {
                    return false; // There's a piece between them
                }
            }
            return true; // They face each other
        }
        
        return false;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
}
