package org.example.clients;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class homePageClient extends JPanel {
    String idOfClient;
    String nameOfClient;
    JPanel sidebarPanel;
    CardLayout cardLayout;
    JPanel chatBox;
    List<clientFormatData> clients = new ArrayList<>();
    List<JList> text = new ArrayList<>();
    List<JPanel> contentPanels = new ArrayList<>();
    List<JLabel> contentLabels = new ArrayList<>();
    List<DefaultListModel> listModel = new ArrayList<>();
    JButton group = new JButton("group");
    DefaultListModel<String> groupModel = new DefaultListModel<>();
    JList<String> groupList = new JList<>(groupModel);
    JPanel groupPanel = new JPanel();
    JButton groupSelect = new JButton("Submit");
    Socket ss;

    homePageClient(Socket ss, String idOfClient, String nameOfClient) {
        this.idOfClient = idOfClient;
        this.nameOfClient = nameOfClient;
        this.ss = ss;
        SwingUtilities.invokeLater(() -> {
            createUi();
        });
    }

    private void saveListToFile(List<String> dataList) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save to File");
        int userSelection = fileChooser.showSaveDialog(homePageClient.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                for (String value : dataList) {
                    writer.write(value);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "File saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void appendContent(String id, String data, List<String> file) {
        int pos = 0;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId().equals(id)) {
                pos = clients.get(i).getIndex();
            }
        }
        int finalPos = pos;
        SwingUtilities.invokeLater(() -> {
            listModel.get(finalPos).addElement(data);
            text.get(finalPos).addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        String selectedValue = (String) text.get(finalPos).getSelectedValue();
                        if (selectedValue.equals("file.txt")) {
                            saveListToFile(file);
                        }
                    }
                }
            });
        });
    }

    public void appendSidebar(String id, String name, String content) {
        clients.add(new clientFormatData(id, name, content));

        JButton delete = new JButton("Delete");
        delete.setBackground(Color.WHITE);

        int pos = 0;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId().equals(id)) {
                pos = i;
                clients.get(i).setIndex(i);
            }
        }
        int finalPos = pos;
        JButton button = new JButton(clients.get(finalPos).getName());
        button.setBackground(new Color(255, 255, 255));
        button.setPreferredSize(new Dimension(98, 35));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(chatBox, clients.get(finalPos).getName());
            }
        });
        contentPanels.add(new JPanel(new BorderLayout()));
        contentLabels.add(new JLabel(clients.get(finalPos).getName()));
        DefaultListModel model = new DefaultListModel<>();
        text.add(new JList(model));
        listModel.add(model);

        contentPanels.get(finalPos).setBackground(Color.WHITE);
        contentLabels.get(finalPos).setFont(new Font("Serif", Font.PLAIN, 30));
        text.get(finalPos).setFont(new Font("Serif", Font.PLAIN, 20));
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footer.setBackground(Color.WHITE);
        footer.setPreferredSize(new Dimension(300, 30));

        JTextField input = new JTextField();
        input.setFont(new Font("Serif", Font.PLAIN, 20));
        input.setPreferredSize(new Dimension(150, 30));

        JButton submit = new JButton("Send");
        JButton chooseFile = new JButton("file");
        submit.setPreferredSize(new Dimension(70, 30));
        chooseFile.setPreferredSize(new Dimension(60, 30));

        chooseFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                new sendFile(ss, idOfClient, id, selectedFile.getAbsolutePath()).start();
                listModel.get(finalPos).addElement("file.txt");
            } else {
                System.out.println("No file chosen.");
            }
        });


        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (id.contains("group")) {
                    new send(ss).sendData("sendGroup,%" + id + "%" + nameOfClient + ":" + input.getText());
                } else {
                    listModel.get(finalPos).addElement(nameOfClient + ":" + input.getText());
                    new send(ss).sendData("sendMsg," + idOfClient + "," + id + "," + nameOfClient + ":" + input.getText());
                }

            }
        });
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.get(finalPos).removeAllElements();
                new send(ss).sendData("delete," + idOfClient + "," + id);
            }
        });

        JPanel NamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        NamePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        contentPanels.get(finalPos).setBackground(Color.white);
        text.get(finalPos).setBorder(BorderFactory.createLineBorder(Color.black));
        NamePanel.setBackground(Color.WHITE);
        NamePanel.setLayout(new BorderLayout());
        SwingUtilities.invokeLater(() -> {
            sidebarPanel.add(button);
            NamePanel.add(contentLabels.get(finalPos), BorderLayout.WEST);
            NamePanel.add(delete, BorderLayout.EAST);
            contentPanels.get(finalPos).add(NamePanel, BorderLayout.NORTH);
            chatBox.add(contentPanels.get(finalPos), clients.get(finalPos).getName());
            contentPanels.get(finalPos).add(text.get(finalPos), BorderLayout.CENTER);
            String data[] = content.split("#");
            for (String dt : data)
                listModel.get(finalPos).addElement(dt);

            groupModel.addElement(clients.get(finalPos).getName());
            footer.setBorder(BorderFactory.createLineBorder(Color.black));
            footer.add(input);
            footer.add(submit);
            footer.add(chooseFile);
            contentPanels.get(finalPos).add(footer, BorderLayout.SOUTH);
            chatBox.add(groupPanel, "group");
        });
    }

    void createUi() {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel lableHeader = new JLabel(nameOfClient);
        lableHeader.setFont(new Font("Serif", Font.PLAIN, 30));

        sidebarPanel = new JPanel();
        cardLayout = new CardLayout();
        chatBox = new JPanel(cardLayout);

        setLayout(new BorderLayout());
        setBackground(Color.white);

        header.setBackground(Color.WHITE);
        group.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(400, 50));
        header.add(lableHeader, BorderLayout.WEST);
        header.add(group, BorderLayout.EAST);

        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(100, 500));
        sidebarPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        sidebarPanel.setBackground(new Color(255, 255, 255));
        groupList.setFont(new Font("Serif", Font.PLAIN, 30));
        groupPanel.add(groupList);
        groupPanel.add(groupSelect);

        group.addActionListener(e -> {
            cardLayout.show(chatBox, "group");
        });

        groupSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] selectedIndices = groupList.getSelectedIndices();

                StringBuilder selectedItems = new StringBuilder("");
                for (int index : selectedIndices) {
                    selectedItems.append(groupModel.getElementAt(index)).append(",");
                }

                String user[] = selectedItems.toString().split(",");
                StringBuilder id = new StringBuilder("");

                for (int i = 0; i < clients.size(); i++) {
                    for (int j = 0; j < user.length; j++) {
                        if (clients.get(i).getName().equals(user[j])) {
                            id.append("," + clients.get(i).getId());
                        }
                    }
                }

                new send(ss).sendData("group," + idOfClient + id);
            }
        });

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
//        Send send = null;
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
            home.appendSidebar("12", "ngan", "#trinhlongvu:hello#user:hi");
            home.appendSidebar("13", "nam", "#nam:hello#ngan:hi");
//                Thread.sleep(300);
//                home.appendSidebar("2", "Nam", "hoa:hi#nam:hello#nam:xinchao");
////                Thread.sleep(300);
//                home.appendSidebar("3", "Trong", "trong:hi hoa#hoa:hi trong");
//                Thread.sleep(300);
            home.appendContent("4", "file.txt", null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        frame.add(content);
        frame.setBounds(10, 10, 400, 500);
        frame.setVisible(true);
        frame.setResizable(false);

    }
}
