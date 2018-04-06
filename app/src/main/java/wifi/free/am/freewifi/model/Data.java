package wifi.free.am.freewifi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mhers on 13/02/2018.
 */

public class Data implements Serializable {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    private int level;
    private boolean isConect;



    public Data(String username, String password,int level) {
        this.username = username;
        this.password = password;
        this.level=level;
    }
    public Data(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isConect() {
        return isConect;
    }

    public void setConect(boolean conect) {
        isConect = conect;
    }
}
