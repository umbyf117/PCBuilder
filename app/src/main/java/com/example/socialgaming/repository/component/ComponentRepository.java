package com.example.socialgaming.repository.component;

import android.app.Application;

import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.repository.callback.AuthenticationCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class ComponentRepository {

    private Application application;
    private AuthenticationCallback callback;

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

    public ComponentRepository(Application application, AuthenticationCallback callback, String component) {
        this.application = application;
        this.callback = callback;
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void writeData(ComponentType type, ComponentBase component, boolean force) {
        documentReference = firestore.collection(type.toString().toLowerCase(Locale.ROOT)).document(component.getId());
    }

    public void writeData(ComponentType type, ComponentBase component) {

    }

    public void readData(ComponentType type, ComponentBase component) {

    }
}
