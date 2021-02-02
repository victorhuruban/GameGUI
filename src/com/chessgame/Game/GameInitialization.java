package com.chessgame.Game;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Pieces.*;

import java.io.Serializable;

public class GameInitialization implements Serializable {
    private static final long serialVersionUID = 6156930883005779968L;
    final private ChessBoard cb;

    public GameInitialization(int num) {
        cb = new ChessBoard();
        if (num == 1) {
            for (int i = 0; i < 8; i++) {
                cb.getLocation(1, i).setPiece(new Pawn(1, i, "black"));
                cb.getLocation(6, i).setPiece(new Pawn(6, i, "white"));
            }
            cb.getLocation(0, 0).setPiece(new Rook(0, 0, "black"));
            cb.getLocation(0, 1).setPiece(new Knight(0, 1, "black"));
            cb.getLocation(0, 2).setPiece(new Bishop(0, 2, "black"));
            cb.getLocation(0, 4).setPiece(new King(0, 4, "black"));
            cb.getLocation(0, 3).setPiece(new Queen(0, 3, "black"));
            cb.getLocation(0, 5).setPiece(new Bishop(0, 5, "black"));
            cb.getLocation(0, 6).setPiece(new Knight(0, 6, "black"));
            cb.getLocation(0, 7).setPiece(new Rook(0, 7, "black"));

            cb.getLocation(7, 0).setPiece(new Rook(7, 0, "white"));
            cb.getLocation(7, 1).setPiece(new Knight(7, 1, "white"));
            cb.getLocation(7, 2).setPiece(new Bishop(7, 2, "white"));
            cb.getLocation(7, 4).setPiece(new King(7, 4, "white"));
            cb.getLocation(7, 3).setPiece(new Queen(7, 3, "white"));
            cb.getLocation(7, 5).setPiece(new Bishop(7, 5, "white"));
            cb.getLocation(7, 6).setPiece(new Knight(7, 6, "white"));
            cb.getLocation(7, 7).setPiece(new Rook(7, 7, "white"));
        } else {
            for (int i = 0; i < 8; i++) {
                cb.getLocation(1, i).setPiece(new Pawn(1, i, "white"));
                cb.getLocation(6, i).setPiece(new Pawn(6, i, "black"));
            }
            cb.getLocation(0, 0).setPiece(new Rook(0, 0, "white"));
            cb.getLocation(0, 1).setPiece(new Knight(0, 1, "white"));
            cb.getLocation(0, 2).setPiece(new Bishop(0, 2, "white"));
            cb.getLocation(0, 4).setPiece(new Queen(0, 4, "white"));
            cb.getLocation(0, 3).setPiece(new King(0, 3, "white"));
            cb.getLocation(0, 5).setPiece(new Bishop(0, 5, "white"));
            cb.getLocation(0, 6).setPiece(new Knight(0, 6, "white"));
            cb.getLocation(0, 7).setPiece(new Rook(0, 7, "white"));

            cb.getLocation(7, 0).setPiece(new Rook(7, 0, "black"));
            cb.getLocation(7, 1).setPiece(new Knight(7, 1, "black"));
            cb.getLocation(7, 2).setPiece(new Bishop(7, 2, "black"));
            cb.getLocation(7, 4).setPiece(new Queen(7, 4, "black"));
            cb.getLocation(7, 3).setPiece(new King(7, 3, "black"));
            cb.getLocation(7, 5).setPiece(new Bishop(7, 5, "black"));
            cb.getLocation(7, 6).setPiece(new Knight(7, 6, "black"));
            cb.getLocation(7, 7).setPiece(new Rook(7, 7, "black"));
        }

    }

    public ChessBoard getCb() {
        return cb;
    }
}
