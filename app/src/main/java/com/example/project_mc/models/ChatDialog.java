package com.example.project_mc.models;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatDialog implements IDialog {
    public  String id;
    public String dialogPhoto;
    public String dialogName;
    public ArrayList<User> users = new ArrayList<>();
    public Message lastMessage;
    public int unreadCount;

    public String TyperID;
    public String TyperName;
    public boolean isTyping;




    public boolean getIsTyping(){return isTyping;}
    public String getTyperName(){return TyperName;}

    public String getTyperID(){return TyperID;}
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage lastMessage) {
        this.lastMessage = (Message) lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public Map<String, Object> hashMap(){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("dialogName", dialogName);
        hashMap.put("dialogPhoto", dialogPhoto);
        hashMap.put("users",users);
        hashMap.put("lastMessage", lastMessage);
        hashMap.put("unreadCount", unreadCount);
        hashMap.put("isTyping", isTyping);
        hashMap.put("TyperID", TyperID);
        hashMap.put("TyperName", TyperName);


        return hashMap;
    }
}
