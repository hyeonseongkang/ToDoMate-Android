package com.mirror.todomate_android.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.todomate_android.R;
import com.mirror.todomate_android.classes.UserProfile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder>{

    private List<UserProfile> userProfiles = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @NotNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_profile_item, parent,false);

        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendHolder holder, int position) {
        UserProfile userProfile = userProfiles.get(position);
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(userProfile.getProfileUri()))
                .into(holder.userProfile);

        holder.userNickName.setText(userProfile.getNickName());
    }

    @Override
    public int getItemCount() { return userProfiles == null ? 0 : userProfiles.size();}

    public void setFriends(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
        notifyDataSetChanged();
    }

    public UserProfile getUserProfileAt(int position) { return userProfiles.get(position); }

    class FriendHolder extends RecyclerView.ViewHolder {
        private CircleImageView userProfile;
        private TextView userNickName;

        public FriendHolder(View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.user_profile);
            userNickName = itemView.findViewById(R.id.user_nick_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(userProfiles.get(position), position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(UserProfile userProfile, int position);
    }

    public void setOnItemClickListener(onItemClickListener listener ) {this.listener= listener;}
}
