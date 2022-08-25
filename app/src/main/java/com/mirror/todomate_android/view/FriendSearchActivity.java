package com.mirror.todomate_android.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mirror.todomate_android.R;
import com.mirror.todomate_android.classes.User;
import com.mirror.todomate_android.databinding.ActivityFriendSearchBinding;
import com.mirror.todomate_android.viewmodel.FriendSearchViewModel;

import java.util.List;

public class FriendSearchActivity extends AppCompatActivity {

    public static final String TAG = "FriendSearchActivity";

    ActivityFriendSearchBinding binding;
    private FriendSearchViewModel friendSearchViewModel;
    private String userNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_up, R.anim.none);

        friendSearchViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(FriendSearchViewModel.class);
        friendSearchViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                boolean valid = false;
                for (User user : users) {
                    // user.getNickName().equals(userNickName)
                   if (user.getEmail().equals(userNickName)) {
                       valid = true;
                       break;
                   }
                }

                if (valid) {
                    Log.d(TAG, "친구 추가 함");
                } else {
                    Log.d(TAG, "존재하지 않는 닉네임 입니다.");
                }
            }
        });



        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNickName = binding.userNickName.getText().toString();
                friendSearchViewModel.getUsers();
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