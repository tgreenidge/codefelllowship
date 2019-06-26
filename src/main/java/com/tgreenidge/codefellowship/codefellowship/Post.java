package com.tgreenidge.codefellowship.codefellowship;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    AppUser creator;

    String body;
    Timestamp createdAt;

    public Post() {}

    public Post(String body) {
        this.body = body;
        this.createdAt = new Timestamp(new Date().getTime());
    }

    public void setId(long id) {
        this.id = id;
    }

    public com.tgreenidge.codefellowship.codefellowship.AppUser getCreator() {
        return creator;
    }

    public void setCreator(com.tgreenidge.codefellowship.codefellowship.AppUser creator) {
        this.creator = creator;
    }

    public String getBody() {
        return body;
    }

    public long getId() {
        return id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
