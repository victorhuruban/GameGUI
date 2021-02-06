package com.chessgame.Board;

import java.io.Serializable;

public class ChessBoard implements Serializable {
    private static final long serialVersionUID = 6156930883005779968L;
    private Loc[][] locations = new Loc[8][8];

    // ChessBoard class: The class which creates and controls how the Chess board works.
    //
    //                   When the class is instantiated, the "locations' array is populated with
    //                 'Loc' objects, getting ready to be populated with the respective pieces,
    //                 to create the Chessboard.
    //
    public ChessBoard() {
        for (int row = 0; row < locations.length; row++) {
            for (int column = 0; column < locations[row].length; column++) {
                this.locations[row][column] = new Loc(row, column);
            }
        }
    }

    // Returns the Loc object from the respective row and column value given.
    // Returns null if row or/and column given are out of bounds of the Chessboard (value < 0 or value > 8)
    //
    public Loc getLocation(int row, int column) {
        try {
            return locations[row][column];
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    // Use to debug: Displays the state of the ChessBoard instance
    //
    /*public void listCB() {
        for (Loc[] location : locations) {
            for (Loc loc : location) {
                System.out.print(loc + " ");
            }
            System.out.println();
        }
    }*/

    // Method which rotates the board for the other player.
    // This is the means through which the player is sending the ChessBoard state to the other player through
    //the sockets.
    //
    public void reverseBoard() {
        Loc[][] tempLoc = new Loc[8][8];
        int r = 0;
        int c = 0;
        for (int i = 7; i >= 0; i--) {
            for (int j = 7; j >= 0; j--) {
                tempLoc[r][c] = new Loc(r, c);
                if (locations[i][j].isOccupied()) {
                    tempLoc[r][c].setPiece(locations[i][j].getPiece());
                    tempLoc[r][c].getPiece().setRowRev(r);
                    tempLoc[r][c].getPiece().setColumnRev(c);
                }
                c++;
            }
            r++;
            c = 0;
        }
        this.locations = tempLoc;
    }
}
