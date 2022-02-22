package kerzox.server;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {

    public static final int PORT = 2555;

    @Override
    public void run() {
        ServerThread.start();
    }

    private static class ClientThread extends Thread {

        protected Socket client;

        public ClientThread(Socket connectingClient) {
            this.client = connectingClient;
        }

        @Override
        public void run() {

        }

    }

    public static class ServerThread {

        private Map<InetAddress, Socket> connectedClients = new HashMap<>();
        private ServerSocket server;

        public static ServerThread start() {
            return new ServerThread();
        }

        public ServerThread() {
            try {
                Initialise(PORT);
            } catch (Exception e) {

            }
        }

        public void Initialise(int port) throws IOException {

            this.server = new ServerSocket(port);
            System.out.println("Server started.\nWaiting for clients...");

            while(!this.server.isClosed()) {
                try {
                    Socket connectingClient = server.accept();
                    if (connectingClient == null) return;

                    System.out.println("New client connected!");

                    SocketAddress addr = connectingClient.getRemoteSocketAddress();

                    if (addr instanceof InetSocketAddress iNetAddr) {
                        if (iNetAddr.getAddress() instanceof Inet4Address ipv4) {
                            this.connectedClients.put(ipv4, connectingClient);

                        }
                        else if (iNetAddr.getAddress() instanceof Inet6Address ipv6) {
                            this.connectedClients.put(ipv6, connectingClient);
                        }
                    }

                    ClientThread clientThread = new ClientThread(connectingClient);

                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }

            }

        }

    }

}
