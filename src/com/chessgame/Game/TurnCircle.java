package com.chessgame.Game;

import javax.swing.*;
import java.awt.*;

public class TurnCircle extends JLabel {
    private String color;
    private boolean turn;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (color.equals("white")) {
            if (turn) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }
            g.fillOval(125, 6, 15, 15);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(125, 6, 15, 15);
        } else if (color.equals("black")) {
            if (turn) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }
            g.fillOval(125, 6, 15, 15);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(125, 6, 15, 15);
        }
    }

    public void initialSetCircle(String color, boolean turn) {
        this.color = color;
        this.turn = turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}
