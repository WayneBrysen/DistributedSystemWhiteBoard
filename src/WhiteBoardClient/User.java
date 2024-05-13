package WhiteBoardClient;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private boolean isConnected;
    private boolean isKicked;

    public User(String username) {
        this.username = username;
        this.isConnected = true;
        this.isKicked = false;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isKicked() {
        return isKicked;
    }

    public void setKicked(boolean isKicked) {
        this.isKicked = isKicked;
    }

}
