package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
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
    String reciever = "";
    String idOfClient;
    String nameOfClient;
    JPanel sidebarPanel;
    CardLayout cardLayout;
    JPanel chatBox;
    List<clientFormatData> clients = new ArrayList<>();
    List<JTextArea> text = new ArrayList<>();
    List<JPanel> contentPanels = new ArrayList<>();
    List<JLabel> contentLabels = new ArrayList<>();
    Socket ss;

    homePageClient(Socket ss, String idOfClient, String nameOfClient) {
        this.idOfClient = idOfClient;
        this.nameOfClient = nameOfClient;
        this.ss = ss;
        SwingUtilities.invokeLater(() -> {
            createUi();}
        );
    }
    public void appendContent(String id, String data) {
        int pos = 0;
        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getId().equals(id)){
                pos = clients.get(i).getIndex();
            }
        }
        int finalPos = pos;
        SwingUtilities.invokeLater(() -> {
            text.get(finalPos).append( data + "\n");
        });
    }
    public void appendSidebar(String id, String name, String content) {
        clients.add(new clientFormatData(id, name, content));

        JButton delete = new JButton("Delete");

        int pos = 0;
        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i).getId().equals(id)){
                pos = i;
                clients.get(i).setIndex(i);
            }
            System.out.println("index ::::::"+ clients.get(i).getId().equals(id));
        }
        int finalPos = pos;
        System.out.println("checked: " +  id + pos + clients.get(finalPos).getName());
        JButton button = new JButton(clients.get(finalPos).getName());
        button.setBackground(new Color(255, 255, 255));
        button.setPreferredSize(new Dimension(100, 35));
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cardLayout.show(chatBox, clients.get(finalPos).getName());
                        reciever = clients.get(finalPos).getName();
//                            System.out.println(reciever);
                    }
                }
        );
        contentPanels.add(new JPanel(new BorderLayout()));
        contentLabels.add(new JLabel(clients.get(finalPos).getName()));
        text.add(new JTextArea());

        contentPanels.get(finalPos).setBackground(Color.WHITE);
        contentLabels.get(finalPos).setFont(new Font("Serif", Font.PLAIN, 30));
        text.get(finalPos).setFont(new Font("Serif", Font.PLAIN, 20));
        text.get(finalPos).setEditable(false);
        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(300, 30));
        JTextField input = new JTextField();
        input.setFont(new Font("Serif", Font.PLAIN, 20));
        input.setPreferredSize(new Dimension(200, 30));
        JButton submit = new JButton("submit");


        submit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // handle send data to server
                        text.get(finalPos).append( nameOfClient + ":" + input.getText() + "\n");
                        System.out.println(reciever);
                        new Send(ss).sendData("sendMsg," + idOfClient + "," + id + "," + nameOfClient + ":" + input.getText());
                    }
                });
        delete.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        text.get(finalPos).setText("");
                        new Send(ss).sendData("delete," + idOfClient + "," + id);
                    }
                });

        JPanel NamePanel = new JPanel();

        SwingUtilities.invokeLater(() -> {
            sidebarPanel.add(button);
            NamePanel.add(contentLabels.get(finalPos));
            NamePanel.add(delete);
            contentPanels.get(finalPos).add(NamePanel, BorderLayout.NORTH);
            chatBox.add(contentPanels.get(finalPos), clients.get(finalPos).getName());
            contentPanels.get(finalPos).add(text.get(finalPos), BorderLayout.CENTER);
            text.get(finalPos).append(content.replace("#", "\n"));
            footer.add(input);
            footer.add(submit);
            contentPanels.get(finalPos).add(footer, BorderLayout.SOUTH);
        });
    }

    void createUi() {
        JPanel header = new JPanel();


        JLabel lableHeader = new JLabel(nameOfClient);
        sidebarPanel = new JPanel();
        cardLayout = new CardLayout();
        chatBox = new JPanel(cardLayout);
        setLayout(new BorderLayout());
        setBackground(Color.white);
        header.setBackground(Color.RED);
        header.setPreferredSize(new Dimension(400, 50));

        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(100, 500));
        sidebarPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        sidebarPanel.setBackground(new Color(0xDFCBE1));
        header.add(lableHeader);

        add(sidebarPanel, BorderLayout.WEST);
        add(chatBox, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
    }

        public static void main(String[] a) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.RED);

        CardLayout cardLayout = new CardLayout();
        JPanel content = new JPanel();
        content.setLayout(cardLayout);
        Send send = null;
        String datas = "";
        String dt = "login,1,hoa$2,Nam,hoa:hi#nam:hello#nam:xinchao$3,Trong,trong:hi hoa,hoa:hi trong";

//            content.add(new Login(content, cardLayout, send), "login");
//            content.add(new Signup(content, cardLayout), "signup");
            Socket ss = null;
        homePageClient home = new homePageClient(ss, "4", "hello");
        content.add(home, "homepage");
        cardLayout.show(content, "homepage");
            try {
                Thread.sleep(500);
                home.appendSidebar("12", "ngan", "#trinhlongvu:hello#user:hi#trinhlongvu:lau ngay khong gap#trinhlongvu:lau ngay khong gap#user:hi#user:hi#trinhlongvu:nice#trinhlongvu:nice#trinhlongvu:oke#trinhlongvu:oke#" +
                        "12312user#trinhlongvu:hello#user:hi#trinhlongvu:lau ngay khong gap#trinhlongvu:lau ngay khong gap#user:hi#user:hi#trinhlongvu:nice#trinhlongvu:nice#trinhlongvu:oke#trinhlongvu:oke#");
//                Thread.sleep(300);
//                home.appendSidebar("2", "Nam", "hoa:hi#nam:hello#nam:xinchao");
////                Thread.sleep(300);
//                home.appendSidebar("3", "Trong", "trong:hi hoa#hoa:hi trong");
//                Thread.sleep(300);
//                home.appendContent("4", "hii");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            frame.add(content);
        frame.setBounds(10, 10, 400, 500);
        frame.setVisible(true);
        frame.setResizable(false);

    }
}
