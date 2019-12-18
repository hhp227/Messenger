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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.hhp227.messenger.MessageActivity;
import com.hhp227.messenger.R;
import com.hhp227.messenger.dto.Message;
import com.hhp227.messenger.dto.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private boolean isChat;
    private String theLastMessage;

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
        if (isChat)
            lastMessage(user.getId(), holder.lastMessage);
        else
            holder.lastMessage.setVisibility(View.GONE);

        if (isChat) {
            boolean isOnline = user.getStatus().equals("online");
            holder.imageOn.setVisibility(isOnline ? View.VISIBLE : View.GONE);
            holder.imageOff.setVisibility(isOnline ? View.GONE : View.VISIBLE);
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
        public TextView userName, lastMessage;
        public ImageView profileImage;
        private ImageView imageOn;
        private ImageView imageOff;

        public ViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tv_name);
            profileImage = itemView.findViewById(R.id.iv_profile_image);
            imageOn = itemView.findViewById(R.id.iv_on);
            imageOff = itemView.findViewById(R.id.iv_off);
            lastMessage = itemView.findViewById(R.id.tv_last_msg);
        }
    }

    private void lastMessage(final String userId, final TextView lastMessage) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Messages");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getReceiver().equals(firebaseUser.getUid()) && message.getSender().equals(userId) || message.getReceiver().equals(userId) && message.getSender().equals(firebaseUser.getUid()))
                        theLastMessage = message.getMessage();
                }
                switch (theLastMessage) {
                    case "default" :
                        lastMessage.setText("No Message");
                        break;
                    default :
                        lastMessage.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
