package kerzox.server;

import kerzox.common.NetworkUtil;
import kerzox.client.TempData;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kerzox.common.NetworkUtil.Header.MSG;
import static kerzox.common.NetworkUtil.Header.REFRESH;

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
            NetworkUtil.write(client, NetworkUtil.Header.ID, ID);
            this.server.getConnectedClients().put(ID, this.client);

            while (!client.isClosed()) {
                List<Object> data = NetworkUtil.read(this.client);
                if (data == null) continue;
                if (data.isEmpty()) return;
                switch ((NetworkUtil.Header)data.get(0)) {
                    case PAYMENT -> {
                        double m = Double.parseDouble(String.valueOf(data.get(2)));
                        int sender = Integer.parseInt(String.valueOf(data.get(1)));
                        int recipient = Integer.parseInt(String.valueOf(data.get(3)));

                        TempData recipientClient = this.server.storage.clientList.get(recipient);
                        TempData senderClient = this.server.storage.clientList.get(sender);

                        if (recipientClient == null) {
                            NetworkUtil.write(this.server.getClientSocketFromData(senderClient), MSG, "The account you have entered doesn't exist");
                            continue;
                        }

                        recipientClient.addMoney(m);
                        senderClient.deductMoney(m);

                        NetworkUtil.write(this.server.getClientSocketFromData(recipientClient), REFRESH, recipientClient);
                        NetworkUtil.write(this.server.getClientSocketFromData(recipientClient), MSG, "You have received $" + m + " from " + senderClient.getName());
                        NetworkUtil.write(this.server.getClientSocketFromData(senderClient), REFRESH, senderClient);
                        NetworkUtil.write(this.server.getClientSocketFromData(senderClient), MSG, "Payment has been sent! $" + m + " has been deducted.");

                    }
                    case ACCOUNT -> {
                        switch (String.valueOf(data.get(1))) {
                            case "creation" -> {
                                this.server.storage.addClient((TempData) data.get(2));
                                System.out.println("Account created: " + ((TempData) data.get(2)).getName());
                            }
                        }
                    }
                }
            }

            System.out.printf("Client [%s] has been disconnected.\n", this.client.getInetAddress());

        }
    }

    public static class ServerThread {

        private Map<Integer, Socket> connectedClients = new HashMap<>();
        private ServerSocket server;
        private ServerStorage storage = new ServerStorage();

        public static ServerThread start() {
            return new ServerThread();
        }

        public ServerThread() {
            try {
                Initialise(PORT);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        public Socket getClientSocketFromData(TempData data) {
            return this.connectedClients.get(data.getId());
        }

        public Map<Integer, Socket> getConnectedClients() {
            return connectedClients;
        }

        public void Initialise(int port) throws IOException {
            this.server = new ServerSocket(port);
            System.out.println("Server started.\nWaiting for clients...");

            while(!this.server.isClosed()) {
                try {
                    Socket connectingClient = server.accept();
                    if (connectingClient == null) return;
                    ClientThread clientThread = new ClientThread(this, connectingClient);
                    System.out.println("Client connected to server");

                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }

            }

        }

    }

}
