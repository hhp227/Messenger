package com.hhp227.messenger.dto;

public class User {
    private String id;
    private String name;
    private String imageUrl;
    private String status;

    public User() {
    }

    public User(String id, String name, String imageUrl, String status) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
