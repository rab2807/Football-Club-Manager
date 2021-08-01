package messagetype;

import database.PlayerOnSale;

import java.io.Serializable;

public class BuyMessageClient implements Serializable {
    PlayerOnSale playerOnSale;

    public BuyMessageClient(PlayerOnSale player) {
        this.playerOnSale = player;
    }

    public PlayerOnSale getPlayerOnSale() {
        return playerOnSale;
    }

    public void setPlayerOnSale(PlayerOnSale playerOnSale) {
        this.playerOnSale = playerOnSale;
    }
}
