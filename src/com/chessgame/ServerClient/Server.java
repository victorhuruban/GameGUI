package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Board.Loc;
import com.chessgame.Game.Game;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Server implements Runnable {
    private final String name;
    private final int port;
    private Socket socket = null;
    private ServerSocket server = null;
    public JFrame frame;
    public Game game;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Loc[][] transfer;

    public Server(int port, String name) throws IOException {
        this.name = name;
        this.game = new Game( 1);
        this.port = port;
        run();
    }

    @Override
    public void run() {
        try {
            System.out.println();
            game.createJFrameCB().setVisible(true);
            game.getMyNameL().setText(" My name:                  " + name + " ");
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for client...");
            socket = server.accept();
            System.out.println("Client accepted");
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            try {
                Object[] trans = { name };
                out.writeObject(trans);
                trans = (Object[]) in.readObject();
                game.getOpponentsNameL().setText("Opponent's name:  " + trans[0]);
            } catch (IOException e) {
                System.out.println("CE PLM");
            } catch (ClassNotFoundException e) {
                System.out.println("CE PLM 2");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        myTurn();
    }

    public void myTurn() {
        if (game.getGameover()) {
            return;
        }

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
                e.printStackTrace();
            }
            return;
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (game.getMovedPiece()) {
                    try {
                        transfer = new Loc[8][8];
                        copyLocForTransfer(transfer, game.getChessBoard());
                        Object[] trans = {transfer, game.getSB()};
                        out.flush();
                        out.writeObject(trans);
                        out.flush();
                        game.getSB().delete(0, game.getSB().length());
                        game.changeMovedPiece();
                        game.setCanMove();
                        timer.cancel();
                    } catch (IOException e) {
                        System.out.println("Linia 86");
                        e.printStackTrace();
                    }
                }
            }
        }, 50, 5000);
        boolean tru = true;
        while (tru) {
            if (game.getGameover()) {
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Linia 100");
                    e.printStackTrace();
                }
                break;
            }
            try {
                System.out.println("astept ceva");
                ChessBoard newCB = game.getChessBoard();
                Object[] trans = (Object[]) in.readObject();
                game.getLogTA().append(trans[1].toString());
                game.setCanMove();
                copyLocFromTransfer((Loc[][]) trans[0], newCB);
                newCB.reverseBoard();
                game.updateChessBoardUI(newCB, game.chessboard);
                game.chessboard.updateUI();
                if (game.isCheckMate(game.getKing("white"))) {
                    game.youLost();
                    System.out.println(game.getGameover());
                    JOptionPane.showMessageDialog(frame, "White lost inside");
                } else {
                    game.changeTurn();
                    myTurn();
                }
                break;


            } catch (IOException | ClassNotFoundException e) {
                tru = false;
                System.out.println(timer.toString());
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }
    private void copyLocFromTransfer(Loc[][] transfer, ChessBoard cb) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (transfer[i][j].getPiece() != null) {
                    cb.getLocation(i ,j).setPiece(transfer[i][j].getPiece());
                } else {
                    cb.getLocation(i, j).setPiece(null);
                }
            }
        }
    }

    private void copyLocForTransfer(Loc[][] transfer, ChessBoard cb) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                transfer[i][j] = cb.getLocation(i, j);
            }
        }
    }
}
