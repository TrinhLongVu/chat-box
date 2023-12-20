package org.example.server;

import java.io.*;
import java.net.Socket;

class sendOfServer extends Thread {
    BufferedWriter bw;

    sendOfServer(Socket ss) {
        OutputStream os = null;
        try {
            os = ss.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    public void sendData(String data) {
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
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
