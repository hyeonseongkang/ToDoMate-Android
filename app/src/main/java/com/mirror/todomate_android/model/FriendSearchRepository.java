package com.mirror.todomate_android.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.todomate_android.classes.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class  FriendSearchRepository {

    public static final String TAG = "FriendSearchRepository";

    private Application application;
    private MutableLiveData<List<User>> allUsers;
    private DatabaseReference myRef;
    private List<User> users;

    boolean valid = false;

    public FriendSearchRepository(Application application) {
        this.application = application;
        myRef = FirebaseDatabase.getInstance().getReference("users");
        allUsers = new MutableLiveData<>();
        users = new ArrayList<>();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void getUsers() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    users.add(user);
                }
                allUsers.setValue(users);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
