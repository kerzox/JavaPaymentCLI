package kerzox.server;

import kerzox.client.Client;
import kerzox.client.TempData;

import java.util.*;

public class ServerStorage {

    Map<Integer, TempData> clientList = new HashMap<>();

    public ServerStorage() {

    }

    public Map<Integer, TempData> getClientList() {
        return clientList;
    }

    public void addClient(TempData client) {
        this.clientList.put(client.getId(), client);
    }

    public void removeClient(int id) {
        this.clientList.remove(id);
    }

}
