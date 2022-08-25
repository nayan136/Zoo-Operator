package in.amtron.zoooperator.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("auth_key")
    private int id;
    private String name;
    private String mobile;
    private String role;


    public User(int id, String name, String mobile, String role) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getRole() {
        return role;
    }
}
