package com.example.project_mc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_mc.models.ChatDialog;
import com.example.project_mc.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRooms extends AppCompatActivity {

    private static final String TAG = "Chats List";
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    TextView nameView;
    ImageView profilePic;
    Button logoutButton;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    ArrayList<ChatDialog> chats = new ArrayList<>();
    DialogsList dialogList;
    DialogsListAdapter dialogsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        final Intent logOutintent = new Intent(this,MainActivity.class);
        dialogList = (DialogsList) findViewById(R.id.chats_list);

        dialogsListAdapter = new DialogsListAdapter<ChatDialog>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                if(!url.equals(""))
                    Picasso.get().load(url).into(imageView);
            }
        });

        dialogList.setAdapter(dialogsListAdapter);
        /*User user = new User();
        user.avatar="https://randomuser.me/api/portraits/men/68.jpg";
        user.id="123";
        user.name="Hello bro";
        ArrayList<User> users = new ArrayList<>();
        users.add(user);*/

        ChatDialog chatDialog = new ChatDialog();
        chatDialog.id="123";
        chatDialog.dialogName = "First Message";
        chatDialog.dialogPhoto= "https://randomuser.me/api/portraits/men/68.jpg";
        chatDialog.unreadCount=0;
       // chatDialog.users=users;

        dialogsListAdapter.addItem(chatDialog);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        //dialogsListAdapter.addItem(chatDialog);

       /* logoutButton = findViewById(R.id.button_chats_logout);
        logoutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signOut();
                        startActivity(logOutintent);
                        finish();
                    }
                }
        );*/

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        View view = super.onCreateView(name, context, attrs);

       /*ChatDialog chatDialog = new ChatDialog();
        chatDialog.id="123";
        chatDialog.dialogName = "First Message";
        chatDialog.dialogPhoto= "";
        chatDialog.unreadCount=0;

        dialogsListAdapter.addItem(chatDialog);*/

        return view;
    }

    private void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //todo
                    }
                });
    }


}