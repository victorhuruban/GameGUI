package com.poker.ServerClient;

import com.poker.Lobby.Lobby;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

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
                if (test.length == 2) {
                    if (lobby.getPanel(Integer.parseInt(test[0])).getComponents().length == 0) {
                        lobby.setJPanel(Integer.parseInt(test[0]), test[1]);
                        lobby.setSendBack();
                    }
                } else if (test.length == 3) {
                    JPanel temp = lobby.getPanel(Integer.parseInt(test[2]));
                    JLabel tempLabel = (JLabel) temp.getComponent(1);
                    if (test[0].equals("Ready")) {
                        tempLabel.setText("Ready");
                    } else tempLabel.setText("Not Ready");
                    lobby.getPanel(Integer.parseInt(test[2])).updateUI();
                } else if (test.length == 6) {
                    lobby.setSendReadyStat();
                } else if (test.length == 10) {
                    System.out.println(Arrays.toString(test));
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
