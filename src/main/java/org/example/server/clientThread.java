package org.example.server;

import java.net.Socket;
import java.util.ArrayList;

class clientThread extends Thread {
    private Socket ss;
    database db;
    ArrayList<structureClient> clients;

    clientThread(Socket a, database d, ArrayList<structureClient> clients) {
        this.ss = a;
        this.db = d;
        this.clients = clients;
    }

    public void run() {
        do {
            new receiveOfServer(ss, db, clients).start();
            new sendOfServer(ss).start();
        }
        while (true);
    }
}
