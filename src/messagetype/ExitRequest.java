package messagetype;

import java.io.Serializable;

public class ExitRequest implements Serializable {

    String name;

    public ExitRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
