package com.mirror.todomate_android.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mirror.todomate_android.R;
import com.mirror.todomate_android.classes.UserProfile;
import com.mirror.todomate_android.databinding.ActivityProfileBinding;
import com.mirror.todomate_android.viewmodel.LoginViewModel;
import com.mirror.todomate_android.viewmodel.ProfileViewModel;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    public final static String TAG = "ProfileActivity";

    private ActivityProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private LoginViewModel loginViewModel;

    String uid;
    Uri tempPhotoUri;
    String userNickName;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fadein_up, R.anim.none);

        // 카메라 권한
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
            }
        }

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        userEmail = intent.getStringExtra("email");

        binding.userEmail.setText(userEmail);

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);
        profileViewModel.getUserProfile().observe(this, new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile profile) {
                Glide.with(ProfileActivity.this)
                        .load(profile.getProfileUri())
                        .into(binding.userProfile);
                binding.userEmail.setText(profile.getEmail());
                binding.userNickName.setText(profile.getNickName());
            }
        });

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.logout();
            }
        });


        profileViewModel.getUser(uid);

        binding.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                launcher.launch(intent);
            }
        });

        binding.userNickName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    userNickName = textView.getText().toString();
                    if (tempPhotoUri == null)
                        return true;
                    UserProfile profile = new UserProfile(uid, userEmail, userNickName, null, tempPhotoUri.toString());
                    profileViewModel.setUser(profile);
                    return true;
                }
                return false;
            }
        });

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_up);
            }
        });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        tempPhotoUri = intent.getData();

                        if (tempPhotoUri != null) {
                            Glide.with(ProfileActivity.this)
                                    .load(tempPhotoUri)
                                    .into(binding.userProfile);
                        }
                    }
                }
            });
}