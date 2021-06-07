package com.example.project_mc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_mc.models.ChatDialog;
import com.example.project_mc.models.Message;
import com.example.project_mc.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
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
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        currentUser.name= user.getDisplayName();
        //currentUser.avatar=user.getPhotoUrl().toString();
        currentUser.id=user.getUid();

        messagesList = (MessagesList) findViewById(R.id.messagesList);

        messagesListAdapter = new MessagesListAdapter<Message>("123", null);

        messagesList.setAdapter(messagesListAdapter);
        db = FirebaseFirestore.getInstance();
        ArrayList<ChatDialog> init_messages = new ArrayList<>();
        db.collection("messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Map<String, Object>> data =  new ArrayList<>();
                    for (QueryDocumentSnapshot document: task.getResult()){
                        data.add(document.getData());
                    }
            }
        })
    }
}