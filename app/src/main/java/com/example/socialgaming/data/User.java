package com.example.socialgaming.data;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String mail;
    private String password;
    private String username;
    private List<Build> favorite;
    private List<Build> created;

    public User(String mail, String password, String username, List<Build> favorite, List<Build> created) {
        this.mail = mail;
        this.password = password;
        this.username = username;
        this.favorite = favorite;
        this.created = created;
    }

    public User(String mail, String password, String username) {
        this(mail, password, username, new ArrayList<>(), new ArrayList<>());
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
}
