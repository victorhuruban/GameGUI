package com.test;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientTest {

    private static final int SERVER_PORT = 5789;
    private static final String SERVER_IP = "192.168.0.155";

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);

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
