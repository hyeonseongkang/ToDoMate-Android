package com.mirror.todomate_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.todomate_android.classes.UserProfile;
import com.mirror.todomate_android.model.ProfileRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository repository;
    private LiveData<UserProfile> userProfile;
    private LiveData<List<UserProfile>> allProfiles;
    private LiveData<List<UserProfile>> allFriends;
    private LiveData<Boolean> addFriendCheck;

    public ProfileViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new ProfileRepository(application);
        userProfile = repository.getUserProfile();
        allProfiles = repository.getAllProfiles();
        allFriends = repository.getAllFriends();
        addFriendCheck = repository.addFirendCheck();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<List<UserProfile>> getAllProfiles() { return allProfiles;}

    public LiveData<List<UserProfile>> getAllFriends() { return allFriends; }

    public LiveData<Boolean> addFriendCheck() { return addFriendCheck; }

    public void getFriends(String uid) { repository.getFriends(uid);}

    public void getUser(String uid) {
        repository.getUser(uid);
    }

    public void setUser(UserProfile profile) {
        repository.setUser(profile);
    }

    public void getUsersProfile() { repository.getUsersProfile();}

    public void addFriend(List<UserProfile> usersProfile, String uid, String userNickName) { repository.addFriend(usersProfile, uid, userNickName);}


}
