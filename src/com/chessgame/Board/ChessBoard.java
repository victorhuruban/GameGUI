package com.chessgame.Board;

import java.io.Serializable;

public class ChessBoard implements Serializable {
    private Loc[][] locations = new Loc[8][8];

    public ChessBoard() {
        for (int row = 0; row < locations.length; row++) {
            for (int column = 0; column < locations[row].length; column++) {
                this.locations[row][column] = new Loc(row, column);
            }
        }
    }

    public Loc getLocation(int row, int column) {
        try {
            return locations[row][column];
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public void listCB() {
        for (Loc[] location : locations) {
            for (Loc loc : location) {
                System.out.print(loc + " ");
            }
            System.out.println();
        }
    }

    public void reversePieces() {
        Loc[][] reverseChessboard = new Loc[8][8];
        int rowLoc = 0;
        int columnLoc = 0;
        for (int i = 7; i >= 0; i--) {
            for (int j = 7; j >= 0; j--) {
                reverseChessboard[rowLoc][columnLoc] = locations[i][j];
                columnLoc++;
            }
            rowLoc++;
            columnLoc = 0;
        }

        setLocations(reverseChessboard);
    }

    private void setLocations(Loc[][] set) {
        this.locations = set;
    }
}
