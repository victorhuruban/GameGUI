package com.chessgame;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Board.Loc;
import com.chessgame.Game.GameInitialization;

public class Main {

    public static void main(String[] args) throws NullPointerException {
        GameGUI start = new GameGUI();
        start.startServerOrClient("57894");
    }
}
