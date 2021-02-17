package com;

import com.poker.Pack.Pack;
import com.poker.Player.Player;
import com.poker.ServerClient.ServerP;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws NullPointerException, IOException {
        GameGUI start = new GameGUI();
        //ServerP test = new ServerP(9090);
        /*int rolls = 100000;


        while (rolls != 0) {
            Pack p = new Pack();
            Player player = new Player("Vic", 1000, p);
            player.getRank().getScore();
            player.displayCards();

            rolls--;
        }*/
    }
}
