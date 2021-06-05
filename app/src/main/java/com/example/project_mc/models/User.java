package com.example.project_mc.models;

import com.stfalcon.chatkit.commons.models.IUser;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> hashMap(){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("name", name);
        hashMap.put("avatar", avatar);

        return hashMap;
    }
}
