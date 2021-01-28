package com.chessgame.Board;

import java.io.Serializable;

public class ChessBoard implements Serializable {
    private static final long serialVersionUID = 6156930883005779968L;
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

    public void reverseBoard() {
        Loc[][] tempLoc = new Loc[8][8];
        int r = 0;
        int c = 0;
        for (int i = 7; i >= 0; i--) {
            for (int j = 7; j >= 0; j--) {
                tempLoc[r][c] = locations[i][j];
                c++;
            }
            r++;
            c = 0;
        }
        this.locations = tempLoc;
    }
}
