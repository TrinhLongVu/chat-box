package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
class Send extends Thread {
    BufferedWriter bw ;
    Send(Socket ss) {
        OutputStream os = null;
        try {
            os = ss.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }
    public void sendData(String data){
        try {
            bw.write(data);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void run() {
        try {
            do {
                DataInputStream din = new DataInputStream(System.in);
                String k = null;
                k = din.readLine();
                bw.write(k);
                bw.newLine();
                bw.flush();
            }while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Recieve extends Thread {
    String receiveMsg = "";
    CardLayout cardLayout;
    JPanel content;
    BufferedReader br;
    Recieve ( Socket ss, CardLayout cardLayout, JPanel content) {
        InputStream is = null;
        this.cardLayout = cardLayout;
        this.content = content;
        try {
            is = ss.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        br = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        try {
            do {
                this.receiveMsg = this.br.readLine();
                homePageClient a = null;
                String datas[] = receiveMsg.split(",");
                System.out.println("Received : " + receiveMsg);

                if(datas[0].equals("login")) {
                    String dt = "login,1,hoa$2,Nam,hoa:hi#nam:hello#nam:xinchao$3,Trong,trong:hi hoa,hoa:hi trong";
                    a = new homePageClient(dt);
                    // get username.
                    content.add(a, "homepage");
                    cardLayout.show(content, "homepage");
                    Thread.sleep(1000);
                    a.appendContent("3","hello ban nha");
                    Thread.sleep(2000);
                    a.appendContent("2","ok ban");
                }else if(datas[0].equals("send")) {
                    datas[1] = "12";
                    System.out.println("ok");
                }
            }while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

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
//            new receiveThread(s).start();
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
//        JPanel homepage = new homePageClient(datas);

        content.add(new Login(content, cardLayout, send), "login");
        content.add(new Signup(content, cardLayout, send), "signup");

        cardLayout.show(content, "login");
        new Recieve(s, cardLayout, content).start();
        frame.add(content);
        frame.setBounds(10,10,400,500);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] a){
        new Clients();
    }
}
