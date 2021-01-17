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
        this.game = new Game();
        this.port = port;
        run();
    }

    @Override
    public void run() {
        try {
            timer = new Timer();
            game.createJFrameCB().setVisible(true);
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for client...");
            socket = server.accept();
            System.out.println("Client accepted");
        } catch (IOException e) {
            e.printStackTrace();
        }
        myTurn();
    }

    public void myTurn() {
        if (game.turn()) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("trebuie sa mut");
                    if (game.getMovedPiece()) {
                        System.out.println("Output");
                        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())){
                            out.writeObject(game.getChessBoard());
                            game.changeMovedPiece();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        timer.cancel();
                    }
                }
            }, 1000, 10000);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
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
                }
            },1000 ,10000);
        } else {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
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
                }
            },1000 ,10000);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("trebuie sa mut");
                    if (game.getMovedPiece()) {
                        System.out.println("Output");
                        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())){
                            out.writeObject(game.getChessBoard());
                            game.changeMovedPiece();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        timer.cancel();
                    }
                }
            }, 1000, 10000);
        }
    }
}
