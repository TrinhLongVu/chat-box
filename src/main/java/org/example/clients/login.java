package org.example.clients;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class login extends JPanel{
    CardLayout cardLayout;
    org.example.clients.send send;
    JPanel content;
    login(JPanel c, CardLayout layout, org.example.clients.send s){
        this.content = c;
        this.cardLayout = layout;
        this.send = s;
        UI();
    }
    void UI() {
        JPanel usernamePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel passwordPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel loginButton = new JPanel(new GridLayout(1, 1, 5, 5));
        JPanel signUpButton = new JPanel(new GridLayout(1, 1, 5, 5));
        JLabel login = new JLabel("LOGIN");
        JLabel labelUser = new JLabel("User");
        JTextField fieldUser = new JTextField();
        JLabel labelpassword = new JLabel("Password");
        JTextField password = new JTextField();
        JButton button = new JButton("login");
        JLabel signUp = new JLabel("Signup");

        setBackground(Color.white);
        usernamePanel.setBackground(Color.white);
        passwordPanel.setBackground(Color.white);
        loginButton.setBackground(Color.white);
        signUpButton.setBackground(Color.white);
        login.setFont(new Font("Serif", Font.PLAIN, 30));
        labelUser.setFont(new Font("Serif", Font.PLAIN, 20));
        fieldUser.setPreferredSize(new Dimension(330, 40));
        fieldUser.setFont(new Font("Serif", Font.PLAIN, 20));
        fieldUser.setBorder(new roundedBorder(10));
        labelpassword.setFont(new Font("Serif", Font.PLAIN, 20));
        password.setPreferredSize(new Dimension(330, 40));
        password.setFont(new Font("Serif", Font.PLAIN, 20));
        password.setBorder(new roundedBorder(10));
        button.setFont(new Font("Serif", Font.PLAIN, 20));
        button.setPreferredSize(new Dimension(330, 40));
        button.setBackground(new Color(200, 209, 226));
        signUp.setFont(new Font("Serif", Font.PLAIN, 20));
        loginButton.setBorder(new EmptyBorder(10, 0, 10, 0));

        signUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(content, "signup");
            }
        });
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               send.sendData("TagLogin," + fieldUser.getText() + "," + password.getText());
            }
        } );

        usernamePanel.add(labelUser);
        usernamePanel.add(fieldUser);
        passwordPanel.add(labelpassword);
        passwordPanel.add(password);
        loginButton.add(button);
        signUpButton.add(signUp);

        add(login);
        add(usernamePanel);
        add(passwordPanel);
        add(loginButton);
        add(signUpButton);
    }
}