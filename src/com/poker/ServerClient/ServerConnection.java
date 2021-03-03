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
                    lobby.addNameAndCon(test[1], Integer.parseInt(test[0]));
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
                } else if (test.length == 7) {
                    int index;
                    System.out.println(test[6]);
                    switch (test[6]) {
                        case "8":
                            System.out.println("8");
                            lobby.ifAllMovedAndEqual();
                            break;
                        case "7":
                            System.out.println("7");
                            index = 0;
                            for (String s: lobby.playersNames) {
                                System.out.println(index + " " + s);
                                if (s.equals(test[0])) {
                                    lobby.players[index] = Integer.parseInt(test[test.length - 2]);
                                    lobby.playersState[index] = true;
                                    break;
                                } else index++;
                            }
                            lobby.printPlayersandPlayersState();
                            lobby.ifAllMovedAndEqual();
                            break;
                        case "5":
                            System.out.println("5");
                            index = 0;
                            for (String s: lobby.playersNames) {
                                if (s.equals(test[0])) {
                                    lobby.playersActive[index] = false;
                                    lobby.playersState[index] = true;
                                    break;
                                } else index++;
                            }
                            lobby.ifAllMovedAndEqual();
                            break;
                        case "6":
                            System.out.println("6");
                            lobby.nextTurn();
                            break;
                        case "4":
                            System.out.println("4");
                            index = 0;
                            for (String s: lobby.playersNames) {
                                if (s.equals(test[0])) {
                                    lobby.players[index] = 0;
                                    lobby.playersState[index] = true;
                                } else index++;
                            }
                            System.out.println(lobby.playersNames);
                            lobby.printPlayersandPlayersState();
                            lobby.ifAllMovedAndEqual();
                            break;
                        case "9":
                            System.out.println("9");
                            index = 0;
                            for (String s: lobby.playersNames) {
                                if (s.equals(test[0])) {
                                    lobby.players[index] = Integer.parseInt(test[test.length - 2]);
                                    lobby.playersState[index] = true;
                                    break;
                                } else index++;
                            }
                            lobby.ifAllMovedAndEqual();
                            break;
                    }
                } else if (test.length >= 12) {
                    if (test[1].equals("3")) {
                        System.out.println(Arrays.toString(test));
                        lobby.createGame(serverResponse);
                        lobby.printCandP();
                    }
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
