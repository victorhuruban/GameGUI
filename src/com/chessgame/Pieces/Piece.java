package com.chessgame.Pieces;

import com.chessgame.Board.ChessBoard;

import java.io.Serializable;

public class Piece implements Serializable {
    private static final long serialVersionUID = 6156930883005779968L;
    private boolean moved;
    private int row, column;
    private String color;

    public Piece (int row, int column, String color) {
        this.row = row;
        this.column = column;
        this.color = color;
        moved = false;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved() {
        moved = true;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        moved = true;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        moved = true;
        this.column = column;
    }

    public void setColumnRev(int column) {
        this.column = column;
    }

    public void setRowRev(int row) {
        this.row = row;
    }

    public String getColor() {
        return color;
    }

    public boolean isValidMove (ChessBoard cb, int toRow, int toColumn) {
        if (getRow() == toRow && getColumn() == toColumn) {
            return false;
        }
        return getRow() >= 0 && getRow() <= 7 && getColumn() >= 0 && getColumn() <= 7 && toRow >= 0 && toRow <= 7 && toColumn >= 0 && toColumn <= 7;
    }

    public boolean isValidCapture (ChessBoard cb, int toRow, int toColumn) {
        return isValidMove(cb, toRow, toColumn);
    }

    public boolean move(ChessBoard cb, int toRow, int toColumn) {
        return isValidMove(cb, toRow, toColumn);
    }

    public boolean capture(ChessBoard cb, int toRow, int toColumn) {
        return isValidMove(cb, toRow, toColumn);
    }
}
