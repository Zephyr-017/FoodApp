package com.example.networkapi;

public class Presiden {
    private final String name;
    private final String remarks;
    private final String photo;

    public Presiden(String name, String remarks, String photo) {
        this.name = name;
        this.remarks = remarks;
        this.photo = photo;
    }

    public String getName() { return name; }
    public String getRemarks() { return remarks; }
    public String getPhoto() { return photo; }
}