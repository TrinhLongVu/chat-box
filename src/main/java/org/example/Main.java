package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class receiveThread extends Thread {
    String receiveMsg = "";
    BufferedReader br;
    receiveThread ( Socket ss) {
        InputStream is = null;
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
            }while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class sendClient extends Thread {
    BufferedWriter bw ;
    sendClient(Socket ss) {
        OutputStream os = null;
        try {
            os = ss.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }
    public void start() {
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
    clientThread(Socket a) {
        this.ss = a;
    }
    public void run() {

        System.out.println("Talking to client");
        System.out.println(ss.getPort());
        do
        {
            new receiveThread(ss).start();
            new sendClient(ss).start();
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
    public static void main(String[] args) {
        try
        {
            ServerSocket s = new ServerSocket(3001);
            do
            {
                Socket ss = s.accept(); //synchronous
                new clientThread(ss).start();
            }
            while (true);
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }
    }
}