package com.chessgame;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws NullPointerException, IOException {
        GameGUI start = new GameGUI();
        start.startServerOrClient("500");
    }
}
