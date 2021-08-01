package server;

import database.Club;
import database.PlayerOnSale;
import messagetype.*;

import java.io.IOException;

public class ServerReadThread implements Runnable {
    SocketWrapper socket;
    Server server;
    Thread thread;

    ServerReadThread(SocketWrapper socket, Server server) {
        System.out.println("ServerReadThread started ... ");
        this.socket = socket;
        this.server = server;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            Object object = null;
            try {
                object = socket.read();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (object instanceof LoginMessageClient) {
                // toEnter-->true, extract club name from client message and search in the database.
                // if found send the club information to client and add client to client list, else send error.
                // toEnter-->false, remove client from client list;

                LoginMessageClient client = (LoginMessageClient) object;
                LoginMessageServer message;
                Club club = Server.getPlayerList().searchClub(client.getName());

                if (((LoginMessageClient) object).getToEnter()) {
                    if (club == null)
                        message = new LoginMessageServer("There is no club with this name", null);
                    else {
                        if (!Server.getConnectedClients().containsKey(club.getName())) {
                            Server.getConnectedClients().put(club.getName(), socket);
                            message = new LoginMessageServer("Successfully logged in", club);
                        } else
                            message = new LoginMessageServer("Already logged in with this name", null);
                    }

                    try {
                        socket.write(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Server.getConnectedClients().remove(club.getName());
                }
            } else if (object instanceof SellMessageClient) {
                // extract PlayerOnSale object, add it to Sales list, nullify the player's club name.
                // update the player's info in database.
                // send updated auction list to clients who have their buy page opened.

                PlayerOnSale playerOnSale = ((SellMessageClient) object).getPlayer();
                playerOnSale.getPlayer().setClub("none");
                Server.getPlayerList().getPlayerOnSaleList().add(playerOnSale);
                Server.getPlayerList().updatePlayer(playerOnSale.getPlayer());
                server.sendAuctionListToAll();
            } else if (object instanceof BuyMessageClient) {
                // extract PlayerOnSale object, remove it from Sales list.
                // update the player's info in database.
                // send updated auction list to clients who have their buy page opened.

                PlayerOnSale playerOnSale = ((BuyMessageClient) object).getPlayerOnSale();
                if (server.isPlayerOnSale(playerOnSale)) {
                    Server.getPlayerList().getPlayerOnSaleList().remove(playerOnSale);
                    Server.getPlayerList().updatePlayer(playerOnSale.getPlayer());
                    server.sendAuctionListToAll();
                }
            } else if (object instanceof AuctionPageMessage) {
                // isToOpen --> true, client notifies server that it has opened its buy page and started a new read thread.
                // server adds the client in auction client list and send the auction list.
                // isToOpen --> false, server sends a null list and client closes its read thread after getting it.
                // server removes the client from auction client list.

                AuctionPageMessage request = (AuctionPageMessage) object;
                if (request.isToOpen()) {
                    Server.getAuctionClients().add(request.getName());
                    server.sendAuctionListToAll();
                } else {
                    try {
                        socket.write(new AuctionList(null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Server.getAuctionClients().remove(request.getName());
                }
            } else if (object instanceof ExitRequest) {
                // when a client window is closed, server terminates this read thread.

                Server.getConnectedClients().remove(((ExitRequest) object).getName());
                break;
            }
        }
        System.out.println("ServerReadThread closed ... ");
        socket.close();
    }
}
