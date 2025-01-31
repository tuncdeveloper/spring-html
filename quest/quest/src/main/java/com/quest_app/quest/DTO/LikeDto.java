package com.quest_app.quest.DTO;

public class LikeDto {

    private Integer likeId;
    private Integer userIdFk;
    private Integer postIdFk;
    private String userName;
    private String postTitle;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public Integer getLikeId() {
        return likeId;
    }

    public void setLikeId(Integer likeId) {
        this.likeId = likeId;
    }

    public Integer getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(Integer userIdFk) {
        this.userIdFk = userIdFk;
    }

    public Integer getPostIdFk() {
        return postIdFk;
    }

    public void setPostIdFk(Integer postIdFk) {
        this.postIdFk = postIdFk;
    }
}
