package com.example.socialgaming.repository.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.callbacks.ISearchCallback;
import com.example.socialgaming.repository.user.UserRepository;
import com.example.socialgaming.utils.ImageUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BuildRepository {

    public static final String BUILD_COLLECTION = "Builds";
    public static final String RAM_COLLECTION = "Rams";
    public static final String MEMORY_COLLECTION = "Memory";

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

    private Map<String, Object> data;
    private Map<String, Object> userData;
    private List<Build> buildList;

    public BuildRepository() {
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * @param build - Build da inserire nel Database
     * @return true - viene aggiunta con successo
     * @return false - se non viene aggiunta
     */
    public boolean setBuild(BuildFirestore build, User user, UserRepository repo) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, Object> map = build.getAttributeMap();


                firestore.collection("/" + BUILD_COLLECTION).document(build.getUuid().toString())
                        .set(map)
                        .addOnSuccessListener(documentReference -> {
                            Log.d("TAG", "Document written with ID: " + build.getUuid());
                        })
                        .addOnFailureListener(e -> {
                            Log.w("TAG", "Error adding document", e);
                        });

                uploadBitmapToFirebaseStorage(build.getImage(), build.getUuid().toString());
                repo.updateUserBuilds(user);

            }
        }).run();

        return true;
    }

    public void getBuild(Build build, IBuildCallback callback) {
        getBuild(build.getUuid(), callback);
    }

    /**
     * @param uuid - UUID della build
     * @return build - se esiste nel database
     * @return null - se non trova la build con l'uuid specificato
     */
    public void getBuild(UUID uuid, IBuildCallback callback) {

        documentReference = firestore.collection("/" + BUILD_COLLECTION).document("/" + uuid.toString());
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> callback.onBuildReceived(documentSnapshot, false));

    }

    public void getBuildList(int limit, int offset, IBuildCallback callback) {

        firestore.collection("/" + BUILD_COLLECTION)
                .limit(limit + offset)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->
                        callback.onBuildsReceived(queryDocumentSnapshots.getDocuments(), false));


    }

    public void getUserBuilds(List<String> uuids, IBuildCallback callback, boolean created) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<DocumentSnapshot> documents = new ArrayList<>();

                for(String uuid : uuids) {
                    documentReference = firestore.collection("/" + BUILD_COLLECTION).document("/" + uuid);
                    documentReference.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                documents.add(documentSnapshot);
                                callback.onBuildReceived(documentSnapshot, created);
                            });
                }
            }
        }).run();
    }

    public void uploadBitmapToFirebaseStorage(Bitmap bitmap, final String imageName) {
        // Get the reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // Create a reference to the file to be uploaded with the given name
        StorageReference bitmapRef = storageRef.child("images/" + imageName + ".jpeg");
        UploadTask uploadTask = bitmapRef.putBytes(ImageUtils.encodeBitmapToByteArray(ImageUtils.resize(bitmap)));
    }

    public void downloadBitmapFromFirebaseStorage(String imageName, BuildFirestore build, IBuildCallback callback, boolean created) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("images/").child(imageName + ".jpeg");

        storageReference.getDownloadUrl().addOnSuccessListener(result -> {
            callback.onImageReceived(ImageUtils.getBitmapFromURL(result.toString()), build, created);
        });

    }

    public void downloadBitmapFromFirebaseStorage(String imageName, BuildFirestore build, ISearchCallback callback) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("images/").child(imageName + ".jpeg");

        storageReference.getDownloadUrl().addOnSuccessListener(result -> {
            callback.onImageReceived(ImageUtils.getBitmapFromURL(result.toString()), build);
        });

    }

    public void deleteBuild(BuildFirestore b) {
        firestore.collection("/" + BUILD_COLLECTION).document("/" + b.getUuid()).delete();
    }

    public void searchBuilds(String name, String creator, ISearchCallback callback){
        if(name.length()<=0){
            firestore.collection("/" + BUILD_COLLECTION)
                    .whereEqualTo("creator", creator)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                            callback.onSearch(queryDocumentSnapshots.getDocuments()));
        }
        else if(creator.length()<=0){
            firestore.collection("/" + BUILD_COLLECTION)
                    .whereEqualTo("name", name)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                            callback.onSearch(queryDocumentSnapshots.getDocuments()));
        }
        else { firestore.collection("/" + BUILD_COLLECTION)
                .whereEqualTo("creator", creator)
                .whereEqualTo("name", name)
                .get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                            callback.onSearch(queryDocumentSnapshots.getDocuments()));}

    }

    public void updateLikes(User user, BuildFirestore build, boolean addLike, boolean addDislike, boolean removeLike, boolean removeDislike) {

        DocumentReference docRef = firestore.collection("/" + BUILD_COLLECTION).document("/" + build.getUuid().toString());
        if(addLike) {
            docRef.update("like", FieldValue.arrayUnion(user.getUsername()));
        }
        else if(addDislike) {
            docRef.update("dislike", FieldValue.arrayUnion(user.getUsername()));
        }

        if(removeLike)
            docRef.update("like", FieldValue.arrayRemove(user.getUsername()));

        if(removeDislike)
            docRef.update("dislike", FieldValue.arrayRemove(user.getUsername()));

    }
}
