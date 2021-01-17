package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Game.Game;
import com.chessgame.Player.Player;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.sql.Time;
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
    private Timer timer1;

    public Client(String address, int port) throws IOException {
        this.timer = new Timer();
        this.timer1 = new Timer();
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
        myTurn(game.turn());
    }
    public void myTurn(boolean turn) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("waiting");
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    System.out.println("o trecut de in");
                    System.out.println("updating");
                    game.updateChessBoardUI((ChessBoard) in.readObject(), game.chessboard);
                    timer.cancel();
                    timer1.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("trebuie sa mut");
                            if (game.getMovedPiece()) {
                               try {
                                   ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                                   out.writeObject(game.getChessBoard());
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                               game.changeMovedPiece();
                            }
                        }
                    }, 1000, 1000);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 1000);
    }
}
