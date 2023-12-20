package org.example.clients;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

class recieve extends Thread {
    String receiveMsg = "";
    CardLayout cardLayout;
    JPanel content;
    ReentrantLock lock = new ReentrantLock();
    BufferedReader br;
    Socket ss;
    JFrame frame;

    recieve(Socket ss, CardLayout cardLayout, JPanel content) {
        this.ss = ss;
        InputStream is = null;
        this.cardLayout = cardLayout;
        this.content = content;
        this.frame = frame;
        try {
            is = ss.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        br = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        try {
            boolean login = false;
            homePageClient home = null;
//            List<String> file = new ArrayList<>();
            do {
                this.receiveMsg = this.br.readLine();
                if (receiveMsg == null) {
                    // Client has closed the connection
                    System.out.println("Client closed the connection");
                    break;
                }

                String datas[] = receiveMsg.split(",", -1);
                System.out.println("Received : " + receiveMsg);
                if (datas[0].equals("login")) {
                    home = new homePageClient(ss, datas[1], datas[2]);
                    content.add(home, "homepage");
                    login = true;
                }
                if (login == true) {
                    cardLayout.show(content, "homepage");
                    if (datas[0].equals("new")) {
                        home.appendSidebar(datas[1], datas[2], datas[3]);
                    }
                    if (datas[0].equals(("message"))) {
                        home.appendContent(datas[1], datas[2], null);
                    } else if (datas[0].equals("file")) {
                        try {
//                        if (datas[2].equals("end")) {
//                            home.appendContent(datas[1], "file.txt", file);
//                            file.clear();
//                        }
//                        file.add(datas[2]);

                            lock.lock();
                            System.out.println("locked");
                            List<String> data = new ArrayList<>();
                            do {
                                String line[] = br.readLine().split(",");
                                if(line[2].equals("end"))
                                    break;
                                data.add(line[2]);
                            }while(true);
                            System.out.println("okdanhan");
                            home.appendContent(datas[1], "file.txt", data);
                        } finally {
                            lock.unlock();
                        }
                    } else if (datas[0].equals("group")) {
                        System.out.println(datas[datas.length - 1]);
                        String gr[] = receiveMsg.split("%");
                        System.out.println(receiveMsg);
                        System.out.println(gr[2]);
                        home.appendSidebar(gr[1], "GROUP", gr[2]);
                    } else if (datas[0].equals("sendGroup")) {
                        System.out.println(receiveMsg);
                        String gr[] = receiveMsg.split("%");
                        home.appendContent(gr[1], gr[2], null);
                    }
                }
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
