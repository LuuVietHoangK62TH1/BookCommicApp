package com.technifysoft.comicbookapp.model;

public class Category {
    private String id,category,udi;
    private long timestamp;


    //    vd cần 1 hàm tạo ko tham số mới có thể pass qua được bug these constructors are not stripped
    public Category(){

    }

    public Category(String id, String category, String udi, long timestamp) {
        this.id = id;
        this.category = category;
        this.udi = udi;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUdi() {
        return udi;
    }

    public void setUdi(String udi) {
        this.udi = udi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
