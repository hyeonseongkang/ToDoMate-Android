package com.mirror.todomate_android.model;

import android.app.Application;
import android.net.Uri;

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

public class ProfileRepository {

    public static final String TAG = "ProfileRepository";

    private DatabaseReference myRef;
    private MutableLiveData<UserProfile> userProfile;

    public ProfileRepository(Application application) {
        myRef = FirebaseDatabase.getInstance().getReference("profiles");
        userProfile = new MutableLiveData<>();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void getUser(String uid) {
        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserProfile profile = snapshot.getValue(UserProfile.class);
                userProfile.setValue(profile);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

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
