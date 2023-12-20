package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class receiveOfServer extends Thread {
    String receiveMsg = "";
    database db;
    BufferedReader br;
    Socket ss;
    ArrayList<structureClient> clients;
    receiveOfServer(Socket ss, database d, ArrayList<structureClient> clients) {
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
                String[] data = receiveMsg.split(",");

                switch (data[0]) {
                    case "TagSignup":
                    {
                        userFormat user = db.postUser(data[1], data[2]);
                        db.insertContenChat(user.getId());
                        break;
                    }

                    case "TagLogin":
                    {
                        userFormat user = db.checkLogin(data[1], data[2]);
                        sendOfServer send = new sendOfServer(ss);
                        if (user != null) {
                            String loginedClient = "login";
                            send.sendData(loginedClient + "," + user.getId() + "," + user.getUsername());

                            for (structureClient client : clients) {
                                sendOfServer sendForPreClient = new sendOfServer(client.nameSocket());
                                String content = db.getContentText(client.getID(), user.getId());
                                sendForPreClient.sendData("new," + user.getId() + "," + user.getUsername() + "," + content);
                                send.sendData("new," + client.getID() + "," + client.getName() + "," + content);
                            }
                            clients.add(new structureClient(user.getId(), ss, user.getUsername()));
                            List<String> group = db.selectContentGroup();

                            for (int i = 0; i < group.size(); i++) {
                                String idAndContent[] = group.get(i).split("%");
                                String dataGroup[] = idAndContent[0].split(",");
                                for (int j = 1; j < dataGroup.length; j++) {
                                    if (user.getId().equals(dataGroup[j])) {
                                        System.out.println("data...." + dataGroup[dataGroup.length - 1]);
                                        send.sendData("group,%" + idAndContent[0] + "%" + idAndContent[1]);
                                    }
                                }
                            }
                        }
                        break;
                    }

                    case "sendMsg":
                    {
                        for (structureClient client : clients) {
                            if (client.getID().equals(data[2])) {
                                db.update(data[1], data[2], data[3] + "#");
                                new sendOfServer(client.nameSocket()).sendData("message," + data[1] + "," + data[3]);
                            }
                        }
                        System.out.println(data[1]);
                        break;
                    }

                    case "delete":
                    {
                        db.deleteContent(data[1], data[2]);
                        break;
                    }

                    case "file":
                    {
                        for (structureClient client : clients) {
                            if (client.getID().equals(data[2])) {
                                System.out.println("file," + data[1] + "," + data[3]);
                                new sendOfServer(client.nameSocket()).sendData("file," + data[1] + "," + data[3]);
                            }
                        }
                        break;
                    }

                    case "group":
                    {
                        db.insertContenChatGroup(receiveMsg);
                        for (int i = 1; i < data.length; i++) {
                            for (structureClient client : clients) {
                                if (client.getID().equals(data[i])) {
                                    System.out.println(data[i]);
                                    new sendOfServer(client.nameSocket()).sendData("group,%" + receiveMsg);
                                }
                            }
                        }
                        break;
                    }

                    case "sendGroup":
                    {
                        db.updateGroup(receiveMsg.split("%")[1], receiveMsg.split("%")[2] + "#");
                        for (int i = 1; i < data.length; i++) {
                            for (structureClient client : clients) {
                                if (client.getID().equals(data[i])) {
                                    System.out.println(data[i]);
                                    new sendOfServer(client.nameSocket()).sendData(receiveMsg);
                                }
                            }
                        }
                        break;
                    }

                    default:
                        break;
                }
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
