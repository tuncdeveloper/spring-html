package com.quest_app.quest.DTO;

import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;


public class CommentDto {

    private Integer commentId;
    private String text;
    private Integer postIdFk;
    private Integer userIdFk;
    private String userName;  // Kullanıcı adı eklendi

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPostIdFk() {
        return postIdFk;
    }

    public void setPostIdFk(Integer postIdFk) {
        this.postIdFk = postIdFk;
    }

    public Integer getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(Integer userIdFk) {
        this.userIdFk = userIdFk;
    }
}


