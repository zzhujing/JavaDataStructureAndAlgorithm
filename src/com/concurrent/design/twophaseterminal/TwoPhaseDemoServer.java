package com.concurrent.design.twophaseterminal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class TwoPhaseDemoServer extends Thread {


    private final int port;
    private ServerSocket ss;
    private volatile boolean running = true;
    private final List<ClientHandler> clientHandlers = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public TwoPhaseDemoServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
            while (running) {
                Socket client = ss.accept();
                //client工作
                ClientHandler handler = new ClientHandler(client);
                executorService.submit(handler);
                clientHandlers.add(handler);
            }
        } catch (IOException e) {
            //ignore
        } finally {
            this.clean();
        }
    }

    private void clean() {
        System.out.println("server clean .....");
        this.clientHandlers.forEach(ClientHandler::close);
        this.executorService.shutdown();
    }

    public void close() throws IOException {
        this.running = false;
        this.interrupt();
        this.ss.close();
    }
}
