package com.chessgame;

import com.chessgame.Game.Game;
import com.chessgame.ServerClient.CSFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws NullPointerException, IOException {
        JFrame start = new JFrame("Games");
        JPanel panel = new JPanel();
        JButton button = new JButton("change JFrame");

        start.setSize(800, 800);
        start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start.setVisible(true);

        panel.add(button);
        start.add(panel);

        button.addActionListener(e -> {
            start.setVisible(false);
            CSFrame csFrame = new CSFrame();
        });
        /*Game game = new Game();
        game.createJFrameCB().setVisible(true);*/
    }
}
