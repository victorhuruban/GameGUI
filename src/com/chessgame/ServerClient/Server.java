package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Board.Loc;
import com.chessgame.Game.Game;
import com.chessgame.Pieces.*;

import javax.swing.*;
import java.awt.*;
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
    private String changedPiece;

    public Server(int port, String name) throws IOException {
        this.name = name;
        this.game = new Game( 1);
        this.port = port;
        changedPiece = "";
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
                        if (game.getEndPawn()) {
                            System.out.println("92");
                            changeEndPawn();
                            game.changeEndPawn();
                            game.changeMovedPiece();
                        } else {
                            if (!changedPiece.equals("")) {
                                System.out.println("98");
                                String[] elems = changedPiece.split(" ");
                                game.getChessBoard().getLocation(Integer.parseInt(elems[1]), Integer.parseInt(elems[2]))
                                        .setPiece(transformPawn(elems[0], Integer.parseInt(elems[1]), Integer.parseInt(elems[2])));
                                game.updateChessBoardUI(game.getChessBoard(), game.chessboard);
                                game.chessboard.updateUI();
                                changedPiece = "";
                            }
                            System.out.println("106");
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
                        }
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

    private void changeEndPawn() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (game.getChessBoard().getLocation(i, j).isOccupied()) {
                    if (game.getChessBoard().getLocation(i, j).getPiece().toString().equals("Pawn") &&
                            game.getChessBoard().getLocation(i, j).getPiece().getRow() == 0) {
                        launchChangeFrame(i, j);
                        break;
                    }
                }
            }
        }
    }

    private Piece transformPawn(String piece, int row, int column) {
        switch (piece) {
            case "Queen":
                return new Queen(row, column, "white");
            case "Rook":
                return new Rook(row, column, "white");
            case "Knight":
                return new Knight(row, column, "white");
            default:
                return new Bishop(row, column, "white");
        }
    }

    private void launchChangeFrame(int row, int column) {
        JFrame frame = new JFrame("Change pawn");
        frame.setLayout(new FlowLayout());
        frame.setSize(250, 1250);
        frame.setVisible(true);

        JButton queenC = new JButton("Queen");
        JButton rookC = new JButton("Rook");
        JButton knightC = new JButton("Knight");
        JButton bishopC = new JButton("Bishop");
        frame.add(queenC);
        frame.add(rookC);
        frame.add(knightC);
        frame.add(bishopC);

        queenC.addActionListener(e -> {
            changedPiece = "Queen " + row + " " + column;
            game.changeMovedPiece();
            frame.dispose();
        });

        rookC.addActionListener(e -> {
            changedPiece = "Rook " + row + " " + column;
            game.changeMovedPiece();
            frame.dispose();
        });

        knightC.addActionListener(e -> {
            changedPiece = "Knight " + row + " " + column;
            game.changeMovedPiece();
            frame.dispose();
        });

        bishopC.addActionListener(e -> {
            changedPiece = "Bishop " + row + " " + column;
            game.changeMovedPiece();
            frame.dispose();
        });
    }
}
