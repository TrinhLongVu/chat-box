package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class receiveOfServer extends Thread {
    String receiveMsg = "";
    database db;
    BufferedReader br;
    receiveOfServer ( Socket ss, database d) {
        InputStream is = null;
        this.db = d;
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
                System.out.println("Received : " + receiveMsg);
                String data[] = receiveMsg.split(",");
                if(data[0].equals("TagSignup")){
                    System.out.println("11");
                    db.postUser(data[1], data[2]);
                }
            }while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class SendOfServer extends Thread {
    BufferedWriter bw ;
    SendOfServer(Socket ss) {
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

class clientThread extends Thread {
    private Socket ss;
    database db;
    clientThread(Socket a, database d) {
        this.ss = a;
        this.db = d;
    }
    public void run() {

        System.out.println("Talking to client");
        System.out.println(ss.getPort());
        do
        {
            new receiveOfServer(ss, db).start();
            new SendOfServer(ss).start();
        }
        while (true);
    }
}

class Client {
    String nameClientSend;
    String nameClientRecieve;
    clientThread clients;
    Client(clientThread s, String nameSend, String nameRecieve) {
        this.nameClientSend = nameSend;
        this.nameClientRecieve = nameRecieve;
        this.clients = s;
    }
}

public class Main {
    database db;
    Main() {
        db = new database();
        createServer();
    }
    void createServer() {
        try
        {
            ServerSocket s = new ServerSocket(3001);
            do
            {
                Socket ss = s.accept(); //synchronous
                new clientThread(ss, db).start();
            }
            while (true);
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }
    }
    public static void main(String[] args) {
        new Main();
    }
}