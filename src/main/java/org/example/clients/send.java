package org.example.clients;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

class send extends Thread {
    BufferedWriter bw;

    send(Socket ss) {
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
