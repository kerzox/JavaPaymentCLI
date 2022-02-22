package kerzox.client;

import kerzox.ParsingUtil;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class DataHandling {

    private DataInputStream  serverInput;
    private DataOutputStream clientOutput;
    private Thread sm;

    public DataHandling(Socket socket) throws IOException {
        this.serverInput = new DataInputStream(socket.getInputStream());
        this.clientOutput = new DataOutputStream(socket.getOutputStream());
        printCommands();
        try {
            doArgumentHandler(new Scanner(System.in));
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }
        sm = ServerMessages.handle(serverInput);
    }

    private void doArgumentHandler(Scanner scn) throws ArgumentException {
        String line = scn.nextLine();
        String[] args = line.split(" ");
        switch (args[0].toLowerCase()) {
            case "account" -> {
                if (args.length <= 2) throw new ArgumentException("Missing arguments.");
                switch (args[1].toLowerCase()) {
                    case "create" -> attemptAccountCreation();
                    case "delete" -> attemptAccountDeletion();
                }
            }
            case "pay" -> {
                if (args.length <= 3) throw new ArgumentException("Missing arguments.");
                if (!args[1].equalsIgnoreCase("send")) throw new ArgumentException(String.format("%s is not a valid argument", args[1]));
                if (!ParsingUtil.isDouble(args[2])) throw new ArgumentException(String.format("%s is not a number", args[2]));
                else if (!ParsingUtil.isDouble(args[3])) throw new ArgumentException(String.format("%s is not a number", args[3]));
                Double money = Double.parseDouble(args[2]);
                System.out.printf("Payment was successful.\n%.2f was deducted from your account.%n", money);
            }
            default -> throw new ArgumentException(String.format("Unknown Command: %s", args[0]));
        }
    }

    private void attemptAccountDeletion() {

    }

    private void attemptAccountCreation() {

    }

    public static void printCommands() {
        System.out.println(Commands());
    }

    public static String Commands() {
        String s = "Client Commands.";
        s += "\nAccount | create {name}, delete {id}";
        s += "\nPay | send {$} {account}";
        return s;
    }

    public static class ServerMessages extends Thread {

        private DataInputStream inputStream;

        private ServerMessages(DataInputStream input) {
            this.inputStream = input;
            this.start();
        }

        public static Thread handle(DataInputStream input) {
            return new ServerMessages(input);
        }

        @Override
        public void run() {

            while(true) {
                try {
                    String msg = inputStream.readUTF();
                    System.out.println(msg);
                } catch (IOException e) {

                }
            }

        }
    }

    public DataOutputStream getClientOutput() {
        return clientOutput;
    }

    public DataInputStream getServerInput() {
        return serverInput;
    }

    //    public static class SendMessages extends Thread {
//
//        private DataOutputStream outputStream;
//
//        private SendMessages(DataOutputStream outputStream) {
//            this.start();
//        }
//
//        public static Thread handle(DataOutputStream outputStream) {
//            return new SendMessages(outputStream);
//        }
//
//        @Override
//        public void run() {
//
//            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
//
//            while(true) {
//                try {
//                    console.
//                } catch (IOException e) {
//
//                }
//            }
//
//        }
//    }



}
