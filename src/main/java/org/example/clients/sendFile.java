package org.example.clients;

import java.io.*;
import java.net.Socket;

class sendFile extends Thread {
    BufferedWriter bw;
    String file;
    Socket ss;
    String idSend;
    String idRecieve;

    sendFile(Socket ss, String idSend, String idRecieve, String file) {
        this.idSend = idSend;
        this.idRecieve = idRecieve;
        this.ss = ss;
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
