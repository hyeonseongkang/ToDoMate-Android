package com.mirror.todomate_android.model;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mirror.todomate_android.classes.Todo;
import com.mirror.todomate_android.classes.UserProfile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {

    public static final String TAG = "ProfileRepository";

    private DatabaseReference myRef;
    private MutableLiveData<UserProfile> userProfile;
    private MutableLiveData<List<UserProfile>> allProfiles;
    private MutableLiveData<List<UserProfile>> allFriends;
    private MutableLiveData<Boolean> addFriendCheck;
    List<UserProfile> userProfiles;
    List<UserProfile> friends;

    public ProfileRepository(Application application) {
        myRef = FirebaseDatabase.getInstance().getReference("profiles");
        userProfile = new MutableLiveData<>();
        allProfiles = new MutableLiveData<>();
        allFriends = new MutableLiveData<>();
        addFriendCheck = new MutableLiveData<>();
        userProfiles = new ArrayList<>();
        friends = new ArrayList<>();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<List<UserProfile>> getAllProfiles() {
        return allProfiles;
    }

    public LiveData<List<UserProfile>> getAllFriends() {
        return allFriends;
    }

    public LiveData<Boolean> addFirendCheck() {
        return addFriendCheck;
    }

    // 친구 목록을 가져오는 메서드
    public void getFriends(String uid) {
        myRef.child(uid).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                friends.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserProfile profile = snapshot1.getValue(UserProfile.class);
                    friends.add(profile);
                }
                allFriends.setValue(friends);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // 모든 User의 Profile을 가져오는 메서드
    public void getUsersProfile() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userProfiles.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserProfile profile = snapshot1.getValue(UserProfile.class);
                    userProfiles.add(profile);
                }
                allProfiles.setValue(userProfiles);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // 친구 추가 메서드
    public void addFriend(List<UserProfile> usersProfile, String uid, String userNickName) {
        for (UserProfile userProfile : usersProfile) {

            // user목록에 인자 값으로 넘어오는 user가 있다면
            if (userProfile.getNickName().equals(userNickName)) {

                // 친구가 이미 되어 있는지 중복 체크
                myRef.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        boolean overlapCheck = true;
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            UserProfile profile = snapshot1.getValue(UserProfile.class);
                            if (profile.getNickName().equals(userNickName))
                                overlapCheck = false;
                        }

                        // 중복이 아니라면 친구 추가
                        if (overlapCheck) {
                            myRef.child(uid).child("friends").push().setValue(userProfile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                addFriendCheck.setValue(true);
                                        }
                                    });
                        } else {
                            addFriendCheck.setValue(false);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        }
    }

    // user profile 가져오는 메서드
    public void getUser(String uid) {
        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserProfile profile = snapshot.getValue(UserProfile.class);
                if (profile != null)
                    userProfile.setValue(profile);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // user profile 등록 메서드
    public void setUser(UserProfile profile) {
        String uid = profile.getUid();
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("profiles/" + uid + ".jpg");
        UploadTask uploadTask = storage.putFile(Uri.parse(profile.getTempPhotoUri()));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri profileUri = uri;
                        profile.setProfileUri(profileUri.toString());

                        myRef.child(uid).setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful())
                                    getUser(uid);
                            }
                        });
                    }
                });
            }
        });

    }


}
