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
                    out.println("1");
                    timer.cancel();
                    runLoop();
                } else if (lobby.getExit()) {
                    timer.cancel();
                }
            }
        }, 250, 100);
    }
}
