package kerzox;

import kerzox.client.Client;
import kerzox.server.Server;

public class debugrun {

    public static void main(String[] args) {
        new Thread(new Server()).start();
        new Thread(new Client()).start();
    }

}
