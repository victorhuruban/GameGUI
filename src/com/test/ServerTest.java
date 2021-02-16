package com.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTest {

    private final static int PORT = 5789;
    private static String[] names = {"Wily", "Felix", "Carlsbad", "Hobob"};
    private static String[] adjs = {"the gentle", "the un-gentle", "the overwrought", "the urbane"};

    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws IOException {

        ServerSocket listener = new ServerSocket(PORT);

        while (true) {
            System.out.println("[SERVER] Waiting for client connection...");
            Socket client = listener.accept();
            System.out.println("[SERVER] Accepted client.");
            ClientHandler clientThread = new ClientHandler(client);
            clients.add(clientThread);

            pool.execute(clientThread);
        }
    }

    public static String getRandomName() {
        String name = names[ (int) (Math.random() * names.length)];
        String adj = adjs[ (int) (Math.random() * adjs.length)];
        return name + " " + adj;
    }
}
