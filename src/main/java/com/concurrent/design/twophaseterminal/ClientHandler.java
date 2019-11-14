package com.concurrent.design.twophaseterminal;

import java.io.*;
import java.net.Socket;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class ClientHandler implements Runnable {

    private final Socket client;
    private volatile boolean running = true;

    public ClientHandler(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {

        try (InputStream is = client.getInputStream();
             OutputStream os = client.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is));
             PrintWriter pw = new PrintWriter(os)
        ) {
            while (running) {
                final String result = br.readLine();
                System.out.println("Come from client : " + result);
                pw.write("echo " + result);
                pw.flush();
            }
        } catch (IOException e) {
            //客户端断开
            this.running = false;
        }
    }


    public void close() {
        //balking pattern
        if (!running) {
            return;
        }
        try {
            this.running = false;
            client.close();
        } catch (IOException e) {
            System.out.println("client close error .");
        }
    }
}
