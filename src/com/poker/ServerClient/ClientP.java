package com.poker.ServerClient;

import java.io.*;
import java.net.Socket;

public class ClientP {

    private static final int SERVER_PORT = 57894;


    public ClientP(String ip) throws IOException {
        Socket socket = new Socket(ip, SERVER_PORT);

        ServerConnection serverConnection = new ServerConnection(socket);

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(serverConnection).start();
        while (true) {
            System.out.println("> ");

            String command = keyboard.readLine();

            if (command.equals("quit")) {
                break;
            }

            out.println(command);
        }


        socket.close();
        System.exit(0);
    }
}
