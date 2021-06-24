package com.example.project_mc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_mc.models.ApiClient;
import com.example.project_mc.models.ApiInterface;
import com.example.project_mc.models.ChatDialog;
import com.example.project_mc.models.DataModel;
import com.example.project_mc.models.Message;
import com.example.project_mc.models.NotificationModel;
import com.example.project_mc.models.RootModel;
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
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

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
    Map<String, Object> group;
    String groupName;
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

        messagesListAdapter = new MessagesListAdapter<Message>(firebaseAuth.getUid().toString(), new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        });
        inputView = findViewById(R.id.input);
        messagesList.setAdapter(messagesListAdapter);
        db = FirebaseFirestore.getInstance();
        db.collection("Groups").document(group_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        group =  doc.getData();
                        groupName = (String) group.get("dialogName");
                    }
                }
            }
        });

        TextView typing_view = findViewById(R.id.typing_indicator);
        typing_view.setVisibility(View.INVISIBLE);


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

                    currentUser.put("avatar", "https://www.designbust.com/download/1060/png/microsoft_logo_transparent512.png");
                }
                else{
                    currentUser.put("avatar", firebaseAuth.getCurrentUser().getPhotoUrl().toString());

                }
                currentUser.put("name",user.getDisplayName().toString());
                newMessage.setAuthor(currentUser);
                db.collection("Groups").document(group_id).collection("messages").add(newMessage.hashMap());
                String title = user.getDisplayName().toString() + " in " + groupName;
                sendNotificationToUser("/topics/all",title,newMessage.getText());
                //messagesListAdapter.addToStart(newMessage,true);
                return true;
            }
        });

        inputView.setTypingListener(new MessageInput.TypingListener() {
            @Override
            public void onStartTyping() {
                db.collection("Groups").document(group_id).update("isTyping", true
                ,
                        "TyperID", FirebaseAuth.getInstance().getCurrentUser().getUid()
                , "TyperName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


            }

            @Override
            public void onStopTyping() {
                db.collection("Groups").document(group_id).update("isTyping", false
                        ,
                        "TyperID", ""
                , "TyperName", "");

            }
        });

        inputView.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {

            }
        });
}

    @Override
    protected void onStart() {
        super.onStart();
        TextView typingindicator_view = findViewById(R.id.typing_indicator);

        db.collection("Groups").document(group_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value !=null)
                {

                    if(value.getData().get("isTyping") !=null)
                    {
                        boolean check_typing = (boolean) value.getData().get("isTyping");
                        if(check_typing)
                        {
                            if(value.getData().get("TyperID") !=null)
                            {
                                Log.d("Bro","Is this working???????");
                                String id = value.getData().get("TyperID").toString();
                                if(id != FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                                {
                                    if(value.getData().get("TyperName") !=null){
                                        String name = value.getData().get("TyperName").toString() + " is Typing...";
                                        typingindicator_view.setText(name);
                                    }
                                    typingindicator_view.setVisibility(View.VISIBLE);

                                }

                                else
                                {
                                    typingindicator_view.setVisibility(View.INVISIBLE);
                                }


                            }
                            else
                            {
                                typingindicator_view.setVisibility(View.INVISIBLE);
                            }


                        }
                        else
                        {
                            typingindicator_view.setVisibility(View.INVISIBLE);
                        }
                    }
                    else
                    {
                        typingindicator_view.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        
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
    private void sendNotificationToUser(String token,String title,String message) {
        RootModel rootModel = new RootModel(token, new NotificationModel(title, message), new DataModel(title,message));

        ApiInterface apiService =  ApiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d(TAG,"Successfully notification send by using retrofit.");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    }
