package com.example.socialgaming.data;

public class PSU {
    private String mId;
    private String mName;
    private String mImage;

    public PSU(String mId, String mName){
        this(mId, mName, null);
    }

    public PSU(String mId, String mName, String mImage) {
        setId(mId);
        setName(mName);
        setImage(mImage);
    }
    

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }
}
