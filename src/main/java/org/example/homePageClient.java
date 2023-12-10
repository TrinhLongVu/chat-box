package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class chat {
    private String _id;
    private String _content;

    chat(String id, String content) {
        this._id = id;
        this._content = content;
    }

    String getId() {
        return this._id;
    }

    String getContent() {
        return this._content;
    }
}

public class homePageClient extends JPanel {
    String reciever;
    homePageClient() {
        createUi();
    }

    static List<chat> convertToChatList(String data) {
        List<chat> chatList = new ArrayList<>();

        String[] entries = data.split(", ");
        for (String entry : entries) {
            String[] parts = entry.split(": ");
            if (parts.length == 2) {
                String id = parts[0];
                String content = parts[1];
                chatList.add(new chat(id, content));
            }
        }
        return chatList;
    }

    void createUi() {
        String a[] = {"nam", "hoai", "Duy"};
        String data = "me: 123, hoai: 456, me: 111, me: 000";
        List<chat> chatList = convertToChatList(data);
        reciever = a[0];
        for (chat c : chatList) {
            System.out.println("ID: " + c.getId() + ", Content: " + c.getContent());
        }

        JPanel sidebarPanel = new JPanel();
        JPanel header = new JPanel();
        CardLayout cardLayout = new CardLayout();
        JPanel chatBox = new JPanel(cardLayout);
        JPanel contentPanels[] = new JPanel[a.length];
        JLabel contentLabels[] = new JLabel[a.length];
        JTextArea text[] = new JTextArea[a.length];

        setLayout(new BorderLayout());
        setBackground(Color.white);
        sidebarPanel.setBackground(Color.WHITE);
        header.setBackground(Color.RED);

        sidebarPanel.setPreferredSize(new Dimension(100, 500));
        header.setPreferredSize(new Dimension(400, 50));
        sidebarPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        sidebarPanel.setBackground(new Color(0xDFCBE1));

        // declare component of content
        for(int i = 0; i < a.length; i++) {
            contentLabels[i] = new JLabel(a[i]);
            contentPanels[i] = new JPanel(new BorderLayout());
            text[i] = new JTextArea();
        }

        // setup background color
        for(int i = 0; i < a.length; i++) {
            contentLabels[i].setFont(new Font("Serif", Font.PLAIN, 30));
            contentPanels[i].setBackground(Color.WHITE);
            contentPanels[i].add(contentLabels[i], BorderLayout.NORTH);
            chatBox.add(contentPanels[i], a[i]);
            text[i].setEditable(false);
            text[i].setFont(new Font("Serif", Font.PLAIN, 20));
        }

        for (int i = 0; i < a.length; i++) {
            final int index = i;
            JTextField input = new JTextField();
            JButton submit = new JButton("submit");
            JPanel footer = new JPanel();

            for (chat c : chatList) {
                text[i].append(c.getId() + ": " + c.getContent() + "\n");
            }

            contentPanels[i].add(text[i], BorderLayout.CENTER);

            input.setFont(new Font("Serif", Font.PLAIN, 20));
            input.setPreferredSize(new Dimension(200, 30));
            footer.setPreferredSize(new Dimension(300, 30));
            footer.add(input);
            footer.add(submit);
            submit.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // handle send data to server
                            text[index].append("me :" + input.getText() + "\n");
                            System.out.println(reciever);

                        }
                    });
            contentPanels[i].add(footer, BorderLayout.SOUTH);
        }

        for (int i = 0; i < a.length; i++) {
            JButton button = new JButton(a[i]);
            final int index = i;
            button.setPreferredSize(new Dimension(100, 35));
            button.setBackground(new Color(255, 255, 255));
            button.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            cardLayout.show(chatBox, a[index]);
                            reciever = a[index];
                        }
                    }
            );
            sidebarPanel.add(button);
        }

        add(sidebarPanel, BorderLayout.WEST);
        add(chatBox, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
    }

//    public static void main(String[] a) {
//        JFrame frame = new JFrame("Login");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setBackground(Color.RED);
//
//        CardLayout cardLayout = new CardLayout();
//        JPanel content = new JPanel();
//        content.setLayout(cardLayout);
//        Send send = null;
//
////            content.add(new Login(content, cardLayout, send), "login");
////            content.add(new Signup(content, cardLayout), "signup");
//        content.add(new homePageClient(), "homepage");
//        cardLayout.show(content, "homepage");
//
//        frame.add(content);
//        frame.setBounds(10, 10, 400, 500);
//        frame.setVisible(true);
//        frame.setResizable(false);
//
//
//    }
}
