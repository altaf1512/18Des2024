package com.arin.titik_suara.Model;

public class NotificationModel {
    private String category;
    private String message;
    private boolean isRead;
    private long timestamp;

    // Constructor
    public NotificationModel(String category, String message, long timestamp) {
        this.category = category;
        this.message = message;
        this.isRead = false; // Default to unread
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "category='" + category + '\'' +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", timestamp=" + timestamp +
                '}';
    }
}