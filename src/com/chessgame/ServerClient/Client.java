package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
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
    public Game game = null;
    public JFrame frame = null;
    private boolean myturn = false;
    private boolean moved = false;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String address, int port) throws IOException {
        this.address = address;
        this.port = port;
        run();
    }

    @Override
    public void run() {
        try {
            game = new Game();
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
            System.out.println("Ar trebuii sa iasa dar nu vrea");
            return;
        }

        if (game.isCheckMate(game.getKing("black"))) {
            game.youLost();
            JOptionPane.showMessageDialog(frame, "Black lost outside");
            return;
        }

        boolean tru = true;
        while (tru) {
            System.out.println("acilea");
            try {
                Object[] rec = (Object[]) in.readObject();
                ChessBoard temp = (ChessBoard) rec[0];
                System.out.println("a trecut de in");
                System.out.println("updating");
                game.updateChessBoardUI(temp, game.chessboard);
                game.chessboard.updateUI();
                tru = false;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("If you win, this runs after they click 'ok' on the JOptionPane");
                in.close();
                out.close();
                socket.close();
                return;
            }
        }
        if (game.isCheckMate(game.getKing("black"))) {
            game.youLost();
            JOptionPane.showMessageDialog(frame, "Black lost outside");
            in.close();
            out.close();
            socket.close();
            return;
        }
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Trebuie sa mut");
                if (game.getMovedPiece()) {
                    System.out.println("Output");
                    try {
                        Object[] send = {game.getChessBoard()};
                        out.writeObject(send);
                        game.changeMovedPiece();
                        if (true) {
                            game.changeTurn();
                            myTurn();
                            timer.cancel();
                            return;
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }
}
