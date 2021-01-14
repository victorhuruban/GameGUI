package com.chessgame.ServerClient;

import com.chessgame.Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CSFrame {
    private JFrame frame;

    public CSFrame() {
        this.frame = new JFrame("Chess");
        JPanel panel = new JPanel() {
            public void paintComponent (Graphics g) {
                Image background = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/background.jpg"));
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        JButton button1 = new JButton("Host");
        JButton button2 = new JButton("Join");
        JTextField field = new JTextField(20);

        panel.add(button1);
        panel.add(button2);
        panel.add(field);
        frame.setContentPane(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        button1.addActionListener(e -> {
            startServer(Integer.parseInt(field.getText()));
        });

        button2.addActionListener(e -> {
            String[] list = field.getText().split(" ");
            startClient(list[0], Integer.parseInt(list[1]));
        });
    }

    public void startServer (int port) {
        Runnable runnable = () -> {
            try {
                Server server = new Server(port);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
        Thread serverThread = new Thread(runnable);
        serverThread.start();
        getFrame().setVisible(false);
        serverThread.interrupt();
    }

    public void startClient (String ip, int port) {
        Runnable runnable = () -> {
            try {
                Client client = new Client(ip, port);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
        Thread clientThread = new Thread(runnable);
        clientThread.start();
        getFrame().setVisible(false);
        clientThread.interrupt();
    }

    public JFrame getFrame() {
        return this.frame;
    }
}
