package messagetype;

import server.SocketWrapper;

import java.io.Serializable;

public class LoginMessageClient implements Serializable {
    private String name;
    private boolean toEnter;

    public LoginMessageClient(String name, boolean toEnter, SocketWrapper socket) {
        this.name = name;
        this.toEnter = toEnter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getToEnter() {
        return toEnter;
    }

    public void setToEnter(boolean toEnter) {
        this.toEnter = toEnter;
    }
}
