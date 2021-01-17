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
        myTurn(game.turn());
    }

    public void myTurn(boolean turn) {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("trebuie sa mut");
                if (game.getMovedPiece()) {
                    System.out.println("Output");
                    try {
                        Object[] send = {game.getChessBoard()};
                        out.writeObject(send);
                        game.changeMovedPiece();
                        boolean tru = true;
                        while (tru) {
                            System.out.println("acilea");
                            try {
                                ChessBoard temp = (ChessBoard) ((Object[]) in.readObject())[0];
                                System.out.println("a trecut de out");
                                System.out.println("Updating");
                                game.updateChessBoardUI(temp, game.chessboard);
                                game.chessboard.updateUI();
                                if (true) {
                                    game.changeTurn();
                                    myTurn(game.turn());
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
