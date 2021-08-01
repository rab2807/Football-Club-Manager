package server;

import java.util.Scanner;

public class ServerIOThread implements Runnable {

    Scanner input;
    Server server;

    ServerIOThread(Server server) {
        this.server = server;
        input = new Scanner(System.in);
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("ServerIOThread started");
        while (true) {
            String text = input.next();
            if (text.equalsIgnoreCase("close")) {
                if (server.getConnectedClients().isEmpty()) break;
                else System.out.println("All clients must be disconnected");
            }
        }
        server.save();
        server.close();
        System.out.println("ServerIOThread closed");
    }
}
