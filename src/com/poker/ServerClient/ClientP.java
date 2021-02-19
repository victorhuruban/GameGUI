package com.poker.ServerClient;

import com.poker.Lobby.Lobby;

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

        out.println(lobby.getName() + " " + 0);
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
                    out.println(lobby.getName() + " " + 0);
                    lobby.setSendBack();
                    timer.cancel();
                    runLoop();
                } else if (lobby.getTryStart()) {
                    out.println("try2start");
                    lobby.setTryStart();
                } else if (lobby.getSendReadyStat()) {
                    if (lobby.getReadyPressed()) {
                        out.println(1);
                    } else out.println(0);
                    lobby.setSendReadyStat();
                }
            }
        }, 250, 100);
    }
}
