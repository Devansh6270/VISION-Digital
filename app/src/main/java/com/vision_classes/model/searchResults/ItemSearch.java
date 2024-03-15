package com.vision_classes.model.searchResults;

public class ItemSearch {

    String title;
    String duration;
    String imageUrl;
    String id;
    String ownedBy;
    String description;
    String type;
    String cid;

    public ItemSearch(String title, String duration, String imageUrl, String id, String ownedBy, String description, String type, String cid) {
        this.title = title;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.id = id;
        this.ownedBy = ownedBy;
        this.description = description;
        this.type = type;
        this.cid = cid;
    }

    public ItemSearch() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
