package com.example.forums_firebase;

import com.google.firebase.Timestamp;

public class Comment {
    String createdByName, comment, createdByUid, commentId;
    Timestamp createdAt;

    public Comment(String createdByName, String comment, String createdByUid, Timestamp createdAt) {
        this.createdByName = createdByName;
        this.comment = comment;
        this.createdByUid = createdByUid;
        this.createdAt = createdAt;
    }

    public Comment() {
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedByUid() {
        return createdByUid;
    }

    public void setCreatedByUid(String createdByUid) {
        this.createdByUid = createdByUid;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "createdByName='" + createdByName + '\'' +
                ", comment='" + comment + '\'' +
                ", createdByUid='" + createdByUid + '\'' +
                ", forumId='" + commentId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
