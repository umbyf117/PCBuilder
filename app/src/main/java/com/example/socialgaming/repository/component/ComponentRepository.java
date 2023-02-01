package com.example.socialgaming.repository.component;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callback.AuthenticationCallback;
import com.example.socialgaming.view.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ComponentRepository {

    private Application application;
    private AuthenticationCallback callback;

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

    private Map<String, Object> data;

    public ComponentRepository(Application application, AuthenticationCallback callback, String component) {
        this.application = application;
        this.callback = callback;
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public boolean writeData(ComponentType type, ComponentBase component, boolean force) {
        documentReference = firestore.collection(type.toCapitalCase()).document(component.getId());
        if(exists(type, component.getId()) && force == false)
            return false;

        Map<String, Object> upload = component.getMap();
        final boolean[] success = new boolean[1];

        firestore.collection(type.toCapitalCase()).add(upload)
                .addOnSuccessListener(documentReference -> {
                    Log.d("TAG", "Document written with ID: " + documentReference.getId());
                    success[0] = true;
                })
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error adding document", e);
                    success[0] = false;
                });

        return success[0];

    }

    public boolean writeData(ComponentType type, ComponentBase component) {
        return writeData(type, component, false);
    }

    public ComponentBase readData(ComponentType type, String idComponent) {

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        data = document.getData();
                    } else {
                        data = null;
                    }
                } else {
                    data = null;
                    Log.e(HomeActivity.class.getSimpleName(), "Error trying to read data!");
                }
            }
        });

        if(data == null)
            return null;

        ComponentBase componentBase = ComponentBase.construct(type, data);

        return componentBase;
    }

    public boolean exists(ComponentType type, String idComponent) {
        readData(type, idComponent);
        if(data == null)
            return false;
        return true;
    }
}
