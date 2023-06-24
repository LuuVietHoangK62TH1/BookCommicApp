package com.technifysoft.comicbookapp.model;

public class Pdf {
    String uid,id,title,category,decreption,url;
    long timeStamp,viewCount;

    public  Pdf(){

    }

    public Pdf(String uid, String id, String title, String category, String decreption, String url, long timeStamp, long viewCount) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.category = category;
        this.decreption = decreption;
        this.url = url;
        this.timeStamp = timeStamp;
        this.viewCount = viewCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDecreption() {
        return decreption;
    }

    public void setDecreption(String decreption) {
        this.decreption = decreption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
