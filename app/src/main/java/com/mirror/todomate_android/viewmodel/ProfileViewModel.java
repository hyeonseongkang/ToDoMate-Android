package com.mirror.todomate_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.todomate_android.classes.UserProfile;
import com.mirror.todomate_android.model.ProfileRepository;

import org.jetbrains.annotations.NotNull;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository repository;
    private LiveData<UserProfile> userProfile;

    public ProfileViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new ProfileRepository(application);
        userProfile = repository.getUserProfile();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void getUser(String uid) {
        repository.getUser(uid);
    }

    public void setUser(UserProfile profile) {
        repository.setUser(profile);
    }
}
