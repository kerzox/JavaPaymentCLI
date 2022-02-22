package kerzox.client;

import java.io.IOException;

public class RunClient {

    public static void main(String[] args) {
        new Thread(new Client()).start();
    }

}
