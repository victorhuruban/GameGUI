package com.chessgame.ServerClient;

import com.chessgame.Board.ChessBoard;
import com.chessgame.Game.Game;
import com.chessgame.Player.Player;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    private final String address;
    private final int port;
    private Socket socket = null;
    public Game game = null;
    public JFrame frame = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean myturn = false;
    private boolean moved = false;

    public Client(String address, int port) throws IOException {
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

        while (true) {
            if (game.isCheckMate(game.getKing("black"))) {
                System.out.println("Black lost");
                break;
            }
            System.out.println("Connected");
            if (!game.turn()) {
                System.out.println("Need to move");
                while(!game.getMovedPiece()) {
                    System.out.println("Acilea");
                    if (game.getMovedPiece()) {
                        System.out.println("moved");
                        game.changeMovedPiece();
                        break;
                    }
                }
                System.out.println("Output");
                try {
                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(game.getChessBoard());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Waiting");
            try {
                in = new ObjectInputStream(socket.getInputStream());
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
            socket = new Socket(address, port);
            System.out.println("Connected");
            game = new Game();
            game.createJFrameCB().setVisible(true);


            while (!game.isGameover()) {
                System.out.println("Here");
                if (!game.turn()) {
                    System.out.println("Need to move");
                    while (!game.getMovedPiece()) {
                        System.out.println("Acilea");
                        if (game.getMovedPiece()) {
                            System.out.println("Moved");
                            game.changeMovedPiece();
                            break;
                        }
                    }
                    System.out.println("Output");
                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(game.getChessBoard());

                } else {
                    System.out.println("Waiting");
                    in = new ObjectInputStream(socket.getInputStream());
                    try {
                        System.out.println("Updating");
                        game.updateChessBoardUI((ChessBoard) in.readObject(), game.chessboard);

                        game.changeTurn();
                        game.chessboard.updateUI();
                        moved = false;
                        in = null;
                    } catch (EOFException i) {
                        break;
                    }
                }
            }

            socket.close();
        } catch (IOException | ClassNotFoundException i) {
            System.out.println(i);
        }*/
    }
}
