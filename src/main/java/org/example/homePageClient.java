package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class clientFormatData {
    private String id;
    private String name;
    private String content;
    private int index;

    clientFormatData(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
    void setIndex(int index) {
        this.index = index;
    }
    int getIndex(){
        return this.index;
    }

    String getId() {
        return this.id;
    }
    String getName() {return this.name;}

    String getContent() {
        return this.content;
    }
}

public class homePageClient extends JPanel {
    String reciever;
    JPanel sidebarPanel;
    CardLayout cardLayout;
    JPanel chatBox;
    List<clientFormatData> clients = new ArrayList<>();

    homePageClient(String data) {
        SwingUtilities.invokeLater(() -> {
            SplitData(data);
            createUi();}
        );
    }
    JTextArea text[];
    public void appendContent(String id, String data) {
        int pos = 0;
        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getId().equals(id)){
                pos = clients.get(i).getIndex();
            }
        }
        int finalPos = pos;
        SwingUtilities.invokeLater(() -> {
            text[finalPos].append(data + "\n");
        });
    }

    public void appendSidebar(int pos, String s) {
        SwingUtilities.invokeLater(() -> {

        });
    }


    static List<clientFormatData> convertToChatList(String data) {
        List<clientFormatData> chatList = new ArrayList<>();
        String[] entries = data.split("#");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                String id = parts[0];
                String content = parts[1];
                chatList.add(new clientFormatData(id, id, content));
            }
        }
        return chatList;
    }

    private void handelBoxChat(String data, String id) {
        List<clientFormatData> chatList = convertToChatList(data);
        for (clientFormatData c : chatList) {
            appendContent(id, c.getId() + ": " + c.getContent());
        }
    }

    void createUi() {
        String nameOtherClients[] = {"nam", "hoai", "Duy"};
        String data = "me:123,hoai:456,me:111,me:000";
        List<clientFormatData> chatList = convertToChatList(data);
        reciever = nameOtherClients[0];
        /////////////////////////////
        JPanel header = new JPanel();
        sidebarPanel = new JPanel();
        cardLayout = new CardLayout();
        chatBox = new JPanel(cardLayout);
        text = new JTextArea[nameOtherClients.length];
        JPanel contentPanels[] = new JPanel[nameOtherClients.length];
        JLabel contentLabels[] = new JLabel[nameOtherClients.length];

        setLayout(new BorderLayout());
        setBackground(Color.white);

        header.setBackground(Color.RED);
        header.setPreferredSize(new Dimension(400, 50));

        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(100, 500));
        sidebarPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        sidebarPanel.setBackground(new Color(0xDFCBE1));

        System.out.println(clients.size());

        for (int i = 0; i < clients.size(); i++) {
            final int index = i;
            clients.get(i).setIndex(index);
            System.out.println(clients.get(i).getName());
            contentLabels[i] = new JLabel(clients.get(i).getName());
            contentPanels[i] = new JPanel(new BorderLayout());
            text[i] = new JTextArea();
            JTextField input = new JTextField();
            JButton submit = new JButton("submit");
            JPanel footer = new JPanel();
            JButton button = new JButton(clients.get(i).getName());

            contentLabels[i].setFont(new Font("Serif", Font.PLAIN, 30));

            text[i].setFont(new Font("Serif", Font.PLAIN, 20));
            text[i].setEditable(false);

            input.setFont(new Font("Serif", Font.PLAIN, 20));
            input.setPreferredSize(new Dimension(200, 30));

            contentPanels[i].setBackground(Color.WHITE);
            contentPanels[i].add(contentLabels[i], BorderLayout.NORTH);

            footer.setPreferredSize(new Dimension(300, 30));
            button.setBackground(new Color(255, 255, 255));
            button.setPreferredSize(new Dimension(100, 35));

            handelBoxChat(clients.get(i).getContent(), clients.get(i).getId());

            submit.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // handle send data to server
                            text[index].append("me :" + input.getText() + "\n");
                            System.out.println(reciever);
//                            new Send(ss).sendData("send,1,hello");
                        }
                    });
            button.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            cardLayout.show(chatBox, clients.get(index).getName());
                            reciever = clients.get(index).getName();
//                            System.out.println(reciever);
                        }
                    }
            );


            chatBox.add(contentPanels[i], clients.get(index).getName());
            contentPanels[i].add(text[i], BorderLayout.CENTER);
            footer.add(input);
            footer.add(submit);
            contentPanels[i].add(footer, BorderLayout.SOUTH);
            sidebarPanel.add(button);
        }
        add(sidebarPanel, BorderLayout.WEST);
        add(chatBox, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
    }

    void SplitData(String data){
        //String dt = "login,1,hoa$2,Nam,hoa:hi#nam:hello#nam:xinchao$3,Trong,trong:hi hoa#hoa:hi trong"
        String entries[] = data.split("\\$");
        for(int i = 1; i < entries.length; i++){
            String entry[] = entries[i].split(",");
            String id = entry[0];
            String name = entry[1];
            String content = entry[2];
            clients.add(new clientFormatData(id, name, content));
        }
    }

//        public static void main(String[] a) {
//        JFrame frame = new JFrame("Login");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setBackground(Color.RED);
//
//        CardLayout cardLayout = new CardLayout();
//        JPanel content = new JPanel();
//        content.setLayout(cardLayout);
//        Send send = null;
//        String datas = "";
//        String dt = "login,1,hoa$2,Nam,hoa:hi#nam:hello#nam:xinchao$3,Trong,trong:hi hoa,hoa:hi trong";
//
////            content.add(new Login(content, cardLayout, send), "login");
////            content.add(new Signup(content, cardLayout), "signup");
//        content.add(new homePageClient(dt), "homepage");
//        cardLayout.show(content, "homepage");
//
//        frame.add(content);
//        frame.setBounds(10, 10, 400, 500);
//        frame.setVisible(true);
//        frame.setResizable(false);

//    }
}
