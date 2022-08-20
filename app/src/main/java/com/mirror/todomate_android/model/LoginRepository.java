package com.mirror.todomate_android.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.todomate_android.User;

import org.jetbrains.annotations.NotNull;


public class LoginRepository {

    public final static String TAG = "LoginRepository";

    private Application application;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public LoginRepository(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(User user) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor() , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            mUser = mAuth.getCurrentUser();
                            Log.d(TAG, "로그인 성공");
                            Toast.makeText(application, "로그인 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            //Toast.makeText(application, "fail", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "로그인 실패");
                            Toast.makeText(application, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 가입 성공
                            mUser = mAuth.getCurrentUser();
                            Log.d(TAG, "가입 성공");
                            Toast.makeText(application, "가입 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 가입 실패
                            Log.d(TAG, "가입 실패");
                            Toast.makeText(application, "가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public void loginCheck() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "로그인 돼있음");
        } else {
            Log.d(TAG, "로그인 안돼있음");
        }
    }
}

