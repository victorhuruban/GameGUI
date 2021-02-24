package com;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws NullPointerException, IOException {
        GameGUI start = new GameGUI();
        /*int rolls = 100000;


        while (rolls != 0) {
            Pack p = new Pack();
            Player player = new Player("Vic", 1000, p);
            player.getRank().getScore();
            player.displayCards();

            rolls--;
        }*/

        /*JFrame test = new JFrame("test");
        test.setSize(200, 200);
        test.setVisible(true);
        test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel test2 = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawOval(50,50, 40,40);
                this.updateUI();
            }
        };
        test2.setSize(100, 100);
        test2.setBackground(Color.GREEN);


        test.add(test2);*/
    }
}
