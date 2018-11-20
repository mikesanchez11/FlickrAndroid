package com.image.finder.Models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Photo {

    private String id;
    private String owner;
    private String secret;
    private String server;
    private Integer farm;
    private String title;
    @SerializedName("ispublic")
    private Integer isPublic;
    @SerializedName("isfriend")
    private Integer isFriend;
    @SerializedName("isfamily")
    private Integer isFamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getFarm() {
        return farm;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(Integer isFriend) {
        this.isFriend = isFriend;
    }

    public Integer getIsFamily() {
        return isFamily;
    }

    public void setIsFamily(Integer isFamily) {
        this.isFamily = isFamily;
    }
}

