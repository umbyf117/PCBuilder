package com.example.socialgaming.repository.user;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.socialgaming.R;
import com.example.socialgaming.repository.callback.AuthenticationCallback;
import com.example.socialgaming.utils.ViewUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AuthRepository {

    private static volatile AuthRepository INSTANCE = null;
    private Application application;
    private AuthenticationCallback callback;

    private FirebaseAuth firebaseAuth;
    private static MutableLiveData<FirebaseUser> userLiveData; // Not null if user logged
    private static MutableLiveData<Boolean> loggedOutLiveData; // True if user not logged

    public AuthRepository(Application application, AuthenticationCallback callback){
        this.application = application;
        this.callback = callback;
        this.firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) { // check if already logged and set mutables
            userLiveData.setValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.setValue(false);
        } else { loggedOutLiveData.setValue(true); }
    }

    public static AuthRepository getInstance(Application application, AuthenticationCallback callback){
        if(AuthRepository.INSTANCE == null){
            synchronized (AuthRepository.class){
                if(AuthRepository.INSTANCE == null)
                    AuthRepository.INSTANCE = new AuthRepository(application, callback);
            }
        }
        return AuthRepository.INSTANCE;
    }

    public static AuthRepository getInstance(Application application){
        return getInstance(application, null);
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
                            callback.showAuthError(application.getString(R.string.authentication_input_username_exist));
                        else {
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(application.getMainExecutor(), taskFA -> {
                                        if (taskFA.isSuccessful()) {
                                            userLiveData.setValue(firebaseAuth.getCurrentUser()); // Set mutable
                                            loggedOutLiveData.setValue(false);
                                            FirebaseFirestore.getInstance().collection(application.getString(R.string.firestore_users_collection))
                                                    .document(username).set(new HashMap<String, Object>() {
                                                        {
                                                            put("uId", firebaseAuth.getCurrentUser().getUid());
                                                        }
                                                    }).addOnCompleteListener(task -> Objects.requireNonNull(userLiveData.getValue())
                                                            .updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build()))
                                                    .addOnFailureListener(e -> ViewUtils.displayToast(application,e.getMessage()));

                                            /*
                                            //Inserisce i dati passati nel Firebase Realtime Database
                                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(email);

                                            */

                                        }
                                    }).addOnFailureListener(e -> ViewUtils.displayToast(application,e.getMessage()));
                        }
                    }
                }).addOnFailureListener(e -> ViewUtils.displayToast(application , e.getMessage()));
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.setValue(true);
        userLiveData.setValue(null);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() { return userLiveData; }

    public MutableLiveData<Boolean> getLoggedOutLiveData() { return loggedOutLiveData; }

}
