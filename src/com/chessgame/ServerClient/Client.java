package com.chessgame.ServerClient;

import com.GameGUI;
import com.chessgame.Board.ChessBoard;
import com.chessgame.Board.Loc;
import com.chessgame.Game.Game;
import com.chessgame.Game.TurnCircle;
import com.chessgame.Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Client implements Runnable {
    private final String name;
    private final String address;
    private final int port;
    private Socket socket = null;
    public Game game;
    public JFrame gFrame;
    public JFrame frame;
    public TurnCircle myCircle; public TurnCircle oppCircle;
    public JLabel myName;
    public JLabel oppName;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Loc[][] transfer;
    private String changedPiece;

    public Client(String address, int port, String name) throws IOException {
        this.name = name;
        this.game = new Game(2, name);
        this.address = address;
        this.port = port;
        oppCircle = new TurnCircle(); myCircle = new TurnCircle();
        changedPiece = "";
        run();
    }

    @Override
    public void run() {
        try {
            game.changeTurn();
            System.out.println(game.getTurn());
            gFrame = game.createJFrameCB();
            gFrame.setVisible(true);
            game.getMyNameL().add(new JLabel());
            myName = (JLabel) game.getMyNameL().getComponent(0);
            myName.setText(" My name: " + name);
            myCircle.initialSetCircle("black", !game.getTurn());
            game.getMyNameL().add(myCircle);
            game.setCanMove();
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            try {
                System.out.println("Aci");
                Object[] trans = (Object[]) in.readObject();
                game.getOpponentsNameL().add(new JLabel());
                oppName = (JLabel) game.getOpponentsNameL().getComponent(0);
                oppName.setText(" Opponent's name: " + trans[0]);
                oppCircle.initialSetCircle("white", !game.getTurn());
                game.getOpponentsNameL().add(oppCircle);
                trans[0] = name ;
                out.writeObject(trans);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("CE PLM");
                closeEverything();
            }
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
            JOptionPane.showMessageDialog(frame, "Black lost");
            closeEverything();
            GameGUI restart = new GameGUI();
            restart.afterEnd(name);
            return;
        }

        boolean tru = true;
        while (tru) {
            try {
                ChessBoard newCB = game.getChessBoard();
                System.out.println("acu aci");
                Object[] trans = (Object[]) in.readObject();
                game.getLogTA().append(trans[1].toString());
                game.setCanMove();
                copyLocFromTransfer((Loc[][]) trans[0], newCB);
                newCB.reverseBoard();
                game.updateChessBoardUI(newCB, game.chessboard);
                game.chessboard.updateUI();
                game.playSound();
                changeTurnCircles();
                tru = false;
            } catch (IOException | ClassNotFoundException e) {
                closeEverything();
                gFrame.dispose();
                GameGUI restart = new GameGUI();
                restart.afterEnd(name);
                break;
            }
        }
        if (game.isCheckMate(game.getKing("black"))) {
            game.youLost();
            JOptionPane.showMessageDialog(frame, "Black lost outside.");
            closeEverything();
            gFrame.dispose();
            GameGUI restart = new GameGUI();
            restart.afterEnd(name);
            return;
        }
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (game.getMovedPiece()) {
                    System.out.println(timer.toString());
                    try {
                        if (game.getEndPawn()) {
                            System.out.println("Ajuns la final");
                            changeEndPawn();
                            game.changeEndPawn();
                            game.changeMovedPiece();
                        } else {
                            if (!changedPiece.equals("")) {
                                System.out.println("Ales chestia");
                                String[] elems = changedPiece.split(" ");
                                game.getChessBoard().getLocation(Integer.parseInt(elems[1]), Integer.parseInt(elems[2]))
                                        .setPiece(transformPawn(elems[0], Integer.parseInt(elems[1]), Integer.parseInt(elems[2])));
                                game.updateChessBoardUI(game.getChessBoard(), game.chessboard);
                                game.chessboard.updateUI();
                                changedPiece = "";
                            }
                            System.out.println("trimis");
                            transfer = new Loc[8][8];
                            copyLocForTransfer(transfer, game.getChessBoard());
                            Object[] trans = {transfer, game.getSB()};
                            out.flush();
                            out.writeObject(trans);
                            out.flush();
                            game.getSB().delete(0, game.getSB().length());
                            game.changeMovedPiece();
                            game.setCanMove();
                            changeTurnCircles();
                            timer.cancel();
                            try {
                                game.changeTurn();
                                myTurn();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 100, 250);
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
                return new Queen(row, column, "black");
            case "Rook":
                return new Rook(row, column, "black");
            case "Knight":
                return new Knight(row, column, "black");
            default:
                return new Bishop(row, column, "black");
        }
    }

    private void launchChangeFrame(int row, int column) {
        JFrame frame = new JFrame("Change pawn");
        frame.setLayout(new FlowLayout());
        frame.setSize(250, 125);
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

    public void changeTurnCircles() {
        myCircle.setTurn(!game.getCanMove());
        oppCircle.setTurn(!game.getCanMove());
        myCircle.repaint();
        oppCircle.repaint();
    }

    public void closeEverything() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}