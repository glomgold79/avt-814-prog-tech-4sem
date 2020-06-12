package server;

import app.GUI;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Server {
    public static final String SWAP_WITH_CLIENT = "connect client";
    public static final String DISCONNECT = "disconnect";

    static TreeMap<Integer, ConnectionThread> clients = new TreeMap<>();
    static List<Integer> ids = new ArrayList<>();
    private static int id = 0;

    public static void main(String[] args) {
        try {
            System.out.println("server.Server is running");
            int port = 1337;
            ServerSocket serverSocket = new ServerSocket(port);
            new Ssd().start();
            // Ждет клиентов и для каждого создает отдельный поток

            while (true) {
                Socket s = serverSocket.accept();
                ConnectionThread connectionThread = new ConnectionThread(s, id);
                clients.put(id, connectionThread);
                ids.add(id);
                id++;
                connectionThread.start();
                System.out.println(clients);
                update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void update(){
        for (int i = 0; i < ids.size()-1; i++) {
            Server.clients.get(ids.get(i)).updateClients();

        }
    }
    static void updateAll(){
        for (int i = 0; i < ids.size(); i++) {
            Server.clients.get(ids.get(i)).updateClients();

        }
    }
}


class Ssd extends Thread {
    public void run() {
        while (true) {
            System.out.println(Server.clients);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}