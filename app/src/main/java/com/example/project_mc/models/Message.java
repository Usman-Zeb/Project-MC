package com.example.project_mc.models;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements IMessage {

    public String id;
    public String text;
    public IUser author;
    public Date createdAt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public Map<String, Object> hashMap(){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("text", text);
        hashMap.put("id", id);
        hashMap.put("author",author);
        hashMap.put("createdAt", createdAt);
        return hashMap;
    }
}
