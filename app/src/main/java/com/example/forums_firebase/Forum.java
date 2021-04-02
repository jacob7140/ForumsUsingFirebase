package com.example.forums_firebase;

import com.google.firebase.Timestamp;

import java.util.HashSet;

public class Forum {
    String createdByName, title, desc, createdByUid, forumId;
    Timestamp createdAt;
    private HashSet<Forum> likedBy;

    public Forum(String createdByName, String title, String desc, String createdByUid, Timestamp createdAt) {
        this.createdByName = createdByName;
        this.title = title;
        this.desc = desc;
        this.createdByUid = createdByUid;
        this.createdAt = createdAt;
        this.likedBy = new HashSet<>();
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

    public HashSet<Forum> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(HashSet<Forum> likedBy) {
        this.likedBy = likedBy;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "createdByName='" + createdByName + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", createdByUid='" + createdByUid + '\'' +
                ", firumId='" + forumId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
