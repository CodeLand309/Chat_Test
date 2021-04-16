package com.example.chat_test;

import android.net.Uri;

public class Users {
    private String Name, Email, Address;
    private int Friends;

//    Uri Profile;


//    public Users(int profile, String name) {
//        Name = name;
////        Message = message;
//        Profile = profile;
//    }

    public Users(){
    }

    public Users(String name, String email, String address, int friends) {
        Name = name;
        Email = email;
//        Profile = profile;
        Address = address;
        Friends = friends;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public int getFriends() {
        return Friends;
    }

    public String getAddress() {
        return Address;
    }

//    public Uri getProfile() {
//        return Profile;
//    }
}
