package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Game.Game;
import com.chessgame.Game.GameInitialization;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Server implements Runnable {
    private final int port;
    private Socket socket = null;
    private ServerSocket server = null;
    public JFrame frame;
    public Game game;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Server(int port) throws IOException {
        this.game = new Game( 1);
        this.port = port;
        run();
    }

    @Override
    public void run() {
        try {
            System.out.println();
            game.createJFrameCB().setVisible(true);
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for client...");
            socket = server.accept();
            System.out.println("Client accepted");
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        myTurn();
    }

    public void myTurn() {
        if (game.getGameover()) {
            return;
        }
        Timer timer = new Timer();
        if (game.isCheckMate(game.getKing("white"))) {
            game.youLost();
            JOptionPane.showMessageDialog(frame, "White lost outside");
            try {
                out.close();
                in.close();
                socket.close();
                server.close();
                System.out.println("s-a inchis tot");
            } catch (IOException e) {
                System.out.println(e);
            }
            return;
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (game.getMovedPiece()) {
                    try {
                        System.out.println("trebuie sa mut");
                        Object[] send = { game.getChessBoard() };
                        out.writeObject(send);
                        game.changeMovedPiece();
                        boolean tru = true;
                        while (tru) {
                            if (game.getGameover()) {
                                in.close();
                                out.close();
                                socket.close();
                                break;
                            }
                            try {
                                System.out.println("astept ceva");
                                ChessBoard temp = (ChessBoard) ((Object[]) in.readObject())[0];
                                temp.reverseBoard();
                                game.updateChessBoardUI(temp, game.chessboard);
                                game.chessboard.updateUI();
                                if (game.isCheckMate(game.getKing("white"))) {
                                    game.youLost();
                                    System.out.println(game.getGameover());
                                    JOptionPane.showMessageDialog(frame, "White lost inside");
                                    timer.cancel();
                                } else {
                                    game.changeTurn();
                                    myTurn();
                                    timer.cancel();
                                    return;
                                }

                            } catch (IOException | ClassNotFoundException e) {
                                tru = false;
                                System.out.println("linia 101");
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Linia 106");
                        System.out.println(e);
                    }
                }
            }
        }, 100, 10000);
    }
}
