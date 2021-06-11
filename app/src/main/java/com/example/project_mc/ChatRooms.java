package com.example.project_mc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_mc.models.ChatDialog;
import com.example.project_mc.models.Message;
import com.example.project_mc.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
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
    FloatingActionButton profileButton;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    ArrayList<ChatDialog> chats = new ArrayList<>();
    DialogsList dialogList;
    DialogsListAdapter dialogsListAdapter;
    User currentUser = new User();
    ChatDialog chatDialog = new ChatDialog();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);

        profileButton = findViewById(R.id.profile_button);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        currentUser.name= user.getDisplayName();
        //currentUser.avatar=user.getPhotoUrl().toString();
        currentUser.id=user.getUid();

        dialogList = (DialogsList) findViewById(R.id.chats_list);

        dialogsListAdapter = new DialogsListAdapter<ChatDialog>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                if(!url.equals(""))
                    Picasso.get().load(url).into(imageView);
            }
        });

        dialogList.setAdapter(dialogsListAdapter);



        FloatingActionButton floatingActionButton = findViewById(R.id.create_chat_btn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChatDialog();
                Log.d("Button", "Is this working?");
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatRooms.this,  ProfileActivity.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();
        ArrayList<ChatDialog> init_groups = new ArrayList<>();
        db.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Map<String, Object>> data =  new ArrayList<>();
                            for (QueryDocumentSnapshot document: task.getResult()){
                                data.add(document.getData());
                                //Log.d("THe DATA:", document.getId() + " => " + data + "\n");
                                /*chatDialog.dialogPhoto= data.get("dialogPhoto").toString();
                                chatDialog.id= data.get("id").toString();
                                chatDialog.dialogName= data.get("dialogName").toString();
                                chatDialog.users = (ArrayList<User>) data.get("users");
                                //chatDialog.unreadCount = (int) data.get("unreadCount");
                                chatDialog.lastMessage = (Message) data.get("lastMessage");
                                dialogsListAdapter.addItem(chatDialog);*/
                            }


                            for (Map<String, Object> singleData: data)
                            {
                                ChatDialog chatDialog = new ChatDialog();
                                Log.d("Data Name: ", singleData.get("dialogName").toString() + "\n");
                                chatDialog.dialogPhoto= singleData.get("dialogPhoto").toString();
                                chatDialog.id= singleData.get("id").toString();
                                chatDialog.dialogName= singleData.get("dialogName").toString();
                                chatDialog.users = (ArrayList<User>) singleData.get("users");
                                //chatDialog.unreadCount = (int) singleData.get("unreadCount");
                                chatDialog.lastMessage = (Message) singleData.get("lastMessage");
                                init_groups.add(chatDialog);


                            }


                        }
                        dialogsListAdapter.setItems(init_groups);
                    }

                });


        dialogsListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener() {
            @Override
            public void onDialogClick(IDialog dialog) {
                Intent intent = new Intent(ChatRooms.this,  ChatActivity.class);
                startActivity(intent);
            }

        });

    }

    private void createChat(String chat_name){
        //final TextView Group_Image = new TextView(ChatRooms.this);
        //Group_Image.setText();
        chatDialog.dialogName=chat_name;
        //chatDialog.dialogPhoto= firebaseAuth.getCurrentUser().getPhotoUrl().toString();
        chatDialog.dialogPhoto= "https://www.google.com/url?sa=i&url=https%3A%2F%2Fsupport.microsoft.com%2Fen-us%2Fwhats-new&psig=AOvVaw2UOk2vjnavAaF8efhdZ-1J&ust=1623157318147000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCJDZ2K3KhfECFQAAAAAdAAAAABAD";
        chatDialog.unreadCount=0;
        chatDialog.lastMessage=null;
        chatDialog.users.add(currentUser);
        chatDialog.id=chat_name + "_id";
        dialogsListAdapter.addItem(chatDialog);



        db.collection("Groups")
                .add(chatDialog.hashMap())
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
    }

    private void showChatDialog(){
        /*final EditText editText = new EditText(ChatRooms.this);
        AlertDialog dialog = new AlertDialog.Builder(ChatRooms.this)
                .setTitle("Create a Group")
                .setMessage("Enter Group Name")
                .setView(editText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();*/

        //Custom Alert Dialog
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ChatRooms.this);
        View mView = getLayoutInflater().inflate(R.layout.gc_dialog, null);
        final EditText mCreateGC = (EditText) mView.findViewById(R.id.group_name_dialog);
        Button mCreate = (Button) mView.findViewById(R.id.gc_btn_dialog);


        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mCreate.getText().toString().isEmpty())
                {
                    Toast.makeText(ChatRooms.this, "Group Chat created", Toast.LENGTH_SHORT).show();
                    dialog.hide();
                    createChat(mCreateGC.getText().toString());
                }
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.item1:
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                final Intent logOutintent = new Intent(this,MainActivity.class);
                signOut();
                startActivity(logOutintent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}