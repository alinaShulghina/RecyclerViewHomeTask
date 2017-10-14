package com.example.alin_.animationht.model;


import java.io.Serializable;

/**
 * Created by alin- on 10.10.2017.
 */

public class User implements Serializable {
    private int id;
    private int imageId;
    private String name;

    public User(int id, int imageURL, String name) {
        this.id = id;
        this.imageId = imageURL;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
