package com.example.chat_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private String name, email, address;
    private int friends;
    private ImageButton send;

    private ImageView profile;
    private TextView user_name;

    private EditText type_message;
    FirebaseAuth firebaseAuth;
    FirebaseUser fUser;
    DatabaseReference reference;

    MessageAdapter messageAdapter;
    ArrayList<Chat> mChat;

    ValueEventListener seenListener;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();
        name = i.getStringExtra("Name");
        email = i.getStringExtra("Email");
        address = i.getStringExtra("Address");
        friends = i.getIntExtra("Friends",0);
//        ActionBar toolbar = getSupportActionBar();
//        toolbar.setTitle(name);
////        toolbar.setLogo(R.drawable.ic_round_person_24);
//        toolbar.setDisplayHomeAsUpEnabled(true);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        profile = findViewById(R.id.profile_image);
        user_name = findViewById(R.id.user_name);
        user_name.setText(name);
        if(address==null)
            profile.setImageResource(R.drawable.ic_round_person_24);
        else
            Picasso.get().load(address).fit().centerInside().placeholder(R.drawable.ic_round_person_24).into(profile);

        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        type_message = findViewById(R.id.text_send);
        send = findViewById(R.id.button_send);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = type_message.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fUser.getEmail(), email, msg);
                }
                else{
                    Toast.makeText(ChatActivity.this, "Type Something", Toast.LENGTH_SHORT).show();
                }
                type_message.setText("");
            }
        });

        readMessage(fUser.getEmail(), email);
        seenMessage(email);
//        reference = FirebaseDatabase.getInstance()

    }

    private void sendMessage(String send_email, String rec_email, String msg) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Sender", send_email);
        hashMap.put("Receiver", rec_email);
        hashMap.put("Message", msg);
        hashMap.put("Seen_Status", false);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void seenMessage(String rec_email){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(fUser.getEmail()) && chat.getSender().equals(rec_email)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Seen_Status", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(String my_email, String rec_email){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    Log.d("my_email", my_email);
                    Log.d("other_email", rec_email);
//                    Log.d("getOtherEmail", chat.getSender());
//                    Log.d("getMyEmail", chat.getReceiver());
                    if(chat.getReceiver().equals(my_email) && chat.getSender().equals(rec_email) ||
                        chat.getReceiver().equals(rec_email) && chat.getSender().equals(my_email)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(mChat, ChatActivity.this);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        switch (item.getItemId()) {
////            // Respond to the action bar's Up/Home button
////            case android.R.id.home:
//////                NavUtils.navigateUpFromSameTask(this);
//////                return true;
////        }
//
//        if (item.getItemId() == android.R.id.home) // Press Back Icon
//        {
//            finish();
////            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }
}