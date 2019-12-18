package com.hhp227.messenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hhp227.messenger.R;
import com.hhp227.messenger.dto.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHoler> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Message> messages;
    private String imageUrl;
    private FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Message> messages, String imageUrl) {
        this.messages = messages;
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public MessageAdapter.ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(viewType == MSG_TYPE_LEFT ? R.layout.chat_item_left : R.layout.chat_item_right, parent, false);
        return new MessageAdapter.ViewHoler(rootView);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHoler holder, int position) {
        Message msg = messages.get(position);
        holder.textMessage.setText(msg.getMessage());
        if (imageUrl.equals("default"))
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        else
            Glide.with(context).load(imageUrl).into(holder.profileImage);
        if (position == messages.size() - 1)
            holder.textSeen.setText(msg.isSeen() ? "읽음" : "전송됨");
        else
            holder.textSeen.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        public TextView textMessage, textSeen;
        public ImageView profileImage;

        public ViewHoler(View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.tv_message);
            profileImage = itemView.findViewById(R.id.iv_profile_image);
            textSeen = itemView.findViewById(R.id.tv_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(firebaseUser.getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }
}
