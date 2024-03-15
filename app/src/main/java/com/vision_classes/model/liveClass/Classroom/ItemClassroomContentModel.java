package com.vision_classes.model.liveClass.Classroom;

public class ItemClassroomContentModel {
    int id;
    int live_course_id;
    int sort;
    boolean is_locked;
    String title, live_time, live_date, short_desc, video_type, video_url, description, access_type, status, created_at, updated_at;

    public ItemClassroomContentModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLive_course_id() {
        return live_course_id;
    }

    public void setLive_course_id(int live_course_id) {
        this.live_course_id = live_course_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLive_time() {
        return live_time;
    }

    public void setLive_time(String live_time) {
        this.live_time = live_time;
    }

    public String getLive_date() {
        return live_date;
    }

    public void setLive_date(String live_date) {
        this.live_date = live_date;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ItemClassroomContentModel(int id, int live_course_id, int sort, boolean is_locked, String title, String live_time, String live_date, String short_desc, String video_type, String video_url, String description, String access_type, String status, String created_at, String updated_at) {
        this.id = id;
        this.live_course_id = live_course_id;
        this.sort = sort;
        this.is_locked = is_locked;
        this.title = title;
        this.live_time = live_time;
        this.live_date = live_date;
        this.short_desc = short_desc;
        this.video_type = video_type;
        this.video_url = video_url;
        this.description = description;
        this.access_type = access_type;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
