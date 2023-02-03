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
import com.example.socialgaming.repository.user.UserRepository;
import com.example.socialgaming.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    private static final String BUILD_COLLECTION = "Builds";

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

        if(exists(build))
            return false;

        Map<String, Object> upload = build.getMap();
        boolean componentsSaved = writeComponents(build);
        if(!componentsSaved)
            return false;

        final boolean[] success = new boolean[1];

        firestore.collection("/" + BUILD_COLLECTION).add(upload)
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

    public Build getBuild(Build build) {
        return getBuild(build.getUuid());
    }

    /**
     * @param uuid - UUID della build
     * @return build - se esiste nel database
     * @return null - se non trova la build con l'uuid specificato
     */
    public Build getBuild(UUID uuid) {

        documentReference = firestore.collection("/" + BUILD_COLLECTION)
                .document("/" + BUILD_COLLECTION + "/" + uuid.toString());
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

        JSONObject o = new JSONObject(data);
        try {
            Motherboard board = (Motherboard) componentRepository.getData(ComponentType.MOTHERBOARD, o.getString("motherboard"));
            CPU cpu = (CPU) componentRepository.getData(ComponentType.CPU, o.getString("cpu"));
            List<RAM> rams = (List<RAM>) data.get("ram");
            List<Memory> harddisks = (List<Memory>) data.get("harddisk");
            GPU gpu = (GPU) componentRepository.getData(ComponentType.GPU, o.getString("gpu"));
            Case house = (Case) componentRepository.getData(ComponentType.CASE, o.getString("case"));
            CPUFan fan = (CPUFan) componentRepository.getData(ComponentType.CPU_FAN, o.getString("fan"));
            PSU psu = (PSU) componentRepository.getData(ComponentType.PSU, o.getString("psu"));
            User creator = getUser(o.getString("username"));
            Set<String> like = (Set<String>) data.get("like");
            Set<String> dislike = (Set<String>) data.get("dislike");
            String name = o.getString("name");
            Uri image = (Uri) o.get("image");
            Build build = new Build(board, cpu, rams, harddisks, gpu, house, fan, psu, creator, like, dislike, uuid, name, image);

            return build;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Build> getBuildList(int limit, int offset) {
        firestore.collection("/" + BUILD_COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit + offset)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        buildList = new ArrayList<Build>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Build build = document.toObject(Build.class);
                            buildList.add(build);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        if(buildList.size() < offset)
            return null;

        return buildList.subList(offset, buildList.size());
    }

    /**
     * @param username - Username del Creatore della Build
     * @return user - istanza di User che contiene tutti i dati dell'utente
     * @throws JSONException
     */
    public User getUser(String username) throws JSONException {

        documentReference = firestore.collection("/" + UserRepository.USERS_COLLECTION)
                .document("/" + UserRepository.USERS_COLLECTION + "/" + username);
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
                Log.e(MainActivity.class.getSimpleName(), "Error trying to read data!");
            }
        });

        if(userData == null)
            return null;

        JSONObject o = new JSONObject(userData);

        return new User(o.getString("mail"),
                o.getString("password"),
                o.getString("username"),
                (List<Build>) userData.get("favorite"),
                (List<Build>) userData.get("created"),
                (Uri) userData.get("image")
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
        if(getBuild(build) == null)
            return false;
        return true;
    }

    //CARICA NEL DATABASE DI TUTTI GLI ELEMENTI DELLA BUILD SE ESSI NON ESISTONO
    private boolean writeComponents(Build build) {
        boolean success = true;
        if(!componentRepository.exists(ComponentType.MOTHERBOARD, build.getBoard().getId()))
            success = success && componentRepository.setData(ComponentType.MOTHERBOARD, build.getBoard());
        if(!componentRepository.exists(ComponentType.CPU, build.getCpu().getId()))
            success = success && componentRepository.setData(ComponentType.CPU, build.getCpu());
        if(!componentRepository.exists(ComponentType.CPU_FAN, build.getFan().getId()))
            success = success && componentRepository.setData(ComponentType.CPU_FAN, build.getFan());
        if(!componentRepository.exists(ComponentType.GPU, build.getGpu().getId()))
            success = success && componentRepository.setData(ComponentType.GPU, build.getGpu());
        if(!componentRepository.exists(ComponentType.PSU, build.getPsu().getId()))
            success = success && componentRepository.setData(ComponentType.PSU, build.getPsu());
        if(!componentRepository.exists(ComponentType.CASE, build.getHouse().getId()))
            success = success && componentRepository.setData(ComponentType.MOTHERBOARD, build.getHouse());

        for(RAM r : build.getRams())
            if(!componentRepository.exists(ComponentType.RAM, r.getId()))
                success = success && componentRepository.setData(ComponentType.RAM, r);

        for(Memory m : build.getHarddisks())
            if(!componentRepository.exists(ComponentType.MEMORY, m.getId()))
                success = success && componentRepository.setData(ComponentType.MEMORY, m);


        return success;
    }

}
