package messagetype;

import database.Club;

import java.io.Serializable;

public class LoginMessageServer implements Serializable {
    private String message;
    private Club club;

    public LoginMessageServer(String message, Club club) {
        this.message = message;
        this.club = club;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
