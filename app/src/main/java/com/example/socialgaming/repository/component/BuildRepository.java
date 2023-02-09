package com.example.socialgaming.repository.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.BuildFirestore;
import com.example.socialgaming.data.User;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.user.UserRepository;
import com.example.socialgaming.utils.ImageUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private ComponentRepository componentRepository;

    private Map<String, Object> data;
    private Map<String, Object> userData;
    private List<Build> buildList;

    public BuildRepository() {
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        componentRepository = new ComponentRepository();
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

    public void uploadBitmapToFirebaseStorage(Bitmap bitmap, final String imageName) {
        // Get the reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // Create a reference to the file to be uploaded with the given name
        StorageReference bitmapRef = storageRef.child("images/" + imageName + ".jpeg");
        UploadTask uploadTask = bitmapRef.putBytes(ImageUtils.encodeBitmapToByteArray(bitmap));
    }

    public void downloadBitmapFromFirebaseStorage(String imageName, BuildFirestore build, IBuildCallback callback) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("images/").child(imageName + ".jpeg");

        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE * 10).addOnSuccessListener(bytes -> {
            callback.onImageReceived(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), build);
        });

    }

    /**
     * @param uuid - UUID della build
     * @return build - se esiste nel database
     * @return null - se non trova la build con l'uuid specificato
     */
    public void getBuild(UUID uuid, IBuildCallback callback) {

        documentReference = firestore.collection("/" + BUILD_COLLECTION).document("/" + uuid.toString());
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> callback.onBuildReceived(documentSnapshot));

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
                    documentReference = firestore.collection(BUILD_COLLECTION).document("/" + uuid.toString());
                    documentReference.get()
                            .addOnSuccessListener(documentSnapshot -> documents.add(documentSnapshot));
                }

                if(documents.size() != 0)
                    callback.onBuildsReceived(documents, created);

            }
        }).run();
    }

    public void deleteBuild(BuildFirestore b) {
        firestore.collection("/" + BUILD_COLLECTION).document("/" + b.getUuid()).delete();
    }

}
