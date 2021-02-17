package com.poker.ServerClient;

import com.poker.Lobby.Lobby;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerP implements Runnable {

    private static String[] names = {"Wily", "Felix", "Carlsbad", "Hobob"};
    private static String[] adjs = {"the gentle", "the un-gentle", "the overwrought", "the urbane"};

    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    private ServerSocket listener;

    public ServerP(int port) throws IOException {
        listener = new ServerSocket(port);
        run();
    }

    public static String getRandomName() {
        String name = names[ (int) (Math.random() * names.length)];
        String adj = adjs[ (int) (Math.random() * adjs.length)];
        return name + " " + adj;
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
                clientThread = new ClientHandler(client, clients);
            } catch (IOException e) {
                e.printStackTrace();
            }
            clients.add(clientThread);
            pool.execute(clientThread);
        }
    }
}
