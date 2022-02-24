package kerzox.server;

import kerzox.NetworkUtil;
import kerzox.client.ArgumentException;
import kerzox.client.Client;
import kerzox.client.TempData;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Runnable {

    public static int ID = 0;
    public static final int PORT = 2555;

    @Override
    public void run() {
        ServerThread.start();
    }

    private static class ClientThread extends Thread {

        protected Socket client;
        private boolean closed;
        private ServerThread server;


        public ClientThread(ServerThread server, Socket connectingClient) throws IOException {
            this.server = server;
            this.client = connectingClient;
            this.start();
        }

        @Override
        public void run() {
            ID++;

            NetworkUtil.write(client, "id", ID);

            while (!closed) {
                List<Object> data = NetworkUtil.read(this.client);
                if (data == null) continue;
                if (data.isEmpty()) return;
                switch (String.valueOf(data.get(0))) {
                    case "payment" -> {
                        boolean completed = false;
                        for (TempData clientData : this.server.storage.clientList) {
                            if (clientData.getId() == Integer.parseInt(String.valueOf(data.get(3)))) {
                                clientData.addMoney(Double.parseDouble(String.valueOf(data.get(2))));
                                completed = true;
                            }
                            for (Socket c : server.getConnectedClients()) {
                                NetworkUtil.write(c, "refresh", clientData);
                            }
                        }
                        for (TempData clientData : this.server.storage.clientList) {
                            if (clientData.getId() == Integer.parseInt(String.valueOf(data.get(1)))) {
                                if (completed) {
                                    clientData.deductMoney(Double.parseDouble(String.valueOf(data.get(2))));
                                }
                            }
                            for (Socket c : server.getConnectedClients()) {
                                NetworkUtil.write(c, "refresh", clientData);
                            }
                        }
                    }
                    case "account" -> {
                        switch (String.valueOf(data.get(1))) {
                            case "creation" -> {
                                this.server.storage.addClient((TempData) data.get(2));
                                System.out.println("Account created on server");
                            }
                            case "exist" -> {
                                for (TempData clientData : this.server.storage.clientList) {
                                    if (clientData.getId() == (int) data.get(2)) {
                                        NetworkUtil.write(client, "exist", true);
                                        continue;
                                    }
                                    NetworkUtil.write(client, "exist", false);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public static class ServerThread {

        private List<Socket> connectedClients = new ArrayList<>();
        private ServerSocket server;
        private ServerStorage storage = new ServerStorage();

        public static ServerThread start() {
            return new ServerThread();
        }

        public ServerThread() {
            try {
                Initialise(PORT);
            } catch (Exception e) {

            }
        }

        public List<Socket> getConnectedClients() {
            return connectedClients;
        }

        public void Initialise(int port) throws IOException {
            this.server = new ServerSocket(port);
            System.out.println("Server started.\nWaiting for clients...");

            while(!this.server.isClosed()) {
                try {
                    Socket connectingClient = server.accept();
                    if (connectingClient == null) return;
                    this.connectedClients.add(connectingClient);
//                    SocketAddress addr = connectingClient.getRemoteSocketAddress();
//                    if (addr instanceof InetSocketAddress iNetAddr) {
//                        if (iNetAddr.getAddress() instanceof Inet4Address ipv4) {
//                            this.connectedClients.put(ipv4, connectingClient);
//                            System.out.printf("New client connected! %s", ipv4);
//                        }
//                        else if (iNetAddr.getAddress() instanceof Inet6Address ipv6) {
//                            this.connectedClients.put(ipv6, connectingClient);
//                            System.out.printf("New client connected! %s", ipv6);
//                        }
//                    }

                    ClientThread clientThread = new ClientThread(this, connectingClient);

                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }

            }

        }

    }

}
