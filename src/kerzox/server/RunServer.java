package kerzox.server;
public class RunServer {

    public static void main(String[] args) {
        new Thread(new Server()).start();
    }

}
