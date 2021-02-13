package com;

import com.chessgame.ServerClient.Client;
import com.chessgame.ServerClient.Server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameGUI {
    private String name;
    private final JFrame frame;
    private final JPanel mainCards;
    private final int DEFAULT_PORT = 57894;
    private Runnable runnable;


    public GameGUI() {
        /////////////////
        // FRAME SET UP//
        /////////////////

        // FRAME SET UP
        //
        this.frame = new JFrame("Games");
        this.mainCards = new JPanel(new CardLayout());
        this.frame.setSize(800, 800);
        this.frame.setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.mainCards);
        this.frame.setVisible(true);


        ////////////////////////
        // CARD LAYOUT SET UP //
        ////////////////////////

        // CHOICE CARD
        //
        JPanel choiceCard = new JPanel() {
            public void paintComponent(Graphics g) {
                Image background = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background_chess.jpg"));
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        JButton startChessGame = new JButton("Start Chess Game");
        choiceCard.add(startChessGame);

        // SET NAME CARD
        //
        JPanel setNameCard = new JPanel() {
            public void paintComponent(Graphics g) {
                Image background = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background_chess.jpg"));
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        JButton setNameButton = new JButton("Set");
        JButton backButton1 = new JButton("Back");
        JTextField nameField = new JTextField(15);
        setNameCard.add(setNameButton);
        setNameCard.add(backButton1);
        setNameCard.add(nameField);

        // CHOICE CARD: HOST OR JOIN A GAME
        //
        JPanel chooseHostOrJoin = new JPanel() {
            public void paintComponent(Graphics g) {
                Image background = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background_chess.jpg"));
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        JButton hostChoice = new JButton("Host");
        JButton joinChoice = new JButton("Join");
        JButton changeName = new JButton("Change Name");
        chooseHostOrJoin.add(hostChoice);
        chooseHostOrJoin.add(joinChoice);
        chooseHostOrJoin.add(changeName);

        // CHESS-HOST-JOIN FUNCTIONALITY CARD
        //
        JPanel chessJoinCard = new JPanel() {
            public void paintComponent(Graphics g) {
                Image background = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background_chess.jpg"));
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        JButton joinButton = new JButton("Join");
        JButton backButton2 = new JButton("Back");
        JTextField ipField1 = new JTextField(3);
        JTextField ipField2 = new JTextField(3);
        JTextField ipField3 = new JTextField(3);
        JTextField ipField4 = new JTextField(3);
        chessJoinCard.add(joinButton);
        chessJoinCard.add(backButton2);
        chessJoinCard.add(ipField1);
        chessJoinCard.add(ipField2);
        chessJoinCard.add(ipField3);
        chessJoinCard.add(ipField4);

        // ADDING CARDS TO THE MAIN-CARD PANEL
        //
        this.mainCards.add(choiceCard, "1");
        this.mainCards.add(setNameCard, "2");
        this.mainCards.add(chooseHostOrJoin, "3");
        this.mainCards.add(chessJoinCard, "4");

        //////////////////////////////
        // BUTTONS ACTION LISTENERS //
        //////////////////////////////

        // START CHESS GAME FRAME
        startChessGame.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "2");
        });

        // SET NAME BUTTON AND GO TO HOST/JOIN CHOICE FRAME
        setNameButton.addActionListener(e -> {
            if (checkName(nameField.getText())) {
                name = nameField.getText();
                CardLayout cl = (CardLayout) this.mainCards.getLayout();
                cl.show(this.mainCards, "3");
            }
        });

        // BACK BUTTON TO CHOOSE A DIFFERENT GAME (SOON TO COME!)
        backButton1.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "1");
        });

        // STRAIGHT HOST A GAME
        hostChoice.addActionListener(e -> startServerAndHost());

        // GO BACK AND CHANGE NAME
        changeName.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "2");
        });

        // GO TO THE JOIN FRAME
        joinChoice.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "4");
        });

        // AFTER FILLING THE TEXT AREAS WITH RELEVANT IP, JOIN THE RESPECTIVE GAME
        joinButton.addActionListener(e -> {
            if (!getIp(ipField1.getText(),ipField2.getText(),ipField3.getText(),ipField4.getText()).equals("")) {
                startClientAndJoin(getIp(ipField1.getText(),ipField2.getText(),ipField3.getText(),ipField4.getText()));
            } else {
                System.out.println("Invalid IP");
            }
        });

        // GO BACK TO THE HOST/JOIN FRAME
        backButton2.addActionListener(e -> {
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
    }

    // METHOD WHICH IS CREATING THE CLIENT
    public void startClientAndJoin (String input) { // TODO: FIND WAYS TO IMPROVE; ADD ERROR HANDLING
        if (input.equals("")) {
            System.out.println("Invalid");
        } else {
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
}
