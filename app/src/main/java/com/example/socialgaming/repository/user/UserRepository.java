package com.example.socialgaming.repository.user;

import android.content.Context;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
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
        documentReference = firestore.collection("/" + USERS_COLLECTION).document("/" + username);

        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> callback.onUserReceived(documentSnapshot));

    }


    //AGGIORNA LA PASSWORD SSE LA VECCHIA PASSWORD E oldPass COINCIDONO
    public void updatePassword(User user, String newPass, Context context, AuthRepository authRepository) {

        DocumentReference document = firestore.collection("/" + USERS_COLLECTION).document("/" + user.getUsername());
        document.update("password", newPass)
                .addOnSuccessListener(aVoid -> {
                    authRepository.updatePassword(user, newPass, context);
                })
                .addOnFailureListener(aVoid -> Toast.makeText(context, "Error trying to update password", Toast.LENGTH_SHORT));

    }

    public void updateUserBuilds(User user, Context context, String msg) {

        DocumentReference document = firestore.collection("/" + USERS_COLLECTION).document("/" + user.getUsername());
        document.update("created", user.getCreated())
                .addOnSuccessListener(aVoid -> Toast.makeText(context, msg, Toast.LENGTH_SHORT))
                .addOnFailureListener(aVoid -> Toast.makeText(context, "Error trying to update build", Toast.LENGTH_SHORT));

    }

    public void updateUserBuilds(User user) {

        DocumentReference document = firestore.collection("/" + USERS_COLLECTION).document("/" + user.getUsername());
        document.update("created", user.getCreated());

    }

    public void updateUserFavorite(User user, BuildFirestore build, Context context, String msg, boolean add) {
        DocumentReference document = firestore.collection("/" + USERS_COLLECTION).document("/" + user.getUsername());
        if (add == true)
            document.update("favorite", FieldValue.arrayUnion(build.getUuid().toString()))
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, msg, Toast.LENGTH_SHORT))
                    .addOnFailureListener(aVoid -> Toast.makeText(context, "Error trying to update build", Toast.LENGTH_SHORT));
        else
            document.update("favorite", FieldValue.arrayRemove(build.getUuid().toString()))
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, msg, Toast.LENGTH_SHORT))
                    .addOnFailureListener(aVoid -> Toast.makeText(context, "Error trying to update build", Toast.LENGTH_SHORT));
    }

    public void removeBuildsUpdate(BuildFirestore build, User user, Context context, String msg) {
        DocumentReference docRef = firestore.collection("/" + USERS_COLLECTION).document("/" + user.getUsername());
        docRef.update("created", FieldValue.arrayRemove(build.getUuid().toString()))
                .addOnSuccessListener(unused -> Toast.makeText(context, msg, Toast.LENGTH_SHORT));

        firestore.collection("/" + USERS_COLLECTION)
                .whereArrayContains("favorite", build.getUuid().toString())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot doc : docs)
                        if (doc.exists())
                            doc.getReference().update("favorite", FieldValue.arrayRemove(build.getUuid().toString()));

                });
    }


    public void uploadBitmapToFirebaseStorage(Bitmap bitmap, final String imageName) {
        // Get the reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // Create a reference to the file to be uploaded with the given name
        StorageReference bitmapRef = storageRef.child("users/" + imageName + ".png");
        UploadTask uploadTask = bitmapRef.putBytes(ImageUtils.encodeBitmapToByteArray(ImageUtils.resize(bitmap)));
    }

    public void downloadBitmapFromFirebaseStorage(String imageName, IUserCallback callback) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("users/").child(imageName + ".png");

        storageReference.getDownloadUrl().addOnSuccessListener(result -> {
            callback.onImageReceived(ImageUtils.getBitmapFromURL(result.toString()));
        });

    }

}
