package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Game.Game;
import com.chessgame.Player.Player;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
    private final int port;
    private Socket socket = null;
    private ServerSocket server = null;
    public JFrame frame;
    private boolean myturn = true;
    private boolean moved = false;
    public Game game;
    private Timer timer;

    public Server(int port) throws IOException {
        timer = new Timer();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            if (game.turn()) {
                if (game.isCheckMate(game.getKing("white"))) {
                    System.out.println("White lost");
                    break;
                }
                System.out.println(game.isCheckMate(game.getKing("white")));
                System.out.println("Need to move");
                if (!game.getMovedPiece()) {
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("trebuie sa mut");
                            if (game.getMovedPiece()) {
                                timer.cancel();
                            }
                        }
                    }, 1000, 1000);
                }
                /*while (!game.getMovedPiece()) {
                    if (game.getMovedPiece()) {
                        System.out.println("moved");
                        game.changeMovedPiece();
                        break;
                    }
                }*/
                System.out.println("Output");
                try {
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(game.getChessBoard());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Waiting");
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    if (in.available() == 0) {
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("astept sa primesc ceva");
                                try {
                                    if (in.available() != 0) {
                                        timer.cancel();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000, 1000);
                    }
                    System.out.println("Updating");
                    game.updateChessBoardUI((ChessBoard) in.readObject(), game.chessboard);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e);
                }

                game.changeTurn();
                game.chessboard.updateUI();
                moved = false;
            }
        }
    }
}
