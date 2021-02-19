package com.poker.ServerClient;

import com.poker.Lobby.Lobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;
    private Lobby lobby;

    private int conNum;

    public ClientHandler(Socket client, ArrayList<ClientHandler> clients, int conNum) throws IOException {
        this.client = client;
        this.clients = clients;
        this.conNum = conNum;
        this.lobby = ClientP.getLobby();
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        if (conNum != 0) {
            int count = 0;
            for (ClientHandler c: clients) {
                c.out.println(count + " " + lobby.getName());
                count++;
            }
        }
        lobby.setJPanel(conNum, lobby.getName());
        try {
            while (true) {
                String request = in.readLine();
                if (request.contains("1")) {
                    outToAll("sa ma");
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
