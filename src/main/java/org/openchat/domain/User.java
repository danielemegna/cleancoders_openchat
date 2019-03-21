package org.openchat.domain;

public class User {
    public final String username;
    public final String password;
    public final String about;

    public User(String username, String password, String about) {
        this.username = username;
        this.password = password;
        this.about = about;
    }
}
