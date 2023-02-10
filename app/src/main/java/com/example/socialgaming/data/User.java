package com.example.socialgaming.data;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.socialgaming.PcBuilder;
import com.example.socialgaming.R;
import com.example.socialgaming.utils.ImageUtils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class User {
    public static Bitmap DEFAULT_IMAGE;

    private String mail;
    private String password;
    private String username;
    private List<String> favorite;
    private List<String> created;
    private Bitmap image;

    public User(String mail, String password, String username, List<String> favorite, List<String> created, Bitmap image) {
        this.mail = mail;
        this.password = password;
        this.username = username;
        this.favorite = favorite;
        this.created = created;
        this.image = image;
    }

    public User(String mail, String password, String username) {
        this(mail, password, username, new ArrayList<>(), new ArrayList<>(), DEFAULT_IMAGE);
    }

    public User() {
        favorite = new ArrayList<>();
        created = new ArrayList<>();
        image = DEFAULT_IMAGE;
    }

    public void updateWithDocument(DocumentSnapshot documentSnapshot) {
        Map<String, Object> data = documentSnapshot.getData();
        this.mail = (String) data.get("mail");
        this.password = (String) data.get("password");
        this.username = (String) data.get("username");
        if(data.get("favorite") instanceof Map)
            this.favorite = new ArrayList<>(((Map<String, String>)data.get("favorite")).values());
        else
            this.favorite = (List<String>) data.get("favorite");
        if(data.get("created") instanceof Map)
            this.created = new ArrayList<>(((Map<String, String>)data.get("created")).values());
        else
            this.created = (List<String>) data.get("created");
        List<Long> byteList = ((List<Long>)data.get("image"));
        byte[] byteArray = ImageUtils.decodeListToArray(byteList);
        this.image = ImageUtils.decodeByteArrayToBitmap(byteArray);
    }

    public Map<String, Object> getMap() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("password", password);
        attributes.put("created", created);
        attributes.put("favorite", favorite);
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return this.username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mail, password, username, favorite, created, image);
    }

    public boolean addFavoriteBuild(BuildFirestore build) {
        String uuid = build.getUuid().toString();
        if(favorite.contains(uuid))
            return false;
        favorite.add(uuid);
        return true;
    }

    public boolean addBuild(BuildFirestore build) {
        String uuid = build.getUuid().toString();
        if(created.contains(uuid))
            return false;
        created.add(uuid);
        return true;
    }

    public boolean removeFavoriteBuild(BuildFirestore build) {
        String uuid = build.getUuid().toString();
        if(favorite.contains(uuid)) {
            favorite.remove(uuid);
            return true;
        }
        return false;
    }

    public boolean removeBuild(BuildFirestore build) {
        String uuid = build.getUuid().toString();
        if(created.contains(uuid)) {
            created.remove(uuid);
            return true;
        }
        return false;
    }

    /*
        1 = "ERROR: New password cannot be equals to the last one!"
        0 = "Password changed successfully!"
        -1 = "ERROR: You inserted the wrong password."
    */
    public int changePassword(String oldPass, String newPass) {
        if(oldPass.equals(password)) {
            if(newPass.equals(password)) {
                return 1;
            }
            else {
                password = newPass;
                return 0;
            }
        }

        return -1;
    }

    /*
    /**
     * @return - User value based on all his build
     *
    public double getUserValue() {

        if(created.size() == 0)
            return 0.0;

        int value = 0;
        for(BuildFirestore b : created)
            value = (int) (value + b.getValue() * 10);
        value = value / (created.size());
        return value / 10.0;

    }*/

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<String> getFavorite() {
        return favorite;
    }
    public void setFavorite(List<String> favorite) {
        this.favorite = favorite;
    }
    public List<String> getCreated() {
        return created;
    }
    public void setCreated(List<String> created) {
        this.created = created;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
}
