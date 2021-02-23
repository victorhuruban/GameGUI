package com.poker.ServerClient;

import com.poker.Lobby.Lobby;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerP implements Runnable {

    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(6);

    private int conNum = 0;

    private final ServerSocket listener;

    public ServerP(int port) throws IOException {
        listener = new ServerSocket(port);
        run();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("[SERVER] Waiting for client connection...");
            Socket client = null;
            ClientHandler clientThread = null;
            try {
                client = listener.accept();
                System.out.println("[SERVER] Accepted client.");
                clientThread = new ClientHandler(client, clients, conNum);
            } catch (IOException e) {
                e.printStackTrace();
            }
            clients.add(clientThread);
            if (clientThread != null) {
                pool.execute(clientThread);
            }
            conNum++;
        }
    }
}
