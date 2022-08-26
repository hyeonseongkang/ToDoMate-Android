package com.mirror.todomate_android.classes;

public class UserProfile {
    private String uid;
    private String email;
    private String nickName;
    private String profileUri;
    private String tempPhotoUri;

    public UserProfile() {}

    public UserProfile(String uid, String email, String nickName, String profileUri, String tempPhotoUri) {
        this.uid = uid;
        this.email = email;
        this.nickName = nickName;
        this.profileUri = profileUri;
        this.tempPhotoUri = tempPhotoUri;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public String getTempPhotoUri() {
        return tempPhotoUri;
    }
}
