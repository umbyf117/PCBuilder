package com.example.socialgaming.repository.component;

import android.app.Application;
import android.util.Log;

import com.example.socialgaming.data.ComponentBase;
import com.example.socialgaming.data.types.ComponentType;
import com.example.socialgaming.view.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ComponentRepository {

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;

    private Map<String, Object> data;

    public ComponentRepository() {
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * @param type - Tipo di componente
     * @param component - La componente da salvare nel database
     * @return true - se la componente viene aggiunta
     * @return false - se la componente non viene aggiunta (sia se esiste giá, sia per errori interni)
     */
    public boolean setData(ComponentType type, ComponentBase component) {
        documentReference = firestore.collection("/" + type.toCapitalCase())
                .document("/" + type.toCapitalCase() + "/" + component.getId());
        if(exists(type, component.getId()))
            return false;

        Map<String, Object> upload = component.getMap();
        final boolean[] success = new boolean[1];

        firestore.collection("/" + type.toCapitalCase()).add(upload)
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

    /**
     * @param type - Tipo di componente
     * @param idComponent - ID del componente da cercare
     * @return component - se esiste il componente
     * @return null- se non esiste il componenete
     */
    public ComponentBase getData(ComponentType type, String idComponent) {

        documentReference = firestore.collection("/" + type.toCapitalCase())
                .document("/" + type.toCapitalCase() + "/" + idComponent);
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

        ComponentBase componentBase = ComponentBase.construct(type, data);

        return componentBase;
    }

    /**
     * @param type - Tipo di componente
     * @param component - La componente da salvare nel database
     * @return true - se il component esiste (e quindi viene aggiornato)
     * @return false - se non esiste il component con stesso id
     */
    public boolean updateData(ComponentType type, ComponentBase component) {
        if(!exists(type, component.getId()))
            return false;

        Map<String, Object> dataUpdated = component.getMap();
        Map<String, Object> upload = new HashMap<>();
        upload.put("/" + type.toCapitalCase() + "/" + component.getId(), dataUpdated);
        database.updateChildren(upload);

        return true;
    }

    /**
     * @param type - Tipo di componente
     * @param idComponent - ID del componente da cercare
     * @return true - se esiste il component nel database con lo stesso ID
     * @return false - se non trova un component con stesso ID nel database
     */
    public boolean exists(ComponentType type, String idComponent) {
        getData(type, idComponent);
        if(data == null)
            return false;
        return true;
    }
}
