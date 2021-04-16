package com.example.chat_test;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link UserListFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class UserListFragment extends Fragment {

//    private ArrayList<Users> mFriendList;
    private ArrayList<Users> mUserList;
    private String email, name, address;
    private int friends;

    private RecyclerView mRecyclerView;
    private UsersAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
//    private Users user;
//    private final String TAG = "MainActivity";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public UserListFragment() {
//        // Required empty public constructor
//    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment UserListFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static UserListFragment newInstance(String param1, String param2) {
//        UserListFragment fragment = new UserListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
////        }
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mUserList = new ArrayList<>();
        mAdapter = new UsersAdapter(mUserList,getContext());
        mRecyclerView.setAdapter(mAdapter);

        readUsers();


//        mFriendList = new ArrayList<>();
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 1"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 3"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 5"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 7"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 9"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 11"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 13"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 15"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 17"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 19"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 21"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 23"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 25"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 27"));
//        mFriendList.add(new Users(R.drawable.ic_round_person_24, "Line 29"));

//        mDatabase.child("Users").child().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//
//                    mUserList.add();
//                    mRecyclerView.setHasFixedSize(true);
//                    mLayoutManager = new LinearLayoutManager(getContext());
//                    mAdapter = new UsersAdapter(mUserList,getContext());
//                    mRecyclerView.setLayoutManager(mLayoutManager);
//                    mRecyclerView.setAdapter(mAdapter);
//                }
//            }
//        });



//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getContext());
//        mAdapter = new UsersAdapter(mFriendList,getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void readUsers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUserList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    assert user != null;
                    if (!user.getEmail().equals(firebaseUser.getEmail()))
                        mUserList.add(user);

                }
                mAdapter = new UsersAdapter(mUserList,getContext());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
                    @Override
                    public void onItemCLick(int position) {
                        Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                        name = mUserList.get(position).getName();
                        email = mUserList.get(position).getEmail();
                        address = mUserList.get(position).getAddress();
                        friends = mUserList.get(position).getFriends();
                        Intent i = new Intent(getContext(), ChatActivity.class);
                        i.putExtra("Name", name);
                        i.putExtra("Email", email);
                        i.putExtra("Address", address);
                        i.putExtra("Friends", friends);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Could Not Load Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}