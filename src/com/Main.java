package com;

import com.poker.Pack.Pack;
import com.poker.Player.Player;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws NullPointerException {
        //GameGUI start = new GameGUI();
        int rolls = 100000;

        Pack p = new Pack();
        Player player = new Player("Vic", 1000, p);
        player.getRank().getScore();
        player.displayCards();
       /* while (rolls != 0) {


            rolls--;
        }*/
    }
}
