package org.example.clients;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClients {
    private Socket s;
    private org.example.clients.send send;
    MainClients() {
        connect();
        createUI();
    }
    void connect() {
        try {
            s = new Socket("localhost", 3001);
            System.out.println(s.getLocalPort());
            System.out.println("Talking to Server");
            System.out.println(s);
            send = new send(s);
            send.start();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void createUI() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.RED);

        CardLayout cardLayout = new CardLayout();
        JPanel content = new JPanel();
        content.setLayout(cardLayout);

        content.add(new login(content, cardLayout, send), "login");
        content.add(new signup(content, cardLayout, send), "signup");

        cardLayout.show(content, "login");
        SwingUtilities.invokeLater(() -> {
            new recieve(s, cardLayout, content).start();
        });
        frame.add(content);
        frame.setBounds(10,10,400,500);

        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] a){
        SwingUtilities.invokeLater(() -> new MainClients());
        SwingUtilities.invokeLater(() -> new MainClients());
    }
}
