package com.poker.Lobby;

import com.Main;
import com.poker.Pack.Card;
import com.poker.Pack.Pack;
import com.poker.Player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Lobby {
    // SELF DESCRIBED VARIABLES
    private final Color POKER_COLOR = new Color(14,209,69);
    private final String name;

    private final JPanel leftP;
    private final JPanel rightP;
    private final JFrame jframe;

    // BOOLEAN VALUES TO CONTROL FLOW OF DATA TRANSFER BETWEEN CLIENTS IN LOBBY
    private boolean readyPressed = false;
    private boolean readyPressedForTransfer = false;
    private boolean exit = false;
    private boolean sendBack = false;
    private boolean tryStart = false;
    private boolean sendReadyStat = false;
    private int state = 0;
    private final int type;
    private int conNumL;

    // JPANELS TO DISPLAY CLIENTS IN LOBBY
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

    // TABLE CARDS
    private JLabel card1;
    private JLabel card2;
    private JLabel card3;
    private JLabel card4;
    private JLabel card5;

    // LABELS TO DISPLAY IF CLIENT IS BIG OR SMALL BLIND
    private final JLabel BIG_BLIND_LABEL = new JLabel("B", SwingConstants.CENTER);
    private final JLabel SMALL_BLIND_LABEL = new JLabel("b", SwingConstants.CENTER);

    // ARRAYS THAT HOLDS INFORMATION ABOUT PLAYERS NAMES AND CONNECTION NUMBER
    public final ArrayList<String> playersNames = new ArrayList<>();
    public final ArrayList<Integer> cons = new ArrayList<>();

    // GAME VARIABLES
    Player player = null;
    JButton fold;
    JButton check;
    JButton raise;
    JTextField tfield;
    JPanel gameBoard;
    public JPanel playerInfo;
    private boolean interacted = false;
    private boolean inTurn;
    private boolean raised;
    private boolean transmitScore = false;
    private boolean returnControl = false;
    private boolean turnOver = false;
    public int turn;
    public int[] players;
    public int[] playersScores;
    public boolean[] playersState;
    public boolean[] playersActive;
    private int turningCards = 0;
    private Card[] cards;
    public int rValue = -1;

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

        startGame.addActionListener(e -> tryStart = true);
    }

    public boolean getExit() {
        return exit;
    }

    public boolean getReadyPressed() {
        return readyPressed;
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public boolean getSendBack() {
        return sendBack;
    }

    public boolean getTryStart() {
        return tryStart;
    }

    public boolean getReadyPressedForTransfer() {
        return readyPressedForTransfer;
    }

    public boolean getSendReadyStat() {
        return sendReadyStat;
    }

    public boolean getInteracted() {
        return interacted;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean getInTurn() {
        return inTurn;
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
                return 6;
            case 2:
                return 10;
            case 3:
                return 14;
            case 4:
                return 18;
            case 5:
                return 22;
            case 6:
                return 26;
            // TODO: ADD MORE NUMBERS FOR MORE THAN 6 PLAYERS MATCHES
        }
        return -1;
    }

    public int getNumOfPlayers(String[] vals) {
        switch (vals.length) {
            case 16:
                return 1;
            case 20:
                return 2;
            case 24:
                return 3;
            case 28:
                return 4;
            case 32:
                return 5;
            case 36:
                return 6;
            case 40:
                return 7;
            case 44:
                return 8;
            case 48:
                return 9;
            case 52:
                return 10;
        }
        return -1;
    }

    public void setSendReadyStat() {
        sendReadyStat = !sendReadyStat;
    }

    public void setSendBack() {
        sendBack = !sendBack;
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

    public void setTryStart() {
        tryStart = false;
    }

    public void setCardImage(String value, String type, JLabel cardLabel) {
        cardLabel.setIcon(new ImageIcon(new ImageIcon(Main.class.getResource("/com/poker/Lobby/res/" + value + "_" + type + "_s.png")).getImage().getScaledInstance(40, 70, Image.SCALE_DEFAULT)));
    }

    public void setInteracted() {
        interacted = false;
    }

    public void outOfTurn() {
        inTurn = false;
    }

    public void nextTurn() {
        if (turn + 1 == cons.size()) {
            turn = 0;
        } else turn++;
    }

    public void setReadyStat(int num) {
        JPanel temp = null;
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

    public void changeReadyPressed() {
        readyPressed = false;
    }

    public void createGame(String values) throws IOException {
        player = new Player(10000);
        String[] vals = values.split(" ");
        cards = new Card[5];
        raised = false;
        grabCards(vals, cards);
        int index = getIndexForCardStr(conNumL);
        players = new int[getNumOfPlayers(vals)];
        playersState = new boolean[getNumOfPlayers(vals)];
        playersActive = new boolean[getNumOfPlayers(vals)];
        playersScores = new int[getNumOfPlayers(vals)];
        Arrays.fill(players, -1);
        Arrays.fill(playersState, false);
        Arrays.fill(playersActive, true);
        turn = 0;
        inTurn = true;
        player = new Player(10000);
        player.addCards(new Card(vals[index], vals[index + 1]));
        player.addCards(new Card(vals[index + 2], vals[index + 3]));
        shiftNameAndCon(conNumL);

        jframe.getContentPane().removeAll();
        jframe.setLayout(new BorderLayout());

        gameBoard = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        card1 = new JLabel();
        card1.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
        c.gridx = 0;
        c.insets = new Insets(0,0,0,15);
        gameBoard.add(card1, c);
        card2 = new JLabel();
        card2.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
        c.gridx = 1;
        c.insets = new Insets(0,15,0,15);
        gameBoard.add(card2, c);
        card3 = new JLabel();
        card3.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
        c.gridx = 2;
        gameBoard.add(card3, c);
        card4 = new JLabel();
        card4.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
        c.gridx = 3;
        gameBoard.add(card4, c);
        card5 = new JLabel();
        card5.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
        c.gridx = 4;
        c.insets = new Insets(0,15,0,0);
        gameBoard.add(card5, c);
        gameBoard.setBackground(POKER_COLOR);

        JPanel actions = new JPanel(new GridBagLayout());
        actions.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        actions.setBackground(POKER_COLOR);

        fold = new JButton("Fold");
        raise = new JButton("Raise");
        check = new JButton("Check");
        tfield = new JTextField(10);

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

        playerInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        playerInfo.setBackground(POKER_COLOR);
        createPlayerInfo(playerInfo, vals);

        jframe.add(gameBoard, BorderLayout.CENTER);
        jframe.add(playerInfo, BorderLayout.NORTH);
        jframe.add(actions, BorderLayout.EAST);

        jframe.repaint();
    }

    public void addNameAndCon(String name, int con) {
        if (!cons.contains(con)) {
            playersNames.add(name);
            cons.add(con);

            boolean sorted = false;
            while (!sorted) {
                sorted = true;
                for (int i = 0; i < cons.size() - 1; i++) {
                    if (cons.get(i) > cons.get(i + 1)) {
                        int tempI = cons.get(i);
                        String tempS = playersNames.get(i);
                        cons.set(i, cons.get(i + 1));
                        cons.set(i + 1, tempI);
                        playersNames.set(i, playersNames.get(i + 1));
                        playersNames.set(i + 1, tempS);
                        sorted = false;
                    }
                }
            }
        }
    }

    public void shiftNameAndCon(int con) {
        for (int i = 0; i < con; i++) {
            int tempI = cons.get(0);
            String tempS = playersNames.get(0);
            cons.remove(0);
            playersNames.remove(0);
            cons.add(tempI);
            playersNames.add(tempS);
        }
    }

    public void printCandP() {
        System.out.println(playersNames);
        System.out.println(cons);
    }

    public void setRaised() {
        raised = !raised;
    }

    public boolean getRaised() {
        return raised;
    }

    public void ifAllMovedAndEqual() throws IOException {
        Set<Integer> temp1 = new HashSet<>();
        Set<Boolean> temp2 = new HashSet<>();
        for (int i: players) {
            temp1.add(i);
        }
        for (boolean b: playersState) {
            temp2.add(b);
        }
        int countTrue = 0;
        for (boolean b: playersActive) {
            if (!b) {
                countTrue++;
            }
        }
        if (countTrue == 1) {
            System.out.println("Doar unu activ, el a castigant");
        } else {
            if (temp1.size() == 1 && temp2.size() == 1) {
                if (turningCards == 0) {
                    card1.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/" + cards[0].getValue() + "_" + cards[0].getType() + ".png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
                    card2.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/" + cards[1].getValue() + "_" + cards[1].getType() + ".png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
                    card3.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/" + cards[2].getValue() + "_" + cards[2].getType() + ".png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
                    System.out.println(getScore(turningCards) + ": SCORE");
                    setTransmitScore();
                    turningCards++;
                    resetArrays();
                    returnControlToBB();
                } else if (turningCards == 1) {
                    card4.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/" + cards[3].getValue() + "_" + cards[3].getType() + ".png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
                    System.out.println(getScore(turningCards));
                    setTransmitScore();
                    turningCards++;
                    resetArrays();
                    returnControlToBB();
                } else if (turningCards == 2){
                    card5.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/" + cards[4].getValue() + "_" + cards[4].getType() + ".png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
                    System.out.println(getScore(turningCards));
                    setTransmitScore();
                    resetArrays();
                    setTurnOver();
                }
            } else {
                nextTurn();
                System.out.println("Cannot turn cards after this turn");
                System.out.println(turn);
            }
        }
    }

    public void grabCards(String[] vals, Card[] cards) {
        int index = 4;
        for (int i = vals.length - 1; i > vals.length - 1 - 9; i -= 2) {
            Card temp = new Card(vals[i - 1], vals[i]);
            cards[index] = temp;
            index--;
        }
    }

    private void resetArrays() {
        Arrays.fill(players, -1);
        Arrays.fill(playersState, false);
    }

    public void createPlayerInfo(JPanel playerInfo, String[] vals) {
        int index = getIndexForCardStr(conNumL);
        JPanel myInfo = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        myInfo.setBackground(POKER_COLOR);
        myInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel name = new JLabel(getName(), SwingConstants.CENTER);
        name.setBackground(Color.LIGHT_GRAY);
        name.setOpaque(true);
        name.setPreferredSize(new Dimension(50, 15));
        myInfo.add(name, c);
        c.gridy = 1;
        if (conNumL == 0) {
            BIG_BLIND_LABEL.setBackground(Color.LIGHT_GRAY);
            BIG_BLIND_LABEL.setOpaque(true);
            BIG_BLIND_LABEL.setPreferredSize(new Dimension(50,50));
            myInfo.add(BIG_BLIND_LABEL, c);
        } else if (conNumL == 1) {
            SMALL_BLIND_LABEL.setBackground(Color.LIGHT_GRAY);
            SMALL_BLIND_LABEL.setOpaque(true);
            SMALL_BLIND_LABEL.setPreferredSize(new Dimension(50,50));
            myInfo.add(SMALL_BLIND_LABEL, c);
        }
        c.gridy = 0;
        c.gridx = 1;
        c.gridheight = 2;
        c.gridwidth = 2;
        JPanel myCards = new JPanel(new GridBagLayout());
        myCards.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        myCards.setBackground(POKER_COLOR);
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(2,2,2,5);
        JLabel myCard1 = new JLabel();
        setCardImage(vals[index], vals[index + 1], myCard1);
        myCards.add(myCard1, c2);
        c2.insets = new Insets(2,0,2,2);
        JLabel myCard2 = new JLabel();
        setCardImage(vals[index + 2], vals[index + 3], myCard2);
        player.addCards(new Card(vals[index], vals[index + 1]));
        player.addCards(new Card(vals[index + 2], vals[index + 3]));
        player.getRank().addPlayerCard(player.getCards());
        myCards.add(myCard2, c2);
        c.insets = new Insets(5,0,5,5);
        myInfo.add(myCards, c);

        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0,5,2,5);
        JLabel myMoney = new JLabel("$10000 ", SwingConstants.RIGHT);
        myMoney.setPreferredSize(new Dimension(50, 15));
        myMoney.setBackground(Color.LIGHT_GRAY);
        myMoney.setOpaque(true);
        myInfo.add(myMoney, c);
        c.gridwidth = 1;
        c.gridx = 2;
        JLabel turnColor = new JLabel("my turn", SwingConstants.CENTER);
        turnColor.setBackground(Color.LIGHT_GRAY);
        turnColor.setOpaque(true);
        myInfo.add(turnColor, c);

        playerInfo.add(myInfo);
        for (int i = 1; i < cons.size(); i++) {
            myInfo = new JPanel(new GridBagLayout());
            c = new GridBagConstraints();
            c.insets = new Insets(5,5,5,5);
            myInfo.setBackground(POKER_COLOR);
            myInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            name = new JLabel(playersNames.get(i), SwingConstants.CENTER);
            name.setBackground(Color.LIGHT_GRAY);
            name.setOpaque(true);
            name.setPreferredSize(new Dimension(50, 15));
            myInfo.add(name, c);
            c.gridy = 1;
            if (cons.get(i) == 0) {
                BIG_BLIND_LABEL.setBackground(Color.LIGHT_GRAY);
                BIG_BLIND_LABEL.setOpaque(true);
                BIG_BLIND_LABEL.setPreferredSize(new Dimension(50,50));
                myInfo.add(BIG_BLIND_LABEL, c);
            } else if (cons.get(i) == 1) {
                SMALL_BLIND_LABEL.setBackground(Color.LIGHT_GRAY);
                SMALL_BLIND_LABEL.setOpaque(true);
                SMALL_BLIND_LABEL.setPreferredSize(new Dimension(50,50));
                myInfo.add(SMALL_BLIND_LABEL, c);
            }
            c.gridy = 0;
            c.gridx = 1;
            c.gridheight = 2;
            c.gridwidth = 2;
            c.insets = new Insets(5,0,5,5);
            myCards = new JPanel(new GridBagLayout());
            myCards.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            myCards.setBackground(POKER_COLOR);
            c2 = new GridBagConstraints();
            c2.insets = new Insets(2,2,2,5);
            myCard1 = new JLabel();
            myCard1.setIcon(new ImageIcon(new ImageIcon(Main.class.getResource("/com/poker/Lobby/res/back_s.png")).getImage().getScaledInstance(40,70, Image.SCALE_DEFAULT)));
            myCards.add(myCard1, c2);
            c2.insets = new Insets(2,0,2,2);
            myCard2 = new JLabel();
            myCard2.setIcon(new ImageIcon(new ImageIcon(Main.class.getResource("/com/poker/Lobby/res/back_s.png")).getImage().getScaledInstance(40,70, Image.SCALE_DEFAULT)));
            player.addCards(new Card(vals[index], vals[index + 1]));
            player.addCards(new Card(vals[index + 2], vals[index + 3]));
            player.getRank().addPlayerCard(player.getCards());
            myCards.add(myCard2, c2);
            myInfo.add(myCards, c);

            c.gridheight = 1;
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(0,5,2,5);
            myMoney = new JLabel("$10000 ", SwingConstants.RIGHT);
            myMoney.setPreferredSize(new Dimension(50, 15));
            myMoney.setBackground(Color.LIGHT_GRAY);
            myMoney.setOpaque(true);
            myInfo.add(myMoney, c);
            c.gridwidth = 1;
            c.gridx = 2;
            turnColor = new JLabel("my turn", SwingConstants.CENTER);
            turnColor.setBackground(Color.LIGHT_GRAY);
            turnColor.setOpaque(true);
            myInfo.add(turnColor, c);

            playerInfo.add(myInfo);
        }
        fold.addActionListener(e -> {
            if (turn == cons.get(0)) {
                player.setFold();
                playersActive[0] = false;
            }
        });

        check.addActionListener(e -> {
            if (turn == cons.get(0)) {
                boolean check = true;
                for (int i: players) {
                    if (i > 0) {
                        System.out.println("Cannot check, need to raise");
                        check = false;
                        break;
                    }
                }
                if (check) {
                    players[turn] = 0;
                    playersState[turn] = true;
                    interacted = true;
                }
            }
        });

        raise.addActionListener(e -> {
            if (turn == cons.get(0) && !tfield.getText().equals("") && onlyDigits(tfield.getText())) {
                rValue = Integer.parseInt(tfield.getText());
                players[0] = rValue;
                tfield.setText("");
                playersState[turn] = true;
                setRaised();
                interacted = true;
            }
        });
    }

    private boolean onlyDigits(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // DEBUG CODE!
    public void printPlayersandPlayersState() {
        for (int i: players) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (boolean i: playersState) {
            System.out.print(i);
        }
        System.out.println();
    }

    public int getScore(int t) {
        if (t == 0) {
            player.addSharedCards(new Card(cards[0].getValue(), cards[0].getType()));
            player.addSharedCards(new Card(cards[1].getValue(), cards[1].getType()));
            player.addSharedCards(new Card(cards[2].getValue(), cards[2].getType()));
            player.getRank().addSharedCard(player.getSharedCards());
            return player.getRank().getScore();
        } else if (t == 1) {
            player.addSharedCards(new Card(cards[3].getValue(), cards[3].getType()));
            player.getRank().addSharedCard(player.getSharedCards());
            return player.getRank().getScore();
        } else if (t == 2) {
            player.addSharedCards(new Card(cards[4].getValue(), cards[4].getType()));
            player.getRank().addSharedCard(player.getSharedCards());
            return player.getRank().getScore();
        }
        return -1;
    }

    public boolean transmitScore() {
        return transmitScore;
    }

    public void setTransmitScore() {
        transmitScore = !transmitScore;
    }

    public boolean allMovedOnce() {
        for (boolean b: playersState) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public void returnControlToBB() {
        // 0 = JLABEL NAME; 1 = JLABEL BIG OR SMALL BLIND LABEL; 2 = JPANEL WITH CARDS
        // 3 = JLABEL MONEY; 4 = JLABEL TURNCIRCLE
        returnControl = !returnControl;
    }

    public void updateNewTurn(String[] newCards) throws IOException {
        int index = getIndexForCardStr(conNumL);
        JPanel myPanel = (JPanel) playerInfo.getComponent(0);
        JPanel myInfo = (JPanel) myPanel.getComponent(2);
        JLabel myCard1 = new JLabel();
        JLabel myCard2 = new JLabel();
        myInfo.removeAll();
        setCardImage(newCards[index], newCards[index + 1], myCard1);
        setCardImage(newCards[index + 2], newCards[index + 3], myCard2);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2,2,2,5);
        myInfo.add(myCard1, c);
        c.insets = new Insets(2,0,2,2);
        myInfo.add(myCard2, c);
        c.gridy = 0;
        c.gridx = 1;
        c.gridheight = 2;
        c.gridwidth = 2;
        c.insets = new Insets(5,0,5,5);
        myPanel.add(myInfo, c);
        GridBagConstraints c2 = new GridBagConstraints();
        for (int i = 1; i < cons.size(); i++) {

            myPanel = (JPanel) playerInfo.getComponent(i);
            myInfo = (JPanel) myPanel.getComponent(2);
            myCard1 = new JLabel();
            myCard2 = new JLabel();
            myInfo.removeAll();
            myCard1.setIcon(new ImageIcon(new ImageIcon(Main.class.getResource("/com/poker/Lobby/res/back_s.png")).getImage().getScaledInstance(40,70, Image.SCALE_DEFAULT)));
            myCard2.setIcon(new ImageIcon(new ImageIcon(Main.class.getResource("/com/poker/Lobby/res/back_s.png")).getImage().getScaledInstance(40,70, Image.SCALE_DEFAULT)));
            c2.insets = new Insets(2,2,2,5);
            myInfo.add(myCard1, c2);
            c2.insets = new Insets(2,0,2,2);
            c2.gridx = 1;
            myInfo.add(myCard2, c2);
            myPanel.add(myInfo, c);
        }

        for (Component comps: gameBoard.getComponents()) {
            JLabel temp = (JLabel) comps;
            temp.setIcon(new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResource("/com/poker/Lobby/res/back.png"))).getImage().getScaledInstance(105, 140, Image.SCALE_DEFAULT)));
        }

        grabCards(newCards, cards);

        turningCards = 0;
        gameBoard.updateUI();
        playerInfo.updateUI();
    }

    public boolean getReturnControl() {
        return returnControl;
    }

    public void setTurnOver() {
        turnOver = !turnOver;
    }

    public boolean getTurnOver() {
        return turnOver;
    }

    public String getNewPackAndCards() {
        Pack pack = new Pack();
        StringBuilder sb = new StringBuilder("start newgame");
        for (int i = 0; i < 5 + (cons.size() * 2); i++) {
            Card c = pack.popCard();
            sb.append(" ").append(c.getValue()).append(" ").append(c.getType());
        }

        return sb.toString();
    }
}
