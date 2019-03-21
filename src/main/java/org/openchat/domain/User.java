package org.openchat.domain;

public class User {
    public final String id;
    public final String username;
    public final String password;
    public final String about;

    public User(String id, String username, String password, String about) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.about = about;
    }

    public User(String username, String password, String about) {
        this(null, username, password, about);
    }
}
