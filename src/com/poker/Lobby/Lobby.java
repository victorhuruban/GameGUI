package com.poker.Lobby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Lobby {

    private final Color POKER_COLOR = new Color(14,209,69);
    private boolean readyPressed = false;
    private int state = 0;
    private boolean exit = false;
    private String name;

    private JPanel leftP;

    private JPanel firstPlayerPanel;
    private JPanel secondPlayerPanel;
    private JPanel thirdPlayerPanel;
    private JPanel forthPlayerPanel;
    private JPanel fifthPlayerPanel;
    private JPanel sixthPlayerPanel;
    private JPanel seventhPlayerPanel;
    private JPanel eightPlayerPanel;
    private JPanel ninthPlayerPanel;
    private JPanel tenthPlayerPanel;

    public Lobby(int type, String name) {
        this.name = name;
        JFrame jframe = new JFrame("Lobby");
        jframe.setSize(728, 455);
        jframe.getContentPane().setBackground(POKER_COLOR);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setVisible(true);

        JPanel rightP = new JPanel(new GridLayout(3, 0));
        leftP = new JPanel(new GridLayout(10, 0));
        leftP.setBackground(POKER_COLOR);
        leftP.getInsets(new Insets(10,10,10,10));

        JButton readyButton = new JButton("Ready");
        JButton startGame = new JButton("Start");
        JButton exitLobby = new JButton("Back");

        firstPlayerPanel = new JPanel(new GridLayout(0, 2));
        secondPlayerPanel = new JPanel(new GridLayout(0, 2));
        thirdPlayerPanel = new JPanel(new GridLayout(0, 2));
        forthPlayerPanel = new JPanel(new GridLayout(0, 2));
        fifthPlayerPanel = new JPanel(new GridLayout(0, 2));
        sixthPlayerPanel = new JPanel(new GridLayout(0, 2));
        seventhPlayerPanel = new JPanel(new GridLayout(0, 2));
        eightPlayerPanel = new JPanel(new GridLayout(0, 2));
        ninthPlayerPanel = new JPanel(new GridLayout(0, 2));
        tenthPlayerPanel = new JPanel(new GridLayout(0, 2));

        firstPlayerPanel.setBackground(Color.LIGHT_GRAY);
        secondPlayerPanel.setBackground(Color.LIGHT_GRAY);
        thirdPlayerPanel.setBackground(Color.LIGHT_GRAY);
        forthPlayerPanel.setBackground(Color.LIGHT_GRAY);
        fifthPlayerPanel.setBackground(Color.LIGHT_GRAY);
        sixthPlayerPanel.setBackground(Color.LIGHT_GRAY);
        seventhPlayerPanel.setBackground(Color.LIGHT_GRAY);
        eightPlayerPanel.setBackground(Color.LIGHT_GRAY);
        ninthPlayerPanel.setBackground(Color.LIGHT_GRAY);
        tenthPlayerPanel.setBackground(Color.LIGHT_GRAY);

        firstPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        secondPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        thirdPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        forthPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        fifthPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        sixthPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        seventhPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        eightPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        ninthPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        tenthPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        leftP.add(firstPlayerPanel);
        leftP.add(secondPlayerPanel);
        leftP.add(thirdPlayerPanel);
        leftP.add(forthPlayerPanel);
        leftP.add(fifthPlayerPanel);
        leftP.add(sixthPlayerPanel);
        leftP.add(seventhPlayerPanel);
        leftP.add(eightPlayerPanel);
        leftP.add(ninthPlayerPanel);
        leftP.add(tenthPlayerPanel);

        rightP.add(readyButton);
        rightP.add(exitLobby);

        rightP.setBackground(POKER_COLOR);
        jframe.add(leftP, BorderLayout.CENTER);
        jframe.add(rightP, BorderLayout.EAST);
        if (type == 0) {
            rightP.add(startGame);
            /*firstPlayerPanel.add(new JLabel("   " + name));
            firstPlayerPanel.add(new JLabel("Not Ready"));*/
        }

        readyButton.addActionListener(e -> {
            readyPressed = true;
            setState();
        });

        exitLobby.addActionListener(e -> {
            exit = true;
            System.exit(0);
        });
    }

    public boolean getExit() {
        return exit;
    }

    public boolean getReadyPressed() {
        return readyPressed;
    }

    public void changeReadyPressed() {
        readyPressed = false;
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public void setState() {
        if (state == 1) {
            state = 0;
        } else state = 1;
    }

    public void setJPanel(int con, String name) {
        JPanel ref = getPanel(con);
        ref.add(new JLabel("   " + name));
        ref.add(new JLabel("Not Ready"));
        leftP.updateUI();
    }

    public void setReadyStat(int num) {
        if (num == 1) {
            for (Component c : firstPlayerPanel.getComponents()) {
                if (c instanceof JLabel && ((JLabel) c).getText().equals("Not Ready")) {
                    firstPlayerPanel.remove(c);
                }
            }
            firstPlayerPanel.add(new JLabel("Ready"));
        } else if (num == 0) {
            for (Component c : firstPlayerPanel.getComponents()) {
                if (c instanceof JLabel && ((JLabel) c).getText().equals("Ready")) {
                    firstPlayerPanel.remove(c);
                }
            }
            firstPlayerPanel.add(new JLabel("Not Ready"));
        }
        leftP.updateUI();
    }

    public JPanel getPanel(int num) {
        switch (num) {
            case 0:
                return firstPlayerPanel;
            case 1:
                return secondPlayerPanel;
            case 2:
                return thirdPlayerPanel;
            case 3:
                return forthPlayerPanel;
            case 4:
                return fifthPlayerPanel;
            case 5:
                return sixthPlayerPanel;
            case 6:
                return seventhPlayerPanel;
            case 7:
                return eightPlayerPanel;
            case 8:
                return ninthPlayerPanel;
            case 9:
                return tenthPlayerPanel;
        }
        return null;
    }
}