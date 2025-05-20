package com.example.lostfoundapp;

public class LostFoundItem {
    private long   id;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private double latitude;
    private double longitude;
    private String type;

    public LostFoundItem(long id,
                         String name,
                         String phone,
                         String description,
                         String date,
                         String location,
                         double latitude,
                         double longitude,
                         String type) {
        this.id          = id;
        this.name        = name;
        this.phone       = phone;
        this.description = description;
        this.date        = date;
        this.location    = location;
        this.latitude    = latitude;
        this.longitude   = longitude;
        this.type        = type;
    }

    public long   getId()          { return id; }
    public String getName()        { return name; }
    public String getPhone()       { return phone; }
    public String getDescription() { return description; }
    public String getDate()        { return date; }
    public String getLocation()    { return location; }
    public double getLatitude()    { return latitude; }
    public double getLongitude()   { return longitude; }
    public String getType()        { return type; }
}
