package kerzox.common;

import kerzox.client.TempData;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkUtil {

    public enum Header {
        PAYMENT,
        ACCOUNT,
        MSG,
        ID,
        REFRESH;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public static void write(Socket socket, Object... data) {
        try {
            OutputStream output = socket.getOutputStream();
            output.flush();
            List<Object> toSend = new ArrayList<>(Arrays.asList(data));
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(toSend);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> read(Socket socket) {
        try {
            InputStream input = socket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(input);
            Object ret = in.readObject();
            return (List<Object>) ret;
        } catch (IOException | ClassNotFoundException e) {
            closeSocket(socket);
        }
        return null;
    }

    private static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
