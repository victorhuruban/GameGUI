package com.chessgame.Pieces;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Board.Loc;

import java.io.Serializable;

public class King extends Piece implements Serializable {
    //private static final long serialVersionUID = 6156930883005779968L;
    private boolean isMoved;

    public King(int row, int column, String color) {
        super(row, column, color);
        isMoved = false;
    }

    @Override
    public String toString() {
        return "King";
    }

    @Override
    public boolean isValidMove(ChessBoard cb, int toRow, int toColumn) {
        if (!super.isValidMove(cb, toRow, toColumn)) {
            return false;
        }
        Piece current = cb.getLocation(getRow(), getColumn()).getPiece();

        if (cb.getLocation(toRow, toColumn).isOccupied()) {
            return false;
        }

        if ((toRow == getRow() && toColumn != getColumn()) || (toRow != getRow() && toColumn == getColumn())) {
            if (getRow() > toRow) {
                return toRow + 1 == getRow() && !cb.getLocation(toRow, toColumn).isOccupied();
            } else if (getRow() < toRow) {
                return toRow - 1 == getRow() && !cb.getLocation(toRow, toColumn).isOccupied();
            }

            if (getColumn() > toColumn) {
                return toColumn + 1 == getColumn() && !cb.getLocation(toRow, toColumn).isOccupied();
            } else if (getColumn() < toColumn) {
                return toColumn - 1 == getColumn() && !cb.getLocation(toRow, toColumn).isOccupied();
            }
        } else {
            if (toRow < getRow() && toColumn > getColumn()) { // Up right
                return getRow() - 1 == toRow && getColumn() + 1 == toColumn && !cb.getLocation(toRow, toColumn).isOccupied();
            } else if (toRow < getRow() && toColumn < getColumn()) { // Up left
                return getRow() - 1 == toRow && getColumn() - 1 == toColumn && !cb.getLocation(toRow, toColumn).isOccupied();
            } else if (toRow > getRow() && toColumn > getColumn()) { // Down right
                return getRow() + 1 == toRow && getColumn() + 1 == toColumn && !cb.getLocation(toRow, toColumn).isOccupied();
            } else if (toRow > getRow() && toColumn < getColumn()) { // Down left
                return getRow() + 1 == toRow && getColumn() - 1 == toColumn && !cb.getLocation(toRow, toColumn).isOccupied();
            }
        }
        return false;
    }

    /* The "move" method takes 3 parameters, which are the current ChessBoard object, and the row and column of the
     * location where the player whats to move.
     * Firstly, it checks if the location current location is occupied or not so that we can avoid a NullPointerException
     * Secondly, a try-catch for the NullPointerException (redundant, but left there and will come back and clean the code at the end)
     * Finally, if the isValidMove returns true, the piece is moved on the new position
     * */

    @Override
    public boolean move(ChessBoard cb, int toRow, int toColumn) {
        if (!cb.getLocation(getRow(), getColumn()).isOccupied()) {
            return false;
        }
        Piece current;
        try {
            current = cb.getLocation(getRow(), getColumn()).getPiece();
        } catch (NullPointerException ignored) {
            return false;
        }
        if (!isValidMove(cb, toRow, toColumn)) {
            return false;
        } else {
            setFMoved();
            Loc remove = cb.getLocation(getRow(), getColumn());
            Loc moved = cb.getLocation(toRow, toColumn);
            current.setColumn(toColumn);
            current.setRow(toRow);
            current.setMoved();
            moved.setPiece(current);
            remove.removePiece();
            return true;
        }
    }

    @Override
    public boolean isValidCapture(ChessBoard cb, int toRow, int toColumn) {
        Piece current = cb.getLocation(getRow(), getColumn()).getPiece();
        try {
            if (!cb.getLocation(toRow, toColumn).isOccupied()) {
                return false;
            }

            if (!current.getColor().equals(cb.getLocation(toRow, toColumn).getPiece().getColor())) {
                if ((toRow == getRow() && toColumn != getColumn()) || (toRow != getRow() && toColumn == getColumn())) {
                    if (getRow() > toRow) {
                        return toRow + 1 == getRow() && cb.getLocation(toRow, toColumn).isOccupied();
                    } else if (getRow() < toRow) {
                        return toRow - 1 == getRow() && cb.getLocation(toRow, toColumn).isOccupied();
                    }

                    if (getColumn() > toColumn) {
                        return toColumn + 1 == getColumn() && cb.getLocation(toRow, toColumn).isOccupied();
                    } else if (getColumn() < toColumn) {
                        return toColumn - 1 == getColumn() && cb.getLocation(toRow, toColumn).isOccupied();
                    }
                } else {
                    if (toRow < getRow() && toColumn > getColumn()) { // Up right
                        return getRow() - 1 == toRow && getColumn() + 1 == toColumn && cb.getLocation(toRow, toColumn).isOccupied();
                    } else if (toRow < getRow() && toColumn < getColumn()) { // Up left
                        return getRow() - 1 == toRow && getColumn() - 1 == toColumn && cb.getLocation(toRow, toColumn).isOccupied();
                    } else if (toRow > getRow() && toColumn > getColumn()) { // Down right
                        return getRow() + 1 == toRow && getColumn() + 1 == toColumn && cb.getLocation(toRow, toColumn).isOccupied();
                    } else if (toRow > getRow() && toColumn < getColumn()) { // Down left
                        return getRow() + 1 == toRow && getColumn() - 1 == toColumn && cb.getLocation(toRow, toColumn).isOccupied();
                    }
                }
            }
        } catch (NullPointerException ignored) {
            return false;
        }
        return false;
    }

    /* The "capture" method simply captures the enemy piece and replaces it with the piece which captured it if the above method returns true.
     * For further explanations, check "isValidCapture" method. */

    @Override
    public boolean capture(ChessBoard cb, int toRow, int toColumn) {
        super.capture(cb, toRow, toColumn);
        if (isValidCapture(cb, toRow, toColumn)) {
            setFMoved();
            Piece capturer = cb.getLocation(getRow(), getColumn()).getPiece();
            Loc remove = cb.getLocation(getRow(), getColumn());
            Loc moved = cb.getLocation(toRow, toColumn);
            moved.removePiece();
            remove.removePiece();
            capturer.setRow(toRow);
            capturer.setColumn(toColumn);
            moved.setPiece(capturer);
            return true;
        }
        return false;
    }

    private void setFMoved() {
        this.isMoved = true;
    }

    public boolean getFMoved() {
        return isMoved;
    }
}