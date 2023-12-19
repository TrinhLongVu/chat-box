package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

class SendFile extends Thread {
    BufferedWriter bw ;
    String file;
    Socket ss;
    String idSend;
    String idRecieve;
    SendFile(Socket ss, String idSend, String idRecieve, String file) {
        this.idSend = idSend;
        this.idRecieve = idRecieve;
        this.ss =ss;
        OutputStream os = null;
        this.file = file;
        try {
            os = ss.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    public void run() {
        File fileToSend = new File(file);
        try {
                String line = "";
                BufferedReader buff = new BufferedReader(new FileReader(fileToSend));
                while ((line = buff.readLine()) != null) {
                    bw.write("file," + idSend + "," + idRecieve + "," + line);
                    bw.newLine();
                    bw.flush();
                }
                bw.write("file" + "," + idSend + "," + idRecieve + "," + "end");
                bw.newLine();
                bw.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

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

//    public void run() {
//        try {
//            do {
//                DataInputStream din = new DataInputStream(System.in);
//                String k = null;
//                k = din.readLine();
//                bw.write(k);
//                bw.newLine();
//                bw.flush();
//            }while (true);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
class Recieve extends Thread {
    String receiveMsg = "";
    ReentrantLock lock = new ReentrantLock();
    List<String> file = new ArrayList<>();
    CardLayout cardLayout;
    JPanel content;
    BufferedReader br;
    Socket ss;
    Recieve ( Socket ss, CardLayout cardLayout, JPanel content) {
        this.ss = ss;
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
            boolean login = false;
            homePageClient home = null;
            do {
                this.receiveMsg = this.br.readLine();
                String datas[] = receiveMsg.split(",", -1);
                System.out.println("Received : " + receiveMsg);

                if(datas[0].equals("login")) {
//                    String dt = "login,1,hoa$2,Nam,hoa:hi#nam:hello#nam:xinchao$3,Trong,trong:hi hoa,hoa:hi trong";
                    home = new homePageClient(ss, datas[1], datas[2]);
                    content.add(home, "homepage");
//                    Thread.sleep(500);
//                    home.appendSidebar("12", "ngan", "#trinhlongvu:hello#user:hi#trinhlongvu:lau ngay khong gap#trinhlongvu:lau ngay khong gap#user:hi#user:hi#trinhlongvu:nice#trinhlongvu:nice#trinhlongvu:oke#trinhlongvu:oke#" +
//                            "12312user#trinhlongvu:hello#user:hi#trinhlongvu:lau ngay khong gap#trinhlongvu:lau ngay khong gap#user:hi#user:hi#trinhlongvu:nice#trinhlongvu:nice#trinhlongvu:oke#trinhlongvu:oke#");
                    login = true;
                }else if(datas[0].equals("send")) {
                    datas[1] = "12";
                    System.out.println("ok");
                }
                if(login == true) {
                    // get username.
                    cardLayout.show(content, "homepage");
//                    home.appendSidebar("4", "ngan", "hoa:hi#nam:hello#ngan:xinchao");
//                    Thread.sleep(1000);
//                    home.appendContent("4","hello ban nha");
//                    Thread.sleep(2000);
//                    home.appendContent("4","ok ban");
                    if(datas[0].equals("new")) {
                        System.out.println("123"+datas[1] + datas[2] +  datas[3] + "");
//                        datas[1] = "12";
//                        datas[2] = "ngan";
//                        datas[3] = "#trinhlongvu:hello#user:hi#trinhlongvu:lau ngay khong gap#trinhlongvu:lau ngay khong gap#user:hi#user:hi#trinhlongvu:nice#trinhlongvu:nice#trinhlongvu:oke#trinhlongvu:oke#" +
//                                "12312user#trinhlongvu:hello#user:hi#trinhlongvu:lau ngay khong gap#trinhlongvu:lau ngay khong gap#user:hi#user:hi#trinhlongvu:nice#trinhlongvu:nice#trinhlongvu:oke#trinhlongvu:oke#";
//                        home.appendSidebar("12", "ngan", "#trinhlongvu:hello#user:hi#trinhlongvu:lau ngay khong gap#trinhlongvu:lau ngay khong gap#user:hi#user:hi#trinhlongvu:nice#trinhlongvu:nice#trinhlongvu:oke#trinhlongvu:oke#" +
//                                "12312user#trinhlongvu:hello#user:hi#trinhlongvu:lau ngay khong gap#trinhlongvu:lau ngay khong gap#user:hi#user:hi#trinhlongvu:nice#trinhlongvu:nice#trinhlongvu:oke#trinhlongvu:oke#");
                        home.appendSidebar(datas[1], datas[2], datas[3]);
//                        home.appendSidebar(datas[1], datas[2], datas[3]);
                    }
                    if(datas[0].equals(("message"))) {
                        home.appendContent(datas[1], datas[2], null);
                    }
                    else if(datas[0].equals("file")) {
//                        try {
                             if(datas[2].equals("end")){{
                                 home.appendContent(datas[1], "file.txt", file);
                                 file.clear();
                             }}
                             file.add(datas[2]);

//                            lock.lock();
//                            System.out.println("locked");
//                            List<String> data = new ArrayList<>();
//                            do {
//                                String line[] = br.readLine().split(",");
//                                if(line[2].equals("end"))
//                                    break;
//                                data.add(line[2]);
//                            }while(true);
//                            System.out.println("okdanhan");
//                            home.appendContent(datas[1], "file.txt", data);
//                        } finally {
////                            lock.unlock();
//                        }
                    }
                }
            }while (true);
        } catch (IOException e) {
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
