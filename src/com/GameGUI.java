package com;

import com.chessgame.ServerClient.Client;
import com.chessgame.ServerClient.Server;
import com.poker.Lobby.Lobby;
import com.poker.ServerClient.ClientP;
import com.poker.ServerClient.ServerP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameGUI {
    private final Color CHESS_COLOR = new Color(83,130,172);
    private final Color POKER_COLOR = new Color(14,209,69);

    private final int CHESS_MODE = 1;
    private final int POKER_MODE = 2;

    private final Image CHESS_BACKGROUND = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background_chess.jpg"));
    private final Image POKER_BACKGROUND = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background_poker.jpg"));

    private JLabel backgroundPic = new JLabel(new ImageIcon(CHESS_BACKGROUND));

    private String name = "";
    private final JFrame frame;
    private final JPanel mainCards;
    private final int DEFAULT_PORT = 57894; //
    private Runnable runnable;
    private Runnable runnable1;
    private Runnable runnable2;

    private int hovered = 1;


    public GameGUI() {
        /////////////////
        // FRAME SET UP//
        /////////////////

        // FRAME SET UP
        //
        this.frame = new JFrame("Games");
        this.mainCards = new JPanel(new CardLayout());
        this.frame.setSize(1000, 600);
        this.frame.setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.mainCards);
        this.frame.setVisible(true);


        ////////////////////////
        // CARD LAYOUT SET UP //
        ////////////////////////

        // CHOICE CARD
        //
        JPanel choiceCard = new JPanel();
        choiceCard.setBackground(CHESS_COLOR);
        JButton startChessGame = new JButton("Start Chess Game");
        JButton startPokerGame = new JButton("Start Poker Game");
        choiceCard.add(startChessGame);
        choiceCard.add(startPokerGame);
        choiceCard.add(backgroundPic);

        // SET NAME CARD
        //
        JPanel setNameCard = new JPanel();
        JButton setNameButton = new JButton("Set");
        JButton backButton1 = new JButton("Back");
        JTextField nameField = new JTextField(15);
        setNameCard.add(setNameButton);
        setNameCard.add(backButton1);
        setNameCard.add(nameField);

        // CHOICE CARD: HOST OR JOIN A GAME
        //
        JPanel chooseHostOrJoin = new JPanel();
        JButton hostChoice = new JButton("Host");
        JButton joinChoice = new JButton("Join");
        JButton changeName = new JButton("Change Name");
        JButton changeGame = new JButton("Change Game");
        chooseHostOrJoin.add(hostChoice);
        chooseHostOrJoin.add(joinChoice);
        chooseHostOrJoin.add(changeName);
        chooseHostOrJoin.add(changeGame);

        // CHESS-HOST-JOIN FUNCTIONALITY CARD
        //
        JPanel joinCard = new JPanel();
        JButton joinButton = new JButton("Join");
        JButton backButton2 = new JButton("Back");
        JTextField ipField1 = new JTextField(3);
        JTextField ipField2 = new JTextField(3);
        JTextField ipField3 = new JTextField(3);
        JTextField ipField4 = new JTextField(3);
        joinCard.add(joinButton);
        joinCard.add(backButton2);
        joinCard.add(ipField1);
        joinCard.add(ipField2);
        joinCard.add(ipField3);
        joinCard.add(ipField4);

        // ADDING CARDS TO THE MAIN-CARD PANEL
        //
        this.mainCards.add(choiceCard, "1");
        this.mainCards.add(setNameCard, "2");
        this.mainCards.add(chooseHostOrJoin, "3");
        this.mainCards.add(joinCard, "4");

        //////////////////////////////
        // BUTTONS ACTION LISTENERS //
        //////////////////////////////

        // START CHESS GAME FRAME
        startChessGame.addActionListener(e -> {
            if (name.equals("")) {
                deleteJLabel(setNameCard.getComponents(), setNameCard);
                createJLabel(hovered, setNameCard);
                CardLayout cl = (CardLayout) this.mainCards.getLayout();
                cl.show(this.mainCards, "2");
            } else {
                deleteJLabel(chooseHostOrJoin.getComponents(), chooseHostOrJoin);
                createJLabel(hovered, chooseHostOrJoin);
                CardLayout cl = (CardLayout) this.mainCards.getLayout();
                cl.show(this.mainCards, "3");
            }
        });

        // START POKER GAME FRAME
        startPokerGame.addActionListener(e -> {
            if (name.equals("")) {
                deleteJLabel(setNameCard.getComponents(), setNameCard);
                createJLabel(hovered, setNameCard);
                CardLayout cl = (CardLayout) this.mainCards.getLayout();
                cl.show(this.mainCards, "2");
            } else {
                deleteJLabel(chooseHostOrJoin.getComponents(), chooseHostOrJoin);
                createJLabel(hovered, chooseHostOrJoin);
                CardLayout cl = (CardLayout) this.mainCards.getLayout();
                cl.show(this.mainCards, "3");
            }
        });

        // CODE WHICH CHANGES THE BACKGROUND WHEN YOU HOVER MOUSE OVER BUTTONS AT THE FIRST PANEL
        startChessGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hovered != CHESS_MODE) {
                    hovered = CHESS_MODE;
                    choiceCard.setBackground(CHESS_COLOR);
                    choiceCard.repaint();
                    backgroundPic.setIcon(new ImageIcon(CHESS_BACKGROUND));
                }
            }
        });

        startPokerGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hovered != POKER_MODE) {
                    hovered = POKER_MODE;
                    choiceCard.setBackground(POKER_COLOR);
                    choiceCard.repaint();
                    backgroundPic.setIcon(new ImageIcon(POKER_BACKGROUND));
                }
            }
        });

        // SET NAME BUTTON AND GO TO HOST/JOIN CHOICE FRAME
        setNameButton.addActionListener(e -> {
            if (checkName(nameField.getText())) {
                name = nameField.getText();
                deleteJLabel(chooseHostOrJoin.getComponents(), chooseHostOrJoin);
                createJLabel(hovered, chooseHostOrJoin);
                CardLayout cl = (CardLayout) this.mainCards.getLayout();
                cl.show(this.mainCards, "3");
            }
        });

        // BACK BUTTON TO CHOOSE A DIFFERENT GAME
        backButton1.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "1");
        });

        // STRAIGHT HOST A GAME
        hostChoice.addActionListener(e -> {
            if (hovered == CHESS_MODE) {
                startServerAndHost();
            } else if (hovered == POKER_MODE) {
                startServerAndHost();
            }
        });

        // GO TO THE JOIN FRAME
        joinChoice.addActionListener(e -> {
            /*if (hovered == POKER_MODE) {
                startClientAndJoin("192.168.0.155"); // TODO: UNCOMMENT BELOW CODE AFTER TESTING
            }*/
            deleteJLabel(joinCard.getComponents(), joinCard);
            createJLabel(hovered, joinCard);
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "4");
        });

        // GO BACK AND CHANGE NAME
        changeName.addActionListener(e -> {
            deleteJLabel(setNameCard.getComponents(), setNameCard);
            createJLabel(hovered, setNameCard);
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "2");
        });

        // GO BACK TO CHANGE THE GAME MODE
        changeGame.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "1");
        });

        // AFTER FILLING THE TEXT AREAS WITH RELEVANT IP, JOIN THE RESPECTIVE GAME
        joinButton.addActionListener(e -> {
            if (hovered == CHESS_MODE) {
                if (!getIp(ipField1.getText(),ipField2.getText(),ipField3.getText(),ipField4.getText()).equals("")) {
                    startClientAndJoin(getIp(ipField1.getText(),ipField2.getText(),ipField3.getText(),ipField4.getText()));
                } else {
                    System.out.println("Invalid IP");
                }
            } else if (hovered == POKER_MODE) {
                if (!getIp(ipField1.getText(),ipField2.getText(),ipField3.getText(),ipField4.getText()).equals("")) {
                    startClientAndJoin(getIp(ipField1.getText(),ipField2.getText(),ipField3.getText(),ipField4.getText()));
                } else {
                    System.out.println("Invalid IP");
                }
            }
        });

        // GO BACK TO THE HOST/JOIN FRAME
        backButton2.addActionListener(e -> {
            deleteJLabel(chooseHostOrJoin.getComponents(), chooseHostOrJoin);
            createJLabel(hovered, chooseHostOrJoin);
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "3");
        });
    }

    // CHECKS IF THE IP IS VALID OR NOT METHOD FOR THE JOIN FUNCTION
    // TODO: IMPROVE THE FUNCTION FURTHER
    public String getIp(String first, String second, String third, String forth) {
        if (first.length() > 0 && first.length() <= 3 && second.length() > 0 && second.length() <= 3 &&
                third.length() > 0 && third.length() <= 3 && forth.length() > 0 && forth.length() <= 3) {
            return first + "." + second + "." + third + "." + forth;
        } else {
            return "";
        }
    }

    // METHOD WHICH IS CREATING THE SERVER SOCKET
    public void startServerAndHost() {
        if (hovered == CHESS_MODE) {
            runnable = () -> {
                try {
                    Server server = new Server(DEFAULT_PORT, name);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            };
            Thread serverThread = new Thread(runnable);
            serverThread.start();
            getFrame().setVisible(false);
            serverThread.interrupt();
        } else if (hovered == POKER_MODE) {
            runnable1 = () -> {
                try {
                    ServerP server = new ServerP(DEFAULT_PORT);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            };
            runnable2 = () -> {
                try {
                    ClientP client = new ClientP("127.0.0.1", new Lobby(0, name));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            };
            Thread serverThread = new Thread(runnable1);
            Thread clientThread = new Thread(runnable2);
            serverThread.start();
            clientThread.start();
            getFrame().setVisible(false);
            serverThread.interrupt();
            clientThread.interrupt();
        }
    }

    // METHOD WHICH IS CREATING THE CLIENT
    public void startClientAndJoin (String input) { // TODO: FIND WAYS TO IMPROVE; ADD ERROR HANDLING
        if (input.equals("")) {
            System.out.println("Invalid");
        } else {
            if (hovered == CHESS_MODE) {
                runnable = () -> {
                    try {
                        Client client = new Client(input, DEFAULT_PORT, name);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                };
                Thread clientThread = new Thread(runnable);
                clientThread.start();
                getFrame().setVisible(false);
                clientThread.interrupt();
            } else if (hovered == POKER_MODE) {
                runnable = () -> {
                    try {
                        ClientP client = new ClientP(input, new Lobby(1, name));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                };
                Thread clientThread = new Thread(runnable);
                clientThread.start();
                getFrame().setVisible(false);
                clientThread.interrupt();
            }
        }
    }

    // CHECKS IF NAME IS VALID
    public boolean checkName(String name) {
        return !name.equals("");
    }

    // RETURN FRAME
    public JFrame getFrame() {
        return this.frame;
    }

    private void deleteJLabel(Component[] componentList, JPanel jpanel) {
        for (Component c: componentList) {
            if (c instanceof JLabel) {
                jpanel.remove(c);
            }
        }
    }

    private void createJLabel(int mode, JPanel jpanel) {
        if (mode == CHESS_MODE) {
            jpanel.add(new JLabel(new ImageIcon(CHESS_BACKGROUND)));
            jpanel.setBackground(CHESS_COLOR);
        } else if (mode == POKER_MODE) {
            jpanel.add(new JLabel(new ImageIcon(POKER_BACKGROUND)));
            jpanel.setBackground(POKER_COLOR);
        }
        jpanel.repaint();
    }

    public void afterEnd(String newName) {
        name = newName;
    }
}
