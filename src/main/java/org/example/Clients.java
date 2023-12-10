package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Clients {
    private Socket s;
    private Send send;
    Clients() {
        connect();
        createUI();
    }
    void connect() {
        try {
            s = new Socket("localhost", 3001);
            System.out.println(s.getPort());
            System.out.println("Talking to Server");
            new receiveThread(s).start();
            send = new Send(s);
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

        content.add(new Login(content, cardLayout, send), "login");
        content.add(new Signup(content, cardLayout, send), "signup");
        content.add(new homePageClient(), "homepage");

        cardLayout.show(content, "login");

        frame.add(content);
        frame.setBounds(10,10,400,500);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] a){
        new Clients();
    }
}