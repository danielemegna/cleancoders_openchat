package org.openchat.domain.entity;

import java.time.ZonedDateTime;

public class Post {
    public final String id;
    public final String userId;
    public final String text;
    public final ZonedDateTime dateTime;

    public Post(String userId, String text) {
        this(null, userId, text, null);
    }

    public Post(String userId, String text, ZonedDateTime dateTime) {
        this(null, userId, text, dateTime);
    }

    public Post(String id, String userId, String text, ZonedDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.dateTime = dateTime;
    }
}
