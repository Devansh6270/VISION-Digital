package com.vision_classes.model.liveClass.NotesClassroomContent;

public class ItemNotesList {




    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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



    String notesTitle;
    String  url, access_type, status, created_at, updated_at;
    int notesId, live_course_id;
    boolean is_locked;

    public int getNotesId() {
        return notesId;
    }

    public void setNotesId(int notesId) {
        this.notesId = notesId;
    }

    public int getLive_course_id() {
        return live_course_id;
    }

    public void setLive_course_id(int live_course_id) {
        this.live_course_id = live_course_id;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public ItemNotesList(){

    }

    public ItemNotesList( String notesTitle) {

        this.notesTitle = notesTitle;
    }



    public String getNotesTitle() {
        return notesTitle;
    }

    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }
}
