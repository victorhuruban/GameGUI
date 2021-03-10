package com.poker.ServerClient;

import com.poker.Lobby.Lobby;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientP {
    private Lobby lobby;

    private PrintWriter out;

    private static final int SERVER_PORT = 57894;
    private int ready = 0;

    public ClientP(String ip, Lobby lobby) throws IOException {
        this.lobby = lobby;

        Socket socket = new Socket(ip, SERVER_PORT);

        ServerConnection serverConnection = new ServerConnection(socket, lobby);

        out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(serverConnection).start();
        if (lobby.type != 0) {
            out.println(lobby.getName() + " " + lobby.readyOrNot() + " " + 0);
        }

        runLoop();
    }

    public void runLoop() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (lobby.getReadyPressed()) {
                    lobby.setReadyStat(lobby.getState());
                    lobby.changeReadyPressed();
                    if (lobby.getState() == 1) {
                        out.println("Ready " + lobby.getName() + " " + 1);
                    } else {
                        out.println("NotReady " + lobby.getName() + " " + 1);
                    }
                    timer.cancel();
                    runLoop();
                } else if (lobby.getExit()) {
                    timer.cancel();
                } else if (lobby.getSendBack()) {
                    out.println(lobby.getName() + " " + lobby.readyOrNot() + " " + 0);
                    lobby.setSendBack();
                    timer.cancel();
                    runLoop();
                } else if (lobby.getTryStart()) {
                    out.println("try2start");
                    lobby.setTryStart();
                } else if (lobby.getSendReadyStat()) {
                    if (lobby.getReadyPressedForTransfer()) {
                        out.println(1 + " 2");
                    } else {
                        out.println(0 + " 2");
                    }
                    lobby.setSendReadyStat();
                }
                if (lobby.getPlayer() != null) {
                    if (lobby.turn == lobby.cons.get(0) && lobby.allMovedOnce() && lobby.getInteracted()) {
                        System.out.println("CLIENTP 1");
                        lobby.setInteracted();
                        lobby.setRaised();
                        out.println(lobby.getName() + " out 0 0 0 " + lobby.rValue + " 9");
                        lobby.rValue = -1;
                    }
                    if (!lobby.getInTurn()) {
                        System.out.println("CLIENTP 2");
                        lobby.setInteracted();
                        out.println(lobby.getName() + " out 0 0 0 0 6");
                    }
                    if (lobby.getPlayer().getFolded() && lobby.getInTurn()) {
                        System.out.println("CLIENTP 3");
                        lobby.outOfTurn();
                        lobby.setInteracted();
                        out.println(lobby.getName() + " folded 0 0 0 false 5");
                    }
                    if (lobby.transmitScore()) {
                        System.out.println("CLIENTP 4");
                        out.println(lobby.getPlayer().getRank().getScoreValue() + " " + lobby.getName() + " 0 0 0 0 8");
                        lobby.setTransmitScore();
                    }
                    if (lobby.getInteracted() && lobby.getInTurn() && lobby.getRaised()) {
                        System.out.println("CLIENTP 5");
                        lobby.setInteracted();
                        out.println(lobby.getName() + " interacted 0 0 0 " + lobby.rValue + " 7");
                        lobby.setRaised();
                        lobby.rValue = -1;
                    } else if (lobby.getInteracted() && lobby.getInTurn()) {
                        System.out.println("CLIENTP 6");
                        lobby.setInteracted();
                        out.println(lobby.getName() + " interacted 0 0 0 0 4");
                    }
                    if (lobby.getReturnControl()) {
                        System.out.println("CLIENTP 7");
                        lobby.returnControlToBB();
                        for (int i = 0; i < lobby.cons.size(); i++) {
                            JPanel modify = (JPanel) lobby.playerInfo.getComponent(i);
                            JLabel test = (JLabel) modify.getComponent(0);
                            if (test.getText().equals(lobby.getName())) {
                                JLabel test2 = (JLabel) modify.getComponent(1);
                                if (test2.getText().equals("B")) {
                                    out.println("0 0 0 0 0 " + lobby.cons.get(0) + " 10");
                                } else {
                                    out.println("0 0 0 0 0 -1 10");
                                }
                            }
                        }
                    }
                    if (lobby.getTurnOver()) {
                        System.out.println("CLIENTP 8");
                        lobby.setTurnOver();
                        lobby.returnControlToBB();
                        for (int i = 0; i < lobby.cons.size(); i++) {
                            JPanel modify = (JPanel) lobby.playerInfo.getComponent(i);
                            JLabel test = (JLabel) modify.getComponent(0);
                            if (test.getText().equals(lobby.getName())) {
                                JLabel test2 = (JLabel) modify.getComponent(1);
                                if (test2.getText().equals("b")) {
                                    out.println(lobby.getNewPackAndCards() + " 11");
                                } else {
                                    System.out.println("Vin cartile de la b");
                                }
                            }
                        }
                    }
                }
            }
        }, 250, 100);
    }
}
