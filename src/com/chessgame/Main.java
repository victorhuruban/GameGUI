package com.chessgame;

public class Main {

    public static void main(String[] args) throws NullPointerException {
        GameGUI start = new GameGUI();
        start.startServerAndHost();
        //start.startClientAndJoin("192.168.0.155");
    }
}
