package org.example.clients;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class signup extends JPanel {
    CardLayout cardLayout;
    org.example.clients.send send;
    JPanel content;
    signup(JPanel c, CardLayout layout, org.example.clients.send s){
        this.cardLayout = layout;
        this.content = c;
        this.send = s;
        UI();
    }
    void UI() {
        setBackground(Color.white);
        JPanel usernamePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel passwordPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel cpasswordPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel signupButton = new JPanel(new GridLayout(1, 1, 5, 5));
        JPanel loginPanel = new JPanel(new GridLayout(1, 1, 5, 5));
        JLabel login = new JLabel("Sign Up");
        JLabel labelUser = new JLabel("User");
        JLabel labelcpassword = new JLabel("Confirm Password");
        JLabel labelpassword = new JLabel("Password");

        JTextField fieldUser = new JTextField();
        JTextField password = new JTextField();
        JTextField cpassword = new JTextField();
        JButton button = new JButton("Sign Up");
        JLabel loginButton = new JLabel("Login");

        usernamePanel.setBackground(Color.white);
        passwordPanel.setBackground(Color.white);
        cpasswordPanel.setBackground(Color.white);
        signupButton.setBackground(Color.white);
        loginPanel.setBackground(Color.white);

        login.setFont(new Font("Serif", Font.PLAIN, 30));
        labelUser.setFont(new Font("Serif", Font.PLAIN, 20));
        fieldUser.setPreferredSize(new Dimension(330, 40));
        fieldUser.setFont(new Font("Serif", Font.PLAIN, 20));
        fieldUser.setBorder(new roundedBorder(10));
        labelpassword.setFont(new Font("Serif", Font.PLAIN, 20));
        password.setPreferredSize(new Dimension(330, 40));
        password.setFont(new Font("Serif", Font.PLAIN, 20));
        password.setBorder(new roundedBorder(10));
        labelcpassword.setFont(new Font("Serif", Font.PLAIN, 20));
        cpassword.setPreferredSize(new Dimension(330, 40));
        cpassword.setFont(new Font("Serif", Font.PLAIN, 20));
        cpassword.setBorder(new roundedBorder(10));
        button.setFont(new Font("Serif", Font.PLAIN, 20));
        button.setPreferredSize(new Dimension(330, 40));
        button.setBackground(new Color(200, 209, 226));
        loginButton.setFont(new Font("Serif", Font.PLAIN, 20));
        labelcpassword.setBorder(new EmptyBorder(10, 0, 10, 0));

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(content, "login");
            }
        });

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked");
                if(password.getText().equals(cpassword.getText())) {
                    cardLayout.show(content, "login");
                    send.sendData("TagSignup," + fieldUser.getText() + "," + password.getText());
                }
            }
        } );


        usernamePanel.add(labelUser);
        usernamePanel.add(fieldUser);
        passwordPanel.add(labelpassword);
        passwordPanel.add(password);
        cpasswordPanel.add(labelcpassword);
        cpasswordPanel.add(cpassword);
        signupButton.add(button);
        loginPanel.add(loginButton);
        add(login);
        add(usernamePanel);
        add(passwordPanel);
        add(cpasswordPanel);
        add(signupButton);
        add(loginPanel);
    }
}
