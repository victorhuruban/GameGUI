package com.chessgame;

import com.chessgame.ServerClient.Client;
import com.chessgame.ServerClient.Server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameGUI {
    private JFrame frame;
    private JPanel mainCards;
    private JPanel choiceCard;
    private JPanel chessHostJoinCard;


    public GameGUI() {
        this.frame = new JFrame("Games");
        this.mainCards = new JPanel(new CardLayout());

        // CHOICE CARD
        this.choiceCard = new JPanel() {
            public void paintComponent(Graphics g) {
                Image background = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background_chess.jpg"));
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        JButton button = new JButton("Start Chess Game");
        this.choiceCard.add(button);
        // END OF CHOICE GAME

        // CHESS-HOST-JOIN FUNCTIONALITY
        this.chessHostJoinCard = new JPanel() {
            public void paintComponent (Graphics g) {
                Image background = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background_chess.jpg"));
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        JButton button1 = new JButton("Host");
        JButton button2 = new JButton("Join");
        JButton button3 = new JButton("Back");
        JTextField field = new JTextField(20);
        chessHostJoinCard.add(button1);
        chessHostJoinCard.add(button2);
        chessHostJoinCard.add(button3);
        chessHostJoinCard.add(field);
        // END OF CHESS-HOST-JOIN CARD

        // ADDING CARDS TO THE MAIN-CARD PANEL
        this.mainCards.add(this.choiceCard, "1");
        this.mainCards.add(this.chessHostJoinCard, "2");

        // FRAME SET UP
        this.frame.setSize(800, 800);
        this.frame.setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.mainCards);
        this.frame.setVisible(true);

        // BUTTON ACTION LISTENERS
        button.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "2");
        });

        button1.addActionListener(e -> {
            startServerOrClient(field.getText());
        });

        button2.addActionListener(e -> {
            startServerOrClient(field.getText());
        });

        button3.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.mainCards.getLayout();
            cl.show(this.mainCards, "1");
        });
    }

    // METHOD WHICH IS CREATING THE SERVER SOCKET AND CLIENT
    public void startServerOrClient (String input) { // TODO: FIND WAYS TO IMPROVE; ADD ERROR HANDLING
        Runnable runnable;

        if (input.equals("")) {
            System.out.println("Nu i bun inputu");
        } else {
            if (input.length() >= 1 && input.length() <= 5) {
                runnable = () -> {
                    try {
                        Server server = new Server(Integer.parseInt(input));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                };
                Thread serverThread = new Thread(runnable);
                serverThread.start();
                getFrame().setVisible(false);
                serverThread.interrupt();
            } else {
                String[] temp = input.split(" ");
                runnable = () -> {
                    try {
                        Client client = new Client(temp[0], Integer.parseInt(temp[1]));
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

    public JFrame getFrame() {
        return this.frame;
    }
}
