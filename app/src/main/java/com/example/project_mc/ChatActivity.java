package com.example.project_mc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_mc.models.ChatDialog;
import com.example.project_mc.models.Message;
import com.example.project_mc.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "Chats List";
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    TextView nameView;
    ImageView profilePic;
    ArrayList<ChatDialog> chats = new ArrayList<>();
    MessagesList messagesList;
    MessagesListAdapter messagesListAdapter;
    User currentUser = new User();
    Message message = new Message();
    ArrayList<Message> init_messages = new ArrayList<Message>();
    private FirebaseFirestore db;
    String group_id;
    MessageInput inputView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        currentUser.name= user.getDisplayName();
        //currentUser.avatar=user.getPhotoUrl().toString();
        currentUser.id=user.getUid();

        group_id = getIntent().getStringExtra("Group_ID");
        Log.d("This is my ID: ", group_id);

        messagesList = (MessagesList) findViewById(R.id.messagesList);

        messagesListAdapter = new MessagesListAdapter<Message>(firebaseAuth.getUid().toString(), null);
        inputView = findViewById(R.id.input);
        messagesList.setAdapter(messagesListAdapter);
        db = FirebaseFirestore.getInstance();


        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Message newMessage =  new Message();
                newMessage.createdAt = new Date();
                newMessage.text = input.toString();
                newMessage.id = "newMessage";
                HashMap<String,Object> currentUser = new HashMap<String,Object>();
                currentUser.put("id",firebaseAuth.getUid());
                if(firebaseAuth.getCurrentUser().getPhotoUrl() == null){

                    currentUser.put("avatar", "https://www.google.com.pk/url?sa=i&url=https%3A%2F%2Fcommons.wikimedia.org%2Fwiki%2FFile%3AMicrosoft_logo.svg&psig=AOvVaw3PrWg1jza_UoiOaOIkRo3u&ust=1623522034288000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCMCg1oGZkPECFQAAAAAdAAAAABAD");
                }
                else{
                    currentUser.put("avatar", firebaseAuth.getCurrentUser().getPhotoUrl().toString());

                }
                currentUser.put("name",user.getDisplayName().toString());
                newMessage.setAuthor(currentUser);
                db.collection("Groups").document(group_id).collection("messages").add(newMessage.hashMap());
                //messagesListAdapter.addToStart(newMessage,true);
                return true;
            }
        });
}

    @Override
    protected void onStart() {
        super.onStart();
        CollectionReference messageRef = db.collection("Groups").document(group_id).collection("messages");
        messageRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.err.println("Listen failed:" + error);
                    return;
                }
                init_messages = new ArrayList<Message>();
                if (value !=null)
                {
                    for (DocumentSnapshot doc : value) {
                        {
                            Map<String, Object> newData = doc.getData();
                            Message message = new Message();
                            Log.d("Data Name: ", newData.get("id").toString() + "\n");
                            message.id = newData.get("id").toString();
                            message.setAuthor((HashMap<String,Object>) newData.get("author"));
                            message.text = newData.get("text").toString();
                            message.createdAt =  doc.getTimestamp("createdAt").toDate();
                            init_messages.add(message);
                        }
                    }
                    Collections.sort(init_messages, new Comparator<Message>() {
                        @Override
                        public int compare(Message message, Message t1) {
                            return message.getDateTime().compareTo(t1.getDateTime());
                        }

                    });
                    messagesListAdapter.clear();
                    messagesListAdapter.addToEnd(init_messages, true);
                }

            }
            });
        }
    }
