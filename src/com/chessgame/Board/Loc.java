package com.chessgame.Board;

import com.chessgame.Pieces.Piece;

import java.io.Serializable;

public class Loc implements Serializable {

    public int row;
    public int column;
    private Piece piece;

    // Loc class:  Used to populate the List of locations in the ChessBoard instance
    //           and keep track of the state of the location:
    //              - "null" -> there is nothing on the respective location
    //              - "Piece" -> the piece instance
    //
    public Loc(int row, int column) {
        this.piece = null;
        this.row = row;
        this.column = column;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isOccupied() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void removePiece() {
        piece = null;
    }

    @Override
    public String toString() {
        if (piece == null) {
            return "null";
        }
        return piece.toString();
    }
}
