package org.example;

import java.io.*;
import java.net.Socket;

public class client {
    public static void main(String arg[])
    {
        try
        {
            Socket s = new Socket("localhost",3001);
            System.out.println(s.getPort());
            System.out.println("Talking to Server");
            do
            {
                new receiveThread(s).start();
                new sendClient(s).start();
            }
            while(true);
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }
    }
}
