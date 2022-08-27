package com.mirror.todomate_android.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.classes.UserProfile;
import com.mirror.todomate_android.databinding.ActivityFriendSearchBinding;
import com.mirror.todomate_android.viewmodel.ProfileViewModel;

import java.util.List;

public class FriendSearchActivity extends AppCompatActivity {

    public static final String TAG = "FriendSearchActivity";

    ActivityFriendSearchBinding binding;
    private ProfileViewModel profileViewModel;
    private String userNickName;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_up, R.anim.none);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);
        profileViewModel.getAllProfiles().observe(this, new Observer<List<UserProfile>>() {
            @Override
            public void onChanged(List<UserProfile> userProfiles) {
                profileViewModel.addFriend(userProfiles, uid, userNickName);
            }
        });

        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNickName = binding.userNickName.getText().toString();
                profileViewModel.getUsersProfile();
            }
        });

        binding.closeButton.setColorFilter(Color.parseColor("#FFFFFF"));
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_up);
            }
        });
    }
}