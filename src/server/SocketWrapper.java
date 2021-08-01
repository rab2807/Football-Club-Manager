package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class SocketWrapper {
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public SocketWrapper(String address, int port) {
        try {
            socket = new Socket(address, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketWrapper(Socket socket) {
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public Object read() throws IOException, ClassNotFoundException{
        Object object = null;
        object = ois.readUnshared();
        return object;
    }

    public void write(Object object) throws IOException{
        oos.writeUnshared(object);
    }


    public void close() {
        try {
            oos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
