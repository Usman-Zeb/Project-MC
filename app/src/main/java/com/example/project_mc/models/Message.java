package com.example.project_mc.models;

import android.media.Image;
import android.util.Log;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements IMessage,
        MessageContentType.Image {

    public String id;
    public String text;
    public User author = new User();
    public Date createdAt = new Date();

    public MessageContentType.Image image;




    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public User getUser() {
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

    public void setAuthor(HashMap<String,Object> data)
    {
        Log.d("SHOW THIS BITCH", data.get("id").toString());
        this.author.id = data.get("id").toString();
        this.author.avatar = data.get("avatar").toString();
        this.author.name = data.get("name").toString();
    }

    public <T> Comparable<T> getDateTime() {
        return (Comparable<T>) this.createdAt;
    }

    @Nullable
    @Override
    public String getImageUrl() {
        return image == null ? null : image.getImageUrl();
    }
}
