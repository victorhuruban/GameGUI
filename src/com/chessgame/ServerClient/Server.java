package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Game.Game;
import com.chessgame.Player.Player;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final int port;
    private Socket socket = null;
    private ServerSocket server = null;
    public JFrame frame;
    private boolean myturn = true;
    private boolean moved = false;
    public Game game;

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
                while (!game.getMovedPiece()) {
                    System.out.print("-");
                    if (game.getMovedPiece()) {
                        System.out.println("moved");
                        game.changeMovedPiece();
                        break;
                    }
                }
                System.out.println("Output");
                try {
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(game.getChessBoard());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Waiting");
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                System.out.println("Updating");
                game.updateChessBoardUI((ChessBoard) in.readObject(), game.chessboard);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e);
            }

            game.changeTurn();
            game.chessboard.updateUI();
            moved = false;
        }
        /*try {
            game.createJFrameCB().setVisible(true);
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for client...");
            socket = server.accept();
            System.out.println("Client accepted");
            System.out.println(game.turn());

            while (!game.isGameover()) {
                if (game.turn()) {
                    System.out.println("Need to move");
                    while (!game.getMovedPiece()) {
                        System.out.println(game.getMovedPiece());
                        if (game.getMovedPiece()) {
                            break;
                        }
                    }
                    System.out.println("Moved");
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(game.getChessBoard());
                } else {
                    System.out.println("Waiting");
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    try {
                        System.out.println("Updated");
                        game.updateChessBoardUI((ChessBoard) in.readObject(), game.chessboard);
                        game.changeTurn();
                        game.chessboard.updateUI();
                        moved = false;
                    } catch (EOFException i) { break; }
                }
            }

            System.out.println("Closing connection");
            socket.close();
        } catch (IOException | ClassNotFoundException i) {
            System.out.println(i);
        }*/
    }
}
