package com.chessgame;

import com.chessgame.Game.Game;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws NullPointerException, IOException {
        GameGUI start = new GameGUI();
        start.startServerOrClient("192.168.0.15 500");
    }
}
