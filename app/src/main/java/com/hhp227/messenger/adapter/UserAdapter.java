package com.hhp227.messenger.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.hhp227.messenger.MessageActivity;
import com.hhp227.messenger.R;
import com.hhp227.messenger.dto.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private boolean isChat;

    public UserAdapter(Context context, List<User> users, boolean isChat) {
        this.users = users;
        this.context = context;
        this.isChat = isChat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = users.get(position);
        holder.userName.setText(user.getName());
        if (user.getImageUrl().equals("default"))
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        else
            Glide.with(context).load(user.getImageUrl()).into(holder.profileImage);

        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.imageOn.setVisibility(View.VISIBLE);
                holder.imageOff.setVisibility(View.GONE);
            } else {
                holder.imageOn.setVisibility(View.GONE);
                holder.imageOff.setVisibility(View.VISIBLE);
            }
        } else {
            holder.imageOn.setVisibility(View.GONE);
            holder.imageOff.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView profileImage;
        private ImageView imageOn;
        private ImageView imageOff;

        public ViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tv_name);
            profileImage = itemView.findViewById(R.id.iv_profile_image);
            imageOn = itemView.findViewById(R.id.iv_on);
            imageOff = itemView.findViewById(R.id.iv_off);
        }
    }
}
