package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Game.Game;

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
    private boolean myturn = true;
    private boolean moved = false;
    public Game game;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Server(int port) throws IOException {
        this.game = new Game();
        this.port = port;
        run();
    }

    @Override
    public void run() {
        try {
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
            System.out.println("Ar trebuii sa iasa dar nu vrea");
            return;
        }
        Timer timer = new Timer();
        if (game.isCheckMate(game.getKing("white"))) {
            game.youLost();
            JOptionPane.showMessageDialog(frame, "White lost outside");
            return;
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(game.getGameover());
                System.out.println("trebuie sa mut");
                if (game.getMovedPiece()) {
                    System.out.println("Output");
                    try {
                        Object[] send = {game.getChessBoard()};
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
                                ChessBoard temp = (ChessBoard) ((Object[]) in.readObject())[0];
                                System.out.println("a trecut de out");
                                game.updateChessBoardUI(temp, game.chessboard);
                                game.chessboard.updateUI();
                                if (game.isCheckMate(game.getKing("white"))) {
                                    game.youLost();
                                    System.out.println(game.getGameover());
                                    JOptionPane.showMessageDialog(frame, "White lost inside");
                                    timer.cancel();
                                } else {
                                    System.out.println("A trecut pe aici");
                                    game.changeTurn();
                                    myTurn();
                                    timer.cancel();
                                    return;
                                }

                            } catch (IOException | ClassNotFoundException e) {
                                tru = false;
                                System.out.println(e);
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1000, 1000);
    }
}
