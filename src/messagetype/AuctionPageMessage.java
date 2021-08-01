package messagetype;

import java.io.Serializable;

public class AuctionPageMessage implements Serializable {
    private String name;
    private boolean toOpen;

    public AuctionPageMessage(String name, boolean toOpen) {
        this.name = name;
        this.toOpen = toOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isToOpen() {
        return toOpen;
    }

    public void setToOpen(boolean toOpen) {
        this.toOpen = toOpen;
    }
}
