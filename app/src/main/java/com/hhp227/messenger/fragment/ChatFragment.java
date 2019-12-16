package com.hhp227.messenger.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.hhp227.messenger.R;
import com.hhp227.messenger.adapter.UserAdapter;
import com.hhp227.messenger.dto.Message;
import com.hhp227.messenger.dto.User;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private List<String> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
        userList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getSender().equals(firebaseUser.getUid())) {
                        userList.add(message.getReceiver());
                    }
                    if (message.getReceiver().equals(firebaseUser.getUid())) {
                        userList.add(message.getSender());
                    }
                }
                readMessage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return rootView;
    }

    private void readMessage() {
        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (String id : userList)
                        if (user.getId().equals(id))
                            if (users.size() != 0) {
                                for (User user1 : users)
                                    if (!user.getId().equals(user1.getId()))
                                        users.add(user);
                            } else
                                users.add(user);
                }
                userAdapter = new UserAdapter(getContext(), users, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
