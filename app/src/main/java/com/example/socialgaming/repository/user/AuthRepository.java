package com.example.socialgaming.repository.user;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.socialgaming.PcBuilder;
import com.example.socialgaming.R;
import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.User;
import com.example.socialgaming.utils.ImageUtils;
import com.example.socialgaming.utils.ViewUtils;
import com.example.socialgaming.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AuthRepository {

    private static volatile AuthRepository INSTANCE = null;
    private Application application;

    private FirebaseAuth firebaseAuth;
    private static MutableLiveData<FirebaseUser> userLiveData; // Not null if user logged
    private static MutableLiveData<Boolean> loggedOutLiveData; // True if user not logged

    public AuthRepository(Application application){
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) { // check if already logged and set mutables
            userLiveData.setValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.setValue(false);
        } else { loggedOutLiveData.setValue(true); }
    }

    public static AuthRepository getInstance(Application application){
        if(AuthRepository.INSTANCE == null){
            synchronized (AuthRepository.class){
                if(AuthRepository.INSTANCE == null)
                    AuthRepository.INSTANCE = new AuthRepository(application);
            }
        }
        return AuthRepository.INSTANCE;
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if(task.isSuccessful()) { // Set mutable
                        userLiveData.setValue(firebaseAuth.getCurrentUser());
                        loggedOutLiveData.setValue(false);
                    }
                }).addOnFailureListener(e -> ViewUtils.displayToast(application,e.getMessage()));
    }

    public void register(String email, String password, String username) {
        FirebaseFirestore.getInstance().collection(application.getString(R.string.firestore_users_collection))
                .document(username).get().addOnCompleteListener(taskFS -> {
                    if(taskFS.isSuccessful()){
                        if(taskFS.getResult().exists())
                            Log.i(MainActivity.TAG, application.getString(R.string.authentication_input_username_exist));
                        else {

                            FirebaseFirestore.getInstance().collection("/" + application.getString(R.string.firestore_users_collection))
                                    .document("/" + username).set(new HashMap<String, Object>() {
                                        {
                                            put("mail", email);
                                            put("password", password);
                                            put("username", username);
                                            put("favorite", new ArrayList<Build>());
                                            put("created", new ArrayList<Build>());
                                        }
                                    }).addOnCompleteListener(task -> {
                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference bitmapRef = storageRef.child("users/" + username + ".png");
                                        Bitmap profile = BitmapFactory.decodeFile("android.resource://com.example.socialgaming/" + R.drawable.logo);
                                        UploadTask uploadTask = bitmapRef.putBytes(ImageUtils.encodeBitmapToByteArray(ImageUtils.resize(profile)));
                                        uploadTask.addOnCompleteListener(task1 -> firebaseAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(application.getMainExecutor(), taskFA -> {
                                                    if (taskFA.isSuccessful()) {
                                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                .setDisplayName(username)
                                                                .build();
                                                    }
                                                }));
                                    });
                            /*
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(application.getMainExecutor(), taskFA -> {
                                        if (taskFA.isSuccessful()) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(username)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            Log.d("[UPDATE]", "User profile updated.");
                                                        }
                                                    });
                                            userLiveData.setValue(user); // Set mutable
                                            loggedOutLiveData.setValue(false);
                                            FirebaseFirestore.getInstance().collection("/" + application.getString(R.string.firestore_users_collection))
                                                    .document("/" + username).set(new HashMap<String, Object>() {
                                                        {
                                                            put("mail", email);
                                                            put("password", password);
                                                            put("username", username);
                                                            put("favorite", new ArrayList<Build>());
                                                            put("created", new ArrayList<Build>());
                                                        }
                                                    }).addOnCompleteListener(task -> {
                                                        Objects.requireNonNull(userLiveData.getValue());

                                                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build());
                                                    })
                                                    .addOnFailureListener(e -> ViewUtils.displayToast(application,e.getMessage()));

                                            }
                                    }).addOnFailureListener(e -> ViewUtils.displayToast(application,e.getMessage()));*/
                        }
                    }
                }).addOnFailureListener(e -> ViewUtils.displayToast(application , e.getMessage()));
    }

    public void updatePassword(User user, String password, Context context) {
        userLiveData.getValue().updatePassword(password)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Password successfully updated!", Toast.LENGTH_SHORT);
                    user.setPassword(password);
                })
                .addOnFailureListener(aVoid -> Toast.makeText(context, "Error trying to update password", Toast.LENGTH_SHORT));
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.setValue(true);
        userLiveData.setValue(null);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public MutableLiveData<Boolean> getLoggedOutLiveData() { return loggedOutLiveData; }

}
