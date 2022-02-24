package kerzox.client;

import kerzox.NetworkUtil;
import kerzox.ParsingUtil;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class DataHandling {

    private Client client;
    private Thread sm;

    public DataHandling(Client client) throws IOException {
        this.client = client;
        printCommands();
        sm = ServerMessages.handle(client);
    }

    public void doArgumentHandler(Scanner scn) throws ArgumentException {
        String[] args = scn.nextLine().split(" ");
        switch (args[0].toLowerCase()) {
            case "account" -> {
                if (args.length <= 1) throw new ArgumentException("Missing arguments.");
                switch (args[1].toLowerCase()) {
                    case "create" -> {
                        attemptAccountCreation(args[2].toLowerCase());
                    }
                    case "delete" -> {
                        NetworkUtil.write(client.getServer(), "debug");
                    }
                    case "balance" -> {
                        System.out.printf("Current balance: %.2f", this.client.getData().getBank());
                    }
                    case "information" -> System.out.println(client.getData().getName() + "\n" + client.getData().getId());
                    default -> throw new ArgumentException("Invalid Argument. %s", String.valueOf(args[2].toLowerCase()));
                }
            }
            case "pay" -> {
                if (args.length <= 3) throw new ArgumentException("Missing arguments.");
                if (!args[1].equalsIgnoreCase("send"))
                    throw new ArgumentException("%s is not a valid argument", args[1]);
                if (!ParsingUtil.isDouble(args[2]))
                    throw new ArgumentException("%s is not a number", args[2]);
                else if (!ParsingUtil.IsInt(args[3]))
                    throw new ArgumentException("%s is not a valid ID, should be a number", args[3]);
                Double money = Double.parseDouble(args[2]);
                if (this.client.getData().getBank() < money) throw new ArgumentException("You don't have enough money to complete the transfer.");
                NetworkUtil.write(this.client.getServer(), "payment", this.client.getData().getId(), args[2], args[3]);
            }
            default -> throw new ArgumentException("Unrecognized command %s, please input a valid command.", String.valueOf(args[0].toLowerCase()));
        }
    }

    private void attemptAccountCreation(String name) {
        this.client.createClient(new TempData(name, 1500, this.client.getID()));
        NetworkUtil.write(this.client.getServer(), "account", "creation", this.client.getData());
    }

    public static void printCommands() {
        System.out.println(Commands());
    }

    public static String Commands() {
        String s = "Client Commands.";
        s += "\nExit | Close the program";
        s += "\nAccount : create {name}, delete {id}, balance";
        s += "\nPay : send {$} {account}";
        return s;
    }

    public static class ServerMessages extends Thread {

        private Client client;

        private ServerMessages(Client client) {
            this.client = client;
            this.start();
        }

        public static Thread handle(Client client) {
            return new ServerMessages(client);
        }

        @Override
        public void run() {

            while(true) {
                try {
                    doArgumentHandler();
                } catch (ArgumentException e) {

                }
            }

        }

        public void doArgumentHandler() throws ArgumentException {
            List<Object> data = NetworkUtil.read(this.client.getServer());
            if (data == null) return;
            if (data.isEmpty()) return;

            switch (String.valueOf(data.get(0))) {
                case "id" -> client.setID(Integer.parseInt(String.valueOf(data.get(1))));
                case "exist" -> {
                    if ((boolean)data.get(1)) {

                    }
                }
                case "refresh" -> {
                    this.client.refreshClientData((TempData) data.get(1));
                }
            }
        }
    }

}
