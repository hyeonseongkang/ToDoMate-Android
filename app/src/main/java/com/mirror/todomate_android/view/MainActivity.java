package com.mirror.todomate_android.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.todomate_android.R;
import com.mirror.todomate_android.adapter.FriendAdapter;
import com.mirror.todomate_android.adapter.TodoAdapter;
import com.mirror.todomate_android.classes.Todo;
import com.mirror.todomate_android.classes.UserProfile;
import com.mirror.todomate_android.databinding.ActivityMainBinding;
import com.mirror.todomate_android.viewmodel.LoginViewModel;
import com.mirror.todomate_android.viewmodel.ProfileViewModel;
import com.mirror.todomate_android.viewmodel.TodoListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "MainActivity";

    private TodoListViewModel todoListViewModel;
    private LoginViewModel loginViewModel;
    private ProfileViewModel profileViewModel;
    private ActivityMainBinding binding;
    private FirebaseUser user;

    String selected_date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.loginCheck();
        user = loginViewModel.getUser().getValue();

        binding.mainTodosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainTodosRecyclerView.setHasFixedSize(true);

        binding.mainFriendsRecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.mainFriendsRecyclerview.setHasFixedSize(true);

        TodoAdapter todoAdapter = new TodoAdapter(user.getUid());
        binding.mainTodosRecyclerView.setAdapter(todoAdapter);

        FriendAdapter friendAdapter = new FriendAdapter();
        binding.mainFriendsRecyclerview.setAdapter(friendAdapter);

        // Item Click -> Edit
        todoAdapter.setOnItemClickListener(new TodoAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Todo todo, int position) {
                if (todo.getUid().equals(user.getUid())) {
                    Intent intent = new Intent(MainActivity.this, AddEditTodoActivity.class);
                    intent.putExtra(AddEditTodoActivity.EXTRA_POSITION, position);
                    intent.putExtra(AddEditTodoActivity.EXTRA_KEY, todo.getKey());
                    intent.putExtra(AddEditTodoActivity.EXTRA_EMAIL, todo.getEmail());
                    intent.putExtra(AddEditTodoActivity.EXTRA_DATE, todo.getDate());
                    intent.putExtra(AddEditTodoActivity.EXTRA_TITLE, todo.getTitle());
                    intent.putExtra(AddEditTodoActivity.EXTRA_CONTENT, todo.getContent());
                    intent.putExtra(AddEditTodoActivity.EXTRA_HOUR, todo.getHour());
                    intent.putExtra(AddEditTodoActivity.EXTRA_MINUTE, todo.getMinute());

                    editLauncher.launch(intent);
                }
            }
        });

        // Complete Check Click
        todoAdapter.setOnItemCheckedListener(new TodoAdapter.onItemCheckedListener() {
            @Override
            public void onItemChecked(Todo todo, int position) {
                todoListViewModel.updateTodo(user.getUid(), todo.getDate(), todo, position);
            }
        });

        friendAdapter.setOnItemClickListener(new FriendAdapter.onItemClickListener() {
            @Override
            public void onItemClick(UserProfile userProfile, int position) {
                binding.progressBar.setVisibility(View.VISIBLE);
                todoListViewModel.getTodos(userProfile.getUid(), selected_date);
            }
        });

        todoListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TodoListViewModel.class);
        todoListViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                binding.progressBar.setVisibility(View.GONE);
                todoAdapter.setTodos(todos);
            }
        });

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);
        profileViewModel.getUserProfile().observe(this, new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile profile) {
                if (profile != null) {
                    Glide.with(MainActivity.this)
                            .load(profile.getProfileUri())
                            .into(binding.userProfile);
                    binding.userNickName.setText(profile.getNickName());
                } else {
                    Glide.with(MainActivity.this)
                            .load(R.drawable.basic_profile)
                            .into(binding.userProfile);
                    binding.userNickName.setText(user.getEmail());
                }

            }
        });

        profileViewModel.getUser(user.getUid());

        todoListViewModel.getCurrentUser().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String userName) {
                binding.currentUserTodoList.setText(userName+"님의 Todo List");
            }
        });

        profileViewModel.getAllFriends().observe(this, new Observer<List<UserProfile>>() {
            @Override
            public void onChanged(List<UserProfile> userProfiles) {
                friendAdapter.setFriends(userProfiles);
            }
        });

        profileViewModel.getFriends(user.getUid());

        binding.today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                dateLauncher.launch(intent);
            }
        });

        // 현재 날짜 가져온 뒤 todo list 가져옴
        selected_date = todoListViewModel.getToday();
        binding.today.setText(selected_date);
        binding.progressBar.setVisibility(View.VISIBLE);
        todoListViewModel.getTodos(user.getUid(), selected_date);

        // mainTodoRecyclerView Swipe 동작
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Todo todo = todoAdapter.getTodoAt(viewHolder.getAdapterPosition());
                if (todo.getUid().equals(user.getUid())) {
                    todoListViewModel.deleteTodo(user.getUid(), todo.getDate(), todo, position);
                }

            }
        }).attachToRecyclerView(binding.mainTodosRecyclerView);

        binding.addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditTodoActivity.class);
                intent.putExtra(AddEditTodoActivity.EXTRA_DATE, selected_date);
                addLauncher.launch(intent);
            }
        });

        binding.friendSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FriendSearchActivity.class);
                intent.putExtra("uid", user.getUid());
                addFirendLauncher.launch(intent);
                //startActivity(intent);
            }
        });

        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("uid", user.getUid());
                intent.putExtra("email", user.getEmail());
                startActivity(intent);
            }
        });

    }

    ActivityResultLauncher<Intent> addFirendLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        boolean addFriend = intent.getBooleanExtra("check", false);
                        if (addFriend)
                            profileViewModel.getFriends(user.getUid());
                    }
                }
            });

    ActivityResultLauncher<Intent> dateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        String date = intent.getStringExtra("date");
                        Log.d(TAG, date );
                        binding.today.setText(date);
                        todoListViewModel.getTodos(user.getUid(), date);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        selected_date = date;
                    }
                }
            });


    ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        int position = intent.getIntExtra(AddEditTodoActivity.EXTRA_POSITION, -1);
                        String key = intent.getStringExtra(AddEditTodoActivity.EXTRA_KEY);
                        String email = intent.getStringExtra(AddEditTodoActivity.EXTRA_EMAIL);
                        String date = intent.getStringExtra(AddEditTodoActivity.EXTRA_DATE);
                        String title = intent.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
                        String content = intent.getStringExtra(AddEditTodoActivity.EXTRA_CONTENT);
                        String hour = intent.getStringExtra(AddEditTodoActivity.EXTRA_HOUR);
                        String minute = intent.getStringExtra(AddEditTodoActivity.EXTRA_MINUTE);

                        // //String key, String title, String email, String date, String content
                        Todo todo = new Todo(user.getUid(), key, title, email, date, content, hour, minute, false);

                        Log.d(TAG, user.getUid());
                        Log.d(TAG, key);
                        Log.d(TAG, date);
                        todoListViewModel.updateTodo(user.getUid(), date, todo, position);
                    }
                }
            });

    ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        String date = intent.getStringExtra(AddEditTodoActivity.EXTRA_DATE);
                        String title = intent.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
                        String content = intent.getStringExtra(AddEditTodoActivity.EXTRA_CONTENT);
                        String hour = intent.getStringExtra(AddEditTodoActivity.EXTRA_HOUR);
                        String minute = intent.getStringExtra(AddEditTodoActivity.EXTRA_MINUTE);

                        //String key, String title, String email, String date, String content
                        todoListViewModel.insertTodo(user.getUid(), date, new Todo(user.getUid(), null, title, user.getEmail(), date, content, hour, minute, false));
                    }
                }
            });
}