package com.chessgame.Player;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Pieces.Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private static final long serialVersionUID = 6156930883005779968L;
    private final String color;
    private final ChessBoard cb;
    private List<Piece> pieces = new ArrayList<>();

    public Player(String color, ChessBoard cb) {
        this.cb = cb;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public ChessBoard getCb() {
        return cb;
    }

    public Piece getKing() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                try {
                    if (getCb().getLocation(row, column).getPiece().getColor().equals(getColor()) && getCb().getLocation(row, column).getPiece().toString().equals("King")) {
                        return getCb().getLocation(row, column).getPiece();
                    }
                } catch (NullPointerException ignored) {}
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return pieces.toString();
    }
}
