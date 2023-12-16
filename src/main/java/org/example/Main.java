package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ClientOfServer {
    private String id;
    private String name;
    private Socket ss;
    ClientOfServer(String id, Socket ss, String name) {
        this.id = id;
        this.name = name;
        this.ss = ss;
    }
    String getID() {
        return this.id;
    }

    String data() {
        return this.id + "," + this.name;
    }

    String getName() {
        return this.name;
    }
    Socket nameSocket() {
        return this.ss;
    }
}
class receiveOfServer extends Thread {
    String receiveMsg = "";
    database db;
    BufferedReader br;
    Socket ss;
    ArrayList<ClientOfServer> clients;
    receiveOfServer ( Socket ss, database d, ArrayList<ClientOfServer> clients ) {
        InputStream is = null;
        this.db = d;
        this.ss = ss;
        this.clients = clients;
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
                    UserData user = db.postUser(data[1], data[2]);
                    db.insertContenChat(user.getId());
                }else if(data[0].equals("TagLogin")){
                    UserData user = db.checkLogin(data[1], data[2]);
                    SendOfServer send = new SendOfServer(ss);
                    if(user != null) {
                        String loginedClient = "login";
                        send.sendData(loginedClient + "," + user.getId() + "," + user.getUsername());

                        for(ClientOfServer client : clients) {
                            SendOfServer sendForPreClient = new SendOfServer(client.nameSocket());
//                            sendForPreClient.sendData("new," + user.getId() + "," + user.getUsername() + ",hoa:hi#nam:helloaaaa#ngan:xinchaoaaa");
                            String content =  db.getContentText(client.getID(), user.getId());
                            sendForPreClient.sendData("new,"+ user.getId() + "," + user.getUsername() + "," + content);
                            send.sendData("new," + client.getID() + "," + client.getName() + "," + content);
                        }
                        clients.add(new ClientOfServer(user.getId(), ss, user.getUsername()));
//                        Thread.sleep(1000);
//                        send.sendData("send,2,NguyenDuy,1,NguyenHuyHoa,hello");
//                        Thread.sleep(500);/
//                        send.sendData("new,4,xuan,hoa:hi#nam:helloaaaa#ngan:xinchaoaaa");
                    }
                }else if(data[0].equals("sendMsg")) {
                    for(ClientOfServer client : clients) {
                       if(client.getID().equals(data[2])){
                           db.update(data[1], data[2], data[3] + "#");
                           new SendOfServer(client.nameSocket()).sendData("message," + data[1] + "," + data[3]);
                       }
                    }
                    System.out.println(data[1]);
                }else if(data[0].equals("delete")) {
                    db.deleteContent(data[1], data[2]);
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
    ArrayList<ClientOfServer> clients;
    clientThread(Socket a, database d, ArrayList<ClientOfServer> clients) {
        this.ss = a;
        this.db = d;
        this.clients = clients;
    }
    public void run() {

        System.out.println("Talking to client");
        System.out.println(ss.getPort());
        do
        {
            new receiveOfServer(ss, db, clients).start();
            new SendOfServer(ss).start();
        }
        while (true);
    }
}

public class Main {
    ArrayList<ClientOfServer> clients = new ArrayList<>();
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
                new clientThread(ss, db, clients).start();
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