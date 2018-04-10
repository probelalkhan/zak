package net.simplifiedlearning.zakariaproject.helper;

/**
 * Created by Belal on 9/25/2017.
 */

public class User {
    private int id;
    private String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
