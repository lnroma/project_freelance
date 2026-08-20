package com.naumoff.rnc.model;

import java.time.LocalDateTime;

public class Activity {
    private String description;
    private String type;          // ORDER_CREATED, PROFILE_UPDATE и т.п.
    private LocalDateTime timestamp;

    public Activity(String description, String type, LocalDateTime timestamp) {
        this.description = description;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Геттеры
    public String getDescription() { return description; }
    public String getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
}