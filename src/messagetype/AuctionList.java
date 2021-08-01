package messagetype;

import database.PlayerOnSale;

import java.io.Serializable;
import java.util.List;

public class AuctionList implements Serializable {
    List<PlayerOnSale> players;

    public AuctionList(List<PlayerOnSale> players) {
        this.players = players;
    }

    public List<PlayerOnSale> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerOnSale> players) {
        this.players = players;
    }
}
