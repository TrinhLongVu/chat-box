package org.example.server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    ArrayList<structureClient> clients = new ArrayList<>();
    database db;
    Main() {
        db = new database();
        SwingUtilities.invokeLater(() -> {
            UI();
        });
    }

    void UI() {
        JFrame frame = new JFrame("Simple Swing UI");
        JPanel panel = new JPanel();
        JButton button = new JButton("SERVER!");

        button.addActionListener(
            e -> {
                new Thread(() -> {
                    createServer(panel);
                }).start();
            }
        );

        panel.add(button);
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
    void createServer(JPanel panel) {
        try
        {
            SwingUtilities.invokeLater(() -> {
                JLabel openServer = new JLabel("Server opened.....");
                panel.add(openServer);
                panel.revalidate();
                panel.repaint();
            });
            ServerSocket s = new ServerSocket(3001);
            do
            {
                Socket ss = s.accept();
                new clientThread(ss, db, clients).start();
            }
            while (true);
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }
    }
    public static void main(String[] args) {
        new Main();
    }
}