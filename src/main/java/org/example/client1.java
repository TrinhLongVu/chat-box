package org.example;

import java.io.*;
import java.net.Socket;

public class client1 {
    public static void main(String arg[])
    {
        try
        {
            Socket s = new Socket("localhost",3001);
            System.out.println(s.getPort());
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            System.out.println("Talking to Server");

            do
            {
//                new receiveThread(br).start();
//                new sendClient(bw).start();
                bw.close();
                br.close();
            }
            while(true);

        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }
    }
}
