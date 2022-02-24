package kerzox.client;

import java.io.Serializable;
import java.net.Socket;

public class TempData implements Serializable {

    private String name;
    private double bank;
    private int id;

    public TempData(String name, double bank, int id) {
        this.name = name;
        this.bank = bank;
        this.id = id;
    }

    public void setBank(double bank) {
        this.bank = bank;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getBank() {
        return bank;
    }

    public void addMoney(double value) {
        this.bank += value;
    }

    public void deductMoney(double value) {
        this.bank -= value;
    }

}
