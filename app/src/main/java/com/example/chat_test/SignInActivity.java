package com.example.chat_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private String name, email, address;
    private Uri imageURL;
    int flag = 0;
    private SignInButton SignIn;
    private FirebaseDatabase database;

    private GoogleSignInClient mSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SharedPreferences sp = getSharedPreferences("Chat_Test", Context.MODE_PRIVATE);
        if(sp.contains("Log In") && sp.getInt("Log In",0)==1)
            startActivity(new Intent(SignInActivity.this, MainActivity.class));

        SignIn = findViewById(R.id.sign_in_button);

        mFirebaseAuth = FirebaseAuth.getInstance();

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(this, gso);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent in signIn()
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        name = acct.getGivenName();
        email = acct.getEmail();
        imageURL = acct.getPhotoUrl();
        Log.d(TAG, String.valueOf(imageURL));

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // If sign in succeeds the auth state listener will be notified and logic to
                        // handle the signed in user can be handled in the listener.
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String user_id = firebaseUser.getUid();
                        if(imageURL!=null) {
//                            Bitmap bitmap = null;
                            Picasso.get().load(imageURL).into(picassoImageTarget(getApplicationContext(), "Profile", "user.jpg"));
//                                bitmap = MediaStore.Images.Media.getBitmap(SignInActivity.this.getContentResolver() , imageURI);
                            //Toast.makeText(SignInActivity.this, "Got Image", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onSuccess: Got Image");
                            //                            ContextWrapper cw = new ContextWrapper(getApplicationContext());
//                            File directory = cw.getDir("Profile", Context.MODE_PRIVATE);
//                            File file = new File(directory, "user" + ".jpg");
//                            if (!file.exists()) {
//                                Log.d("path", file.toString());
//                                FileOutputStream fos = null;
//                                try {
//                                    fos = new FileOutputStream(file);
//                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                                    fos.flush();
//                                    fos.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                        }

                        SharedPreferences sp = getSharedPreferences("Chat_Test", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("Log In", 1);
                        editor.putString("Name", name);
                        editor.putString("Email", email);
                        editor.putString("User_ID", user_id);
                        editor.commit();

//                        StorageReference ref = FirebaseStorage.getInstance().getReference().child("Images");
//                        StorageReference uploadImageRef = ref.child("user" + imageURL.getLastPathSegment());
//                        UploadTask uploadTask = uploadImageRef.putFile(imageURL);

//                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                flag = 1;
//                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Uri> task) {
//                                        address = task.getResult().toString();
//                                    }
//                                });
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                flag=0;
//                                Log.d(TAG, "onFailure: Could not upload image");
//                            }
//                        });

//                        Users users = new Users(name, email, imageURI, 0);
                        HashMap<String,Object> user =new HashMap<>();
                        user.put("Name",name);
                        user.put("Email", email);
                        user.put("Address", String.valueOf(imageURL));
                        user.put("Friends", 0);
                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference("Users").child(user_id);
//                        myRef.push().setValue(user, new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                                Toast.makeText(SignInActivity.this,"Inserted successfully",Toast.LENGTH_LONG).show();
//                            }
//                        });
                        myRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignInActivity.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential", e);
                        Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SignIn() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }
}