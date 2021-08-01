package server;

import database.Club;
import database.Player;
import database.PlayerList;
import messagetype.AuctionList;
import database.PlayerOnSale;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private static HashMap<String, SocketWrapper> connectedClients;
    private static PlayerList playerList;
    private static List<String> auctionClients;
    private static ServerSocket ss;

    static void initialize() {
        playerList = new PlayerList();
        connectedClients = new HashMap<>();
        auctionClients = new ArrayList<>();
        playerList.load();
    }

    Server(int port) {
        try {
            System.out.println("Server started ... ");
            new ServerIOThread(this);
            ss = new ServerSocket(port);
            while (true) {
                Socket socket = ss.accept();
                SocketWrapper sw = new SocketWrapper(socket);
                new ServerReadThread(sw, this);
            }
        } catch (IOException e) {
            System.out.println("Server closed");
        }
    }


    public synchronized static HashMap<String, SocketWrapper> getConnectedClients() {
        return connectedClients;
    }

    public synchronized static PlayerList getPlayerList() {
        return playerList;
    }

    public synchronized static List<String> getAuctionClients() {
        return auctionClients;
    }

    private AuctionList getAuctionList(String clubName) {
        List<PlayerOnSale> tempList = new ArrayList<>();

        for (PlayerOnSale p : playerList.getPlayerOnSaleList())
            if (!p.getClub().equalsIgnoreCase(clubName)) {
                tempList.add(p);
            }
        return new AuctionList(tempList);
    }


    public synchronized void sendAuctionListToAll() {
        for (String client : auctionClients) {
            try {
                connectedClients.get(client).write(getAuctionList(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean isPlayerOnSale(PlayerOnSale player) {
        for (PlayerOnSale p : playerList.getPlayerOnSaleList())
            if (p.getName().equals(player.getName()))
                return true;
        return false;
    }

    public void save() {
        playerList.save();
    }

    public void close() {
        try {
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initialize();
        new Server(22222);
    }
}
