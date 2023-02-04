package com.example.socialgaming.data;

import android.content.ContentResolver;
import android.net.Uri;

import com.example.socialgaming.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class User {
    public static final Uri DEFAULT_IMAGE = Uri.parse("android.resource://com.example.socialgaming/" + R.drawable.logo);

    private String mail;
    private String password;
    private String username;
    private List<Build> favorite;
    private List<Build> created;
    private Uri image;

    public User(String mail, String password, String username, List<Build> favorite, List<Build> created, Uri image) {
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

    public User() {}

    public void updateWithDocument(DocumentSnapshot documentSnapshot) {
        Map<String, Object> data = documentSnapshot.getData();
        this.mail = (String) data.get("mail");
        this.password = (String) data.get("password");
        this.username = (String) data.get("username");
        this.favorite = (List<Build>) data.get("favorite");
        this.created = (List<Build>) data.get("created");
        this.image = Uri.parse((String) data.get("image"));
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

    public boolean addFavoriteBuild(Build build) {
        if(favorite.contains(build))
            return false;
        favorite.add(build);
        return true;
    }

    public boolean addBuild(Build build) {
        if(created.contains(build))
            return false;
        created.add(build);
        return true;
    }

    public boolean removeFavoriteBuild(Build build) {
        if(favorite.contains(build)) {
            favorite.remove(build);
            return true;
        }
        return false;
    }

    public boolean removeBuild(Build build) {
        if(created.contains(build)) {
            created.remove(build);
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
    public List<Build> getFavorite() {
        return favorite;
    }
    public void setFavorite(List<Build> favorite) {
        this.favorite = favorite;
    }
    public List<Build> getCreated() {
        return created;
    }
    public void setCreated(List<Build> created) {
        this.created = created;
    }
    public Uri getImage() {
        return image;
    }
    public void setImage(Uri image) {
        this.image = image;
    }
}
