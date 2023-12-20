package org.example.server;

import java.net.Socket;

class structureClient {
    private String id;
    private String name;
    private Socket ss;

    structureClient(String id, Socket ss, String name) {
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
