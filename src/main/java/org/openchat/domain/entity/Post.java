package org.openchat.domain.entity;

public class Post {
    public final String id;
    public final String userId;
    public final String text;
    public final String dateTime;

    public Post(String id, String userId, String text, String dateTime) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.dateTime = dateTime;
    }
}
