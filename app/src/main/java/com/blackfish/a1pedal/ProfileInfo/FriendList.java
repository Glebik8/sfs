package com.blackfish.a1pedal.ProfileInfo;

public class FriendList {
    private String name;
    private String image;
    private String model;

    private String id;
    private String phone;
    private String work;

    public String getLast_activity() {
        return last_activity;
    }

    public void setLast_activity(String last_activity) {
        this.last_activity = last_activity;
    }

    private String last_activity;


    private String type;
    public FriendList(String name, String image, String model, String id, String work, String type,String last_activity) {
        this.name = name;
        this.image = image;
        this.model = model;
        this.id = id;
        this.work = work;
        this.type = type;
        this.last_activity=last_activity;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }






}
