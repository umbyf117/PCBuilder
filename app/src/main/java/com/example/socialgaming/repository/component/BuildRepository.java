package com.example.socialgaming.repository.component;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialgaming.data.Build;
import com.example.socialgaming.data.CPU;
import com.example.socialgaming.data.CPUFan;
import com.example.socialgaming.data.Case;
import com.example.socialgaming.data.GPU;
import com.example.socialgaming.data.Memory;
import com.example.socialgaming.data.Motherboard;
import com.example.socialgaming.data.PSU;
import com.example.socialgaming.data.RAM;
import com.example.socialgaming.data.User;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callbacks.IBuildCallback;
import com.example.socialgaming.repository.user.UserRepository;
import com.example.socialgaming.view.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BuildRepository {

    public static final String BUILD_COLLECTION = "Builds";

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
    public boolean setBuild(Build build) {

        Map<String, Object> upload = build.getMap();

        firestore.collection("/" + BUILD_COLLECTION).add(upload)
                .addOnSuccessListener(documentReference -> {
                    Log.d("TAG", "Document written with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error adding document", e);
                });

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

        documentReference = firestore.collection(BUILD_COLLECTION).document("/" + uuid.toString());
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> callback.onBuildReceived(documentSnapshot));

    }

    public List<Build> getBuildList(int limit, int offset, IBuildCallback callback) {

        firestore.collection("/" + BUILD_COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit + offset)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->
                        callback.onBuildsReceived(queryDocumentSnapshots.getDocuments()));

        if(buildList == null)
            return null;

        if(buildList.size() < offset)
            return null;

        return buildList.subList(offset, buildList.size());
    }

    public void deleteBuild(Build b) {
        firestore.collection("/" + BUILD_COLLECTION).document("/" + b.getUuid()).delete();
    }

}
