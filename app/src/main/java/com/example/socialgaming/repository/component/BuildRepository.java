package com.example.socialgaming.repository.component;

import android.app.Application;
import android.util.Log;

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
import com.example.socialgaming.repository.callback.AuthenticationCallback;
import com.example.socialgaming.view.HomeActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BuildRepository {

    private Application application;
    private AuthenticationCallback callback;

    private FirebaseFirestore firestore;
    private DatabaseReference database;
    private DocumentReference documentReference;
    private ComponentRepository componentRepository;

    private Map<String, Object> data;
    private Map<String, Object> userData;

    public BuildRepository(Application application, AuthenticationCallback callback, String component) {
        this.application = application;
        this.callback = callback;
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        componentRepository = new ComponentRepository(application, callback, component);
    }

    /**
     * @param build - Build da inserire nel Database
     * @return true - viene aggiunta con successo
     * @return false - se non viene aggiunta
     */
    public boolean writeBuild(Build build) {

        if(exists(build))
            return false;

        Map<String, Object> upload = build.getMap();
        boolean componentsSaved = writeComponents(build);
        if(!componentsSaved)
            return false;

        final boolean[] success = new boolean[1];

        firestore.collection("Builds").add(upload)
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

    public Build readBuild(Build build) {
        return readBuild(build.getUuid());
    }

    /**
     * @param uuid - UUID della build
     * @return build - se esiste nel database
     * @return null - se non trova la build con l'uuid specificato
     */
    public Build readBuild(UUID uuid) {

        documentReference = firestore.collection("builds").document(uuid.toString());
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
                Log.e(HomeActivity.class.getSimpleName(), "Error trying to read data!");
            }
        });

        if(data == null)
            return null;

        JSONObject o = new JSONObject(data);
        try {
            Motherboard board = (Motherboard) componentRepository.readData(ComponentType.MOTHERBOARD, o.getString("motherboard"));
            CPU cpu = (CPU) componentRepository.readData(ComponentType.CPU, o.getString("cpu"));
            List<RAM> rams = (List<RAM>) data.get("ram");
            List<Memory> harddisks = (List<Memory>) data.get("harddisk");
            GPU gpu = (GPU) componentRepository.readData(ComponentType.GPU, o.getString("gpu"));
            Case house = (Case) componentRepository.readData(ComponentType.CASE, o.getString("case"));
            CPUFan fan = (CPUFan) componentRepository.readData(ComponentType.CPU_FAN, o.getString("fan"));
            PSU psu = (PSU) componentRepository.readData(ComponentType.PSU, o.getString("psu"));
            User creator = getUser(o.getString("username"));
            Set<String> like = (Set<String>) data.get("like");
            Set<String> dislike = (Set<String>) data.get("dislike");
            Build build = new Build(board, cpu, rams, harddisks, gpu, house, fan, psu, creator, like, dislike, uuid);

            return build;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param username - Username del Creatore della Build
     * @return user - istanza di User che contiene tutti i dati dell'utente
     * @throws JSONException
     */
    public User getUser(String username) throws JSONException {

        documentReference = firestore.collection("users").document(username);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userData = document.getData();
                } else {
                    userData = null;
                }
            } else {
                userData = null;
                Log.e(HomeActivity.class.getSimpleName(), "Error trying to read data!");
            }
        });

        if(userData == null)
            return null;

        JSONObject o = new JSONObject(userData);

        return new User(o.getString("mail"),
                o.getString("password"),
                o.getString("username"),
                (List<Build>) userData.get("favorite"),
                (List<Build>) userData.get("created")
        );
    }

    /**
     * @param build
     * @return true - se la build esiste (e quindi viene aggiornata)
     * @return false - se non esiste la build con stesso uuid
     */
    public boolean updateData(Build build) {
        if(!exists(build))
            return false;

        Map<String, Object> upload = build.getMap();
        Map<String, Object> data = new HashMap<>();
        data.put("/builds/" + build.getUuid().toString(), upload);
        database.updateChildren(data);

        return true;
    }

    /**
     * @param build
     * @return true - se esiste una build nel database con lo stesso UUID
     * @return false - se non trova una build con stesso uuid nel database
     */
    public boolean exists(Build build) {
        if(readBuild(build) == null)
            return false;
        return true;
    }

    //CARICA NEL DATABASE DI TUTTI GLI ELEMENTI DELLA BUILD SE ESSI NON ESISTONO
    private boolean writeComponents(Build build) {
        boolean success = true;
        if(!componentRepository.exists(ComponentType.MOTHERBOARD, build.getBoard().getId()))
            success = success && componentRepository.writeData(ComponentType.MOTHERBOARD, build.getBoard());
        if(!componentRepository.exists(ComponentType.CPU, build.getCpu().getId()))
            success = success && componentRepository.writeData(ComponentType.CPU, build.getCpu());
        if(!componentRepository.exists(ComponentType.CPU_FAN, build.getFan().getId()))
            success = success && componentRepository.writeData(ComponentType.CPU_FAN, build.getFan());
        if(!componentRepository.exists(ComponentType.GPU, build.getGpu().getId()))
            success = success && componentRepository.writeData(ComponentType.GPU, build.getGpu());
        if(!componentRepository.exists(ComponentType.PSU, build.getPsu().getId()))
            success = success && componentRepository.writeData(ComponentType.PSU, build.getPsu());
        if(!componentRepository.exists(ComponentType.CASE, build.getHouse().getId()))
            success = success && componentRepository.writeData(ComponentType.MOTHERBOARD, build.getHouse());

        for(RAM r : build.getRams())
            if(!componentRepository.exists(ComponentType.RAM, r.getId()))
                success = success && componentRepository.writeData(ComponentType.RAM, r);

        for(Memory m : build.getHarddisks())
            if(!componentRepository.exists(ComponentType.MEMORY, m.getId()))
                success = success && componentRepository.writeData(ComponentType.MEMORY, m);


        return success;
    }

}
