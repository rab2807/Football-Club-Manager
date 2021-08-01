package messagetype;

import database.Player;

import java.io.Serializable;

public class BuyMessageServer implements Serializable {
    Player player;
    boolean verdict;

    public BuyMessageServer(Player player) {
        this.player = player;
        this.verdict = verdict;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
