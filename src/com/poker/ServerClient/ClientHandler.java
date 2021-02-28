package com.poker.ServerClient;

import com.poker.Lobby.Lobby;
import com.poker.Pack.Card;
import com.poker.Pack.Pack;

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
    private static ArrayList<Integer> start = new ArrayList<>();

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
                    outToAll("Im trying to start the game");
                } else if (req[req.length - 1].equals("2")) {
                    start.add(Integer.parseInt(req[0]));
                    if (start.size() == clients.size()) {
                        if (start.contains(0)) {
                            System.out.println("Nu toti sunt gata sa inceapa");
                        } else {
                            Pack pack = new Pack();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < 5 + (clients.size() * 2); i++) {
                                Card c = pack.popCard();
                                sb.append(" ").append(c.getValue()).append(" ").append(c.getType());
                            }
                            outToAll("start 3" + sb.toString());
                        }
                        start.clear();
                    }
                } else if (req[req.length - 1].equals("4")) {
                    outToAll(req[0] + " did something 0 0 0 4");
                } else if (req[req.length - 1].equals("5")) {
                    outToAll(req[0] + " is folded 0 0 0 5");
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
