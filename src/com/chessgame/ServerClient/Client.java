package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Game.Game;
import com.chessgame.Player.Player;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client implements Runnable {
    private final String address;
    private final int port;
    private Socket socket = null;
    public Game game = null;
    public JFrame frame = null;
    private boolean myturn = false;
    private boolean moved = false;
    private Timer timer;

    public Client(String address, int port) throws IOException {
        this.timer = new Timer();
        this.address = address;
        this.port = port;
        run();
    }

    @Override
    public void run() {
        try {
            game = new Game();
            game.createJFrameCB().setVisible(true);
            socket = new Socket(address, port);
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
