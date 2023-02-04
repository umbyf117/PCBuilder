package com.example.socialgaming.repository.user;

import android.net.Uri;

import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    public static final String USERS_COLLECTION = "users";

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

    private Map<String, Object> data;

    public UserRepository() {
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }


    //Ottenimento dati dell'utente
    public void getUserData(String username, IUserCallback callback) {
        documentReference = firestore.collection(USERS_COLLECTION).document("/" + username);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> callback.onUserReceived(documentSnapshot));

    }
    /*
    public User getUserData(String username) {
        final User[] user = {null};
        final CountDownLatch latch = new CountDownLatch(1);

        documentReference = firestore.collection(USERS_COLLECTION).document("/" + username);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    user[0] = new User((String) data.get("mail"),
                            (String) data.get("password"),
                            username,
                            (List<Build>) data.get("favorite"),
                            (List<Build>) data.get("created"),
                            (Uri) data.get("image"));
                } else {
                    Log.d(MainActivity.class.getSimpleName(), "No such document");
                }
            } else {
                Log.e(MainActivity.class.getSimpleName(), "Error trying to read data!", task.getException());
            }
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.e(MainActivity.class.getSimpleName(), "Error waiting for data", e);
        }

        return user[0];
    }*/


    //AGGIORNA LA PASSWORD SSE LA VECCHIA PASSWORD E oldPass COINCIDONO
    public boolean updatePassword(User user, String oldPass, String newPass) {
        documentReference = firestore.collection(USERS_COLLECTION).document(user.getUsername());

        if(user.changePassword(oldPass, newPass) != 0)
            return false;

        Map<String, Object> dataUpdated = new HashMap<>();
        dataUpdated.put("password", user.getPassword());
        Map<String, Object> upload = new HashMap<>();
        upload.put("/users/" + user.getUsername(), dataUpdated);

        database.updateChildren(upload);

        return true;

    }

    public boolean updateImage(User user, Uri image) {
        documentReference = firestore.collection(USERS_COLLECTION).document(user.getUsername());

        Map<String, Object> dataUpdated = new HashMap<>();
        dataUpdated.put("image", image);
        Map<String, Object> upload = new HashMap<>();
        upload.put("/users/" + user.getUsername(), dataUpdated);

        database.updateChildren(upload);

        return true;
    }

}
