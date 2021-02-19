package com.poker.ServerClient;

import com.poker.Lobby.Lobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private Socket server;
    private BufferedReader in;

    private Lobby lobby;

    public ServerConnection(Socket s, Lobby lobby) throws IOException {
        this.lobby = lobby;
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));

    }

    @Override
    public void run() {
        try {
            while (true) {
                String serverResponse = in.readLine();

                if (serverResponse == null) break;

                String[] test = serverResponse.split(" ");
                if (lobby.getPanel(Integer.parseInt(test[0])).getComponents().length == 0) {
                    lobby.setJPanel(Integer.parseInt(test[0]), test[1]);
                    System.out.println(test[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
