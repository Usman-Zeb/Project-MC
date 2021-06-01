package com.example.project_mc.models;

import com.stfalcon.chatkit.commons.models.IUser;

public class User implements IUser {

    public  String id;
    public String name;
    public String avatar;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
