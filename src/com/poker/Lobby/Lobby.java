package com.poker.Lobby;

import com.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Lobby {

    private final Color POKER_COLOR = new Color(14,209,69);
    private boolean readyPressed = false;
    private boolean readyPressedForTransfer = false;
    private boolean exit = false;
    private boolean sendBack = false;
    private boolean tryStart = false;
    private boolean sendReadyStat = false;
    private int state = 0;
    private int type;
    private int conNumL;
    private String name;

    private JPanel leftP;
    private JPanel rightP;
    private JFrame jframe;

    private final JPanel firstPlayerPanel;
    private final JPanel secondPlayerPanel;
    private final JPanel thirdPlayerPanel;
    private final JPanel forthPlayerPanel;
    private final JPanel fifthPlayerPanel;
    private final JPanel sixthPlayerPanel;
    private final JPanel seventhPlayerPanel;
    private final JPanel eightPlayerPanel;
    private final JPanel ninthPlayerPanel;
    private final JPanel tenthPlayerPanel;

    private JLabel card1;
    private JLabel card2;
    private JLabel card3;
    private JLabel card4;
    private JLabel card5;

    public Lobby(int type, String name) {
        this.name = name;
        this.type = type;
        this.conNumL = -1;
        jframe = new JFrame("Lobby");
        jframe.setSize(1000, 600);
        jframe.getContentPane().setBackground(POKER_COLOR);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setVisible(true);

        rightP = new JPanel(new GridLayout(3, 0));
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
            firstPlayerPanel.add(new JLabel("   " + name));
            firstPlayerPanel.add(new JLabel("Not Ready"));
        }
        jframe.repaint();

        readyButton.addActionListener(e -> {
            readyPressed = true;
            setState();
        });

        exitLobby.addActionListener(e -> {
            exit = true;
            System.exit(0);
        });

        startGame.addActionListener(e -> {
            tryStart = true;
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

    public void setSendBack() {
        sendBack = !sendBack;
    }

    public boolean getSendBack() {
        return sendBack;
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

    public boolean getTryStart() {
        return tryStart;
    }

    public void setTryStart() {
        tryStart = false;
    }

    public void setReadyStat(int num) {
        JPanel temp = null;
        boolean b = false;
        for (int i = 0; i < 11; i++) {
            temp = getPanel(i);
            JLabel tempL = (JLabel) temp.getComponent(0);
            if (tempL.getText().strip().equals(name)) {
                if (conNumL == -1) {
                    conNumL = i;
                }
                break;
            }
        }
        if (num == 1) {
            for (Component c : temp.getComponents()) {
                if (c instanceof JLabel && ((JLabel) c).getText().equals("Not Ready")) {
                    temp.remove(c);
                }
            }
            temp.add(new JLabel("Ready"));
            readyPressedForTransfer = true;
        } else if (num == 0) {
            for (Component c : temp.getComponents()) {
                if (c instanceof JLabel && ((JLabel) c).getText().equals("Ready")) {
                    temp.remove(c);
                }
            }
            temp.add(new JLabel("Not Ready"));
            readyPressedForTransfer = false;
        }
        temp.updateUI();
    }

    public void createGame(String values) throws IOException {
        String[] vals = values.split(" ");
        int index = getIndexForCardStr(conNumL);

        jframe.getContentPane().removeAll();
        jframe.setLayout(new BorderLayout());

        JPanel gameBoard = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        card1 = new JLabel(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))));
        c.gridx = 0;
        c.insets = new Insets(0,0,0,10);
        gameBoard.add(card1, c);
        card2 = new JLabel(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))));
        c.gridx = 1;
        c.insets = new Insets(0,10,0,10);
        gameBoard.add(card2, c);
        card3 = new JLabel(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))));
        c.gridx = 2;
        gameBoard.add(card3, c);
        card4 = new JLabel(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))));
        c.gridx = 3;
        gameBoard.add(card4, c);
        card5 = new JLabel(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))));
        c.gridx = 4;
        c.insets = new Insets(0,10,0,0);
        gameBoard.add(card5, c);
        gameBoard.setBackground(POKER_COLOR);

        JPanel playerInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        playerInfo.setBackground(POKER_COLOR);
        JLabel name = new JLabel(getName());
        JLabel myCard1 = new JLabel();
        setCardImage(vals[index], vals[index + 1], myCard1);
        JLabel myCard2 = new JLabel();
        setCardImage(vals[index + 2], vals[index + 3], myCard2);

        playerInfo.add(name);
        playerInfo.add(myCard1);
        playerInfo.add(myCard2);

        JPanel actions = new JPanel(new GridBagLayout());
        actions.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        actions.setBackground(POKER_COLOR);

        JButton fold = new JButton("Fold");
        JButton raise = new JButton("Raise");
        JButton check = new JButton("Check");
        JTextField tfield = new JTextField(10);

        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0,0,10,0);
        c.anchor = GridBagConstraints.SOUTH;
        actions.add(fold, c);

        c.gridy = 1;
        c.anchor = GridBagConstraints.NORTH;
        actions.add(check, c);

        c.insets = new Insets(0,0,5,0);
        c.anchor = GridBagConstraints.SOUTH;
        actions.add(raise, c);

        c.gridy = 2;
        c.anchor = GridBagConstraints.NORTH;
        actions.add(tfield, c);

        jframe.add(gameBoard, BorderLayout.CENTER);
        jframe.add(playerInfo, BorderLayout.NORTH);
        jframe.add(actions, BorderLayout.EAST);

        jframe.repaint();
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

    public int getIndexForCardStr(int num) {
        switch (num) {
            case 0:
                return 2;
            case 1:
                return 4;
            case 2:
                return 6;
            case 3:
                return 8;
            case 4:
                return 10;
            case 5:
                return 12;
            case 6:
                return 14;
                // TODO: ADD MORE NUMBERS FOR MORE THAN 6 PLAYERS MATCHES
        }
        return -1;
    }

    public void setCardImage(String value, String type, JLabel cardLabel) throws IOException {
        cardLabel.setIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/" + value + "_" + type + ".png"))));
    }

    public boolean getReadyPressedForTransfer() {
        return readyPressedForTransfer;
    }

    public boolean getSendReadyStat() {
        return sendReadyStat;
    }

    public void setSendReadyStat() {
        sendReadyStat = !sendReadyStat;
    }

    public int getType() {
        return type;
    }
}
