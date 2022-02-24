package kerzox.server;

import kerzox.client.Client;
import kerzox.client.TempData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerStorage {

    Set<TempData> clientList = new HashSet<>();

    public ServerStorage() {

    }

    public Set<TempData> getClientList() {
        return clientList;
    }

    public void addClient(TempData client) {
        this.clientList.add(client);
    }

    public void removeClient(TempData client) {
        this.clientList.remove(client);
    }

}
