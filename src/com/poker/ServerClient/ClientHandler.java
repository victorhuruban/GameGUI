package com.poker.ServerClient;

import com.poker.Lobby.Lobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;
    private Set<Integer> start = new HashSet<>();

    private final int conNum;

    public ClientHandler(Socket client, ArrayList<ClientHandler> clients, int conNum) throws IOException {
        this.client = client;
        this.clients = clients;
        this.conNum = conNum;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String request = in.readLine();
                String[] req = request.split(" ");
                if (req[req.length - 1].equals("0")) {
                    outToAll(conNum + " " + req[0]);
                } else if (req[req.length - 1].equals("1") && req[0].equals("Ready")) {
                    outToAll("Ready " + req[1] + " " + conNum);
                } else if (req[req.length - 1].equals("1") && req[0].equals("NotReady")) {
                    outToAll("NotReady " + req[1] + " " + conNum);
                } else if (req.length == 1 && req[0].equals("try2start")) {
                    System.out.println("acilea");
                    outToAll("Im trying to start the game");
                } else if (req.length == 1) {
                    System.out.println(req[0]);
                    start.add(Integer.parseInt(req[0]));
                    System.out.println(start);
                }
            }
        } catch (IOException e) {
            System.err.println("IO exception in client handler");
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void outToAll(String substring) {
        for (ClientHandler cl: clients) {
            cl.out.println(substring);
        }
    }
}
