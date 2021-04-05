package com.example.forums_firebase;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Forum implements Serializable {
    String createdByName, title, desc, createdByUid, forumId;
    Timestamp createdAt;
    ArrayList<String> likedBy = new ArrayList<>();

    public Forum(String createdByName, String title, String desc, String createdByUid, String forumId, Timestamp createdAt) {
        this.createdByName = createdByName;
        this.title = title;
        this.desc = desc;
        this.createdByUid = createdByUid;
        this.forumId = forumId;
        this.createdAt = createdAt;
    }

    public Forum() {
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatedByUid() {
        return createdByUid;
    }

    public void setCreatedByUid(String createdByUid) {
        this.createdByUid = createdByUid;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(ArrayList<String> likedBy) {
        this.likedBy = likedBy;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "createdByName='" + createdByName + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", createdByUid='" + createdByUid + '\'' +
                ", forumId='" + forumId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
