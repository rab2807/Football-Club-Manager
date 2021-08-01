package messagetype;

import database.PlayerOnSale;

import java.io.Serializable;

public class SellMessageClient implements Serializable {
    PlayerOnSale player;

    public SellMessageClient(PlayerOnSale player) {
        this.player = player;
    }

    public PlayerOnSale getPlayer() {
        return player;
    }

    public void setPlayer(PlayerOnSale player) {
        this.player = player;
    }
}
