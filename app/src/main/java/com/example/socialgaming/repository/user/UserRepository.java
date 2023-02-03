package com.example.socialgaming.repository.user;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callback.AuthenticationCallback;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private static final String USERS_COLLECTION = "users";

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

    private Map<String, Object> data;

    public UserRepository() {
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }


    //Ottenimento dati dell'utente
    public User getUserData(String username) {
        documentReference = firestore.collection(USERS_COLLECTION).document(username);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    data = document.getData();
                } else {
                    data = null;
                }
            } else {
                data = null;
                Log.e(MainActivity.class.getSimpleName(), "Error trying to read data!");
            }
        });

        if(data == null)
            return null;

        User user = new User((String) data.get("mail"),
                (String) data.get("password"),
                username,
                (List<Build>) data.get("favorite"),
                (List<Build>) data.get("created"),
                (Uri) data.get("image"));

        return user;
    }

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
