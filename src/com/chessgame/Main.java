package com.chessgame;

import com.chessgame.Game.Game;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws NullPointerException {
        GameGUI start = new GameGUI();
        start.startServerOrClient("500");
    }
}
