package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Board.Loc;
import com.chessgame.Game.Game;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Client implements Runnable {
    private final String address;
    private final int port;
    private Socket socket = null;
    public Game game;
    public JFrame frame;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String address, int port) throws IOException {
        this.game = new Game(2);
        this.address = address;
        this.port = port;
        run();
    }

    @Override
    public void run() {
        try {
            game.changeTurn();
            game.createJFrameCB().setVisible(true);
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            myTurn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void myTurn() throws IOException {
        if (game.getGameover()) {
            return;
        }

        if (game.isCheckMate(game.getKing("black"))) {
            game.youLost();
            JOptionPane.showMessageDialog(frame, "Black lost outside");
            return;
        }

        boolean tru = true;
        while (tru) {
            try {
                ChessBoard newCB = game.getChessBoard();
                Loc[][] temp = (Loc[][]) in.readObject();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (temp[i][j].getPiece() != null) {
                            newCB.getLocation(i ,j).setPiece(temp[i][j].getPiece());
                        } else {
                            newCB.getLocation(i, j).setPiece(null);
                        }
                    }
                }
                newCB.reverseBoard();
                game.updateChessBoardUI(newCB, game.chessboard);
                game.chessboard.updateUI();
                tru = false;
            } catch (IOException | ClassNotFoundException e) {
                in.close();
                out.close();
                socket.close();
                return;
            }
        }
        if (game.isCheckMate(game.getKing("black"))) {
            game.youLost();
            JOptionPane.showMessageDialog(frame, "Black lost outside.");
            in.close();
            out.close();
            socket.close();
            return;
        }
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (game.getMovedPiece()) {
                    System.out.println(timer.toString());
                    try {
                        Loc[][] transfer = new Loc[8][8];
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                transfer[i][j] = game.getChessBoard().getLocation(i, j);
                            }
                        }
                        out.flush();
                        out.writeObject(transfer);
                        out.flush();
                        game.changeMovedPiece();
                        game.changeTurn();
                        timer.cancel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            myTurn();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 100, 5000);
    }
}