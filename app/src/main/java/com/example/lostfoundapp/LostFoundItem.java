package com.example.lostfoundapp;

public class LostFoundItem {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private String contact;

    public LostFoundItem(int id, String title, String description, String location, String type, String contact) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.contact = contact;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public String getContact() { return contact; }
}