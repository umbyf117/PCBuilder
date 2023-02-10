package com.example.socialgaming.repository.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.callbacks.IUserCallback;
import com.example.socialgaming.repository.component.BuildRepository;
import com.example.socialgaming.utils.ImageUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    public static final String USERS_COLLECTION = "users";

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

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

    public void deleteBuild(Build b) {
        firestore.collection("/" + BuildRepository.BUILD_COLLECTION).document("/" + b.getUuid()).delete();
    }

    public boolean updateImage(User user) {

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(user.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("image", ImageUtils.encodeBitmapToByteArray(user.getImage()));

        userRef.update(data);

        return true;
        /*documentReference = firestore.collection(USERS_COLLECTION).document(user.getUsername());

        Map<String, Object> dataUpdated = new HashMap<>();
        dataUpdated.put("image", image);
        Map<String, Object> upload = new HashMap<>();
        upload.put("/users/" + user.getUsername(), dataUpdated);

        database.updateChildren(upload);

        return true;*/
    }

    public void updateUserBuilds(User user) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                documentReference = firestore.collection(USERS_COLLECTION).document(user.getUsername());
                documentReference.update(user.getMap());

                Log.i("[CREATION]", "User creation updated");

            }
        }).run();

    }

    public void uploadBitmapToFirebaseStorage(Bitmap bitmap, final String imageName) {
        // Get the reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // Create a reference to the file to be uploaded with the given name
        StorageReference bitmapRef = storageRef.child("users/" + imageName + ".png");
        UploadTask uploadTask = bitmapRef.putBytes(ImageUtils.encodeBitmapToByteArray(bitmap));
    }

    public void downloadBitmapFromFirebaseStorage(String imageName, IUserCallback callback) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("users/").child(imageName + ".png");

        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE * 5).addOnSuccessListener(bytes -> {
            callback.onImageReceived(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        });

    }

}
