package com.chessgame.Pieces;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Board.Loc;

import java.io.Serializable;
import java.util.Scanner;

public class Pawn extends Piece implements Serializable {
    //private static final long serialVersionUID = 6156930883005779968L;
    boolean end;

    public Pawn(int row, int column, String color) {
        super(row, column, color);
        this.end = false;
    }

    @Override
    public String toString() {
        return "Pawn";
    }

    /* The "isValidMove" takes 3 parameters, which are the current ChessBoard object, and the row and column of the
     * location where the player whats to move.
     * Firstly, it checks if the row and column are inside the 0-7 range and returns boolean value accordingly
     * Second, it checks if the location is occupied or not and returns boolean value accordingly
     * Finally, based on the piece's color, it allows the player to move one only in front and allows the player to move
     * 2 locations if the pawn is at the starting position
     * */

    @Override
    public boolean isValidMove(ChessBoard cb, int toRow, int toColumn) {
        if (!super.isValidMove(cb, toRow, toColumn)) {
            return false;
        }

        if (cb.getLocation(toRow, toColumn).isOccupied()) {
            return false;
        }
        if (toRow == getRow() - 1 && getColumn() == toColumn) {
            return true;
        } else return !isMoved() && toRow == getRow() - 2 && getColumn() == toColumn;
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
        if (!super.move(cb, toRow, toColumn)) {
            return false;
        }
        Piece current;
        try {
            current = cb.getLocation(getRow(), getColumn()).getPiece();
        } catch (NullPointerException ignored) {
            return false;
        }
        if (isValidMove(cb, toRow, toColumn)) {
            Loc remove = cb.getLocation(getRow(), getColumn());
            Loc moved = cb.getLocation(toRow, toColumn);
            current.setColumn(toColumn);
            current.setRow(toRow);
            current.setMoved();
            moved.setPiece(current);
            remove.removePiece();
            if (getColor().equals("white") && getRow() == 0) {
                transformPawn(cb, getRow(), getColumn(), "white");
            } else if (getColor().equals("black") && getRow() == 7) {
                transformPawn(cb, getRow(), getColumn(), "black");
            }
            return true;
        } else return false;
    }

    @Override
    public boolean isValidCapture(ChessBoard cb, int toRow, int toColumn) {

        Piece captureTemp = cb.getLocation(getRow(), getColumn()).getPiece();
        try {
            /*if (!cb.getLocation(toRow, toColumn).isOccupied()) {
                return false;
            }*/
            if (!captureTemp.getColor().equals(cb.getLocation(toRow, toColumn).getPiece().getColor())) {
                return toRow == getRow() - 1 && (getColumn() == toColumn - 1 || getColumn() == toColumn + 1);
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
            Piece captured = cb.getLocation(getRow(), getColumn()).getPiece();
            Loc remove = cb.getLocation(getRow(), getColumn());
            Loc moved = cb.getLocation(toRow, toColumn);
            moved.removePiece();
            remove.removePiece();
            captured.setRow(toRow);
            captured.setColumn(toColumn);
            moved.setPiece(captured);
            return true;
        }
        return false;
    }

    private void transformPawn(ChessBoard cb, int row, int column, String color) {
        System.out.println("Pawn reached end of table. What piece do you want on table?");
        System.out.println("Your choices are: Rook, Knight, Bishop, Queen");
        System.out.print("Write the name of your choice: ");

        Loc removePawn = cb.getLocation(row, column);
        removePawn.removePiece();
        Piece piece;

        boolean terminate = true;
        Scanner scanner = new Scanner(System.in);

        while (terminate) {
            String choice = scanner.nextLine();
            switch (choice) {
                case "Rook":
                    piece = new Rook(getRow(), getColumn(), "white");
                    removePawn.setPiece(piece);
                    terminate = false;
                    break;
                case "Knight":
                    piece = new Knight(getRow(), getColumn(), "white");
                    removePawn.setPiece(piece);
                    terminate = false;
                    break;
                case "Bishop":
                    piece = new Bishop(getRow(), getColumn(), "white");
                    removePawn.setPiece(piece);
                    terminate = false;
                    break;
                case "Queen":
                    piece = new Queen(getRow(), getColumn(), "white");
                    removePawn.setPiece(piece);
                    terminate = false;
                    break;
                default:
                    System.out.println("invalid input, Write the name of your choice: ");
            }
        }
    }
}
