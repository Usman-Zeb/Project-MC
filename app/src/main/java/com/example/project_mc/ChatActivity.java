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
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.Date;
import java.util.HashMap;
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

        messagesList = (MessagesList) findViewById(R.id.messagesList);

        messagesListAdapter = new MessagesListAdapter<Message>(firebaseAuth.getUid().toString(), null);
        inputView = findViewById(R.id.input);
        messagesList.setAdapter(messagesListAdapter);
        db = FirebaseFirestore.getInstance();
        ArrayList<Message> init_messages = new ArrayList<Message>();
        db.collection("Groups").document("AyV8rltUSQNXduBhmEyN").collection("messages").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Map<String, Object>> data =  new ArrayList<>();
                        for (QueryDocumentSnapshot document: task.getResult()){
                            data.add(document.getData());
                        }
                        for (Map<String, Object> singleData: data)
                        {
                            Message message = new Message();
                            Log.d("Data Name: ", singleData.get("id").toString() + "\n");
                            message.id = singleData.get("id").toString();
                            message.setAuthor((HashMap<String,Object>) singleData.get("author"));
                            message.text = singleData.get("text").toString();
                            message.createdAt =  new Date();
                            init_messages.add(message);
                        }
                        messagesListAdapter.addToEnd(init_messages,false);
                    }
                }
        );
        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Message newMessage =  new Message();
                newMessage.createdAt = new Date();
                newMessage.text = input.toString();
                newMessage.id = "newMessage";
                HashMap<String,Object> currentUser = new HashMap<String,Object>();
                currentUser.put("id",firebaseAuth.getUid());
                currentUser.put("avatar","https://www.google.com/url?sa=i&url=https%3A%2F%2Fsupport.microsoft.com%2Fen-us%2Fwhats-new&psig=AOvVaw2UOk2vjnavAaF8efhdZ-1J&ust=1623157318147000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJDZ2K3KhfECFQAAAAAdAAAAABAD");
                currentUser.put("name",user.getDisplayName().toString());
                newMessage.setAuthor(currentUser);
                db.collection("Groups").document("AyV8rltUSQNXduBhmEyN").collection("messages").add(newMessage.hashMap());
                messagesListAdapter.addToStart(newMessage,true);
                return true;
            }
        });
}
}