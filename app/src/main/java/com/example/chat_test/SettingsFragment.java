package com.example.chat_test;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;

public class SettingsFragment extends PreferenceFragmentCompat {

    Preference Profile, LogOut;
    Bitmap bitmap;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Profile = findPreference("my_profile");
        LogOut = findPreference("log_out");

        SharedPreferences sp = getContext().getSharedPreferences("Chat_Test",Context.MODE_PRIVATE);
        Profile.setTitle(sp.getString("Name", ""));

        Profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                startActivity(i);
                return false;
            }
        });
        LogOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sp = getContext().getSharedPreferences("Chat_Test", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("Log In", 0);
                editor.commit();
                startActivity(new Intent(getContext(), SignInActivity.class));
                getActivity().finish();
                return false;
            }
        });
    }
}