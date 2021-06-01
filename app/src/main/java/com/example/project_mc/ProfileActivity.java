package com.example.project_mc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    TextView nameView;
    ImageView profilePic;
    Button logoutButton;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        final Intent logOutintent = new Intent(this,MainActivity.class);
        setContentView(R.layout.activity_profile);
        nameView = findViewById(R.id.nameView);
        profilePic = findViewById(R.id.profilePic);
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signOut();
                        startActivity(logOutintent);
                        finish();
                    }
                }
        );
        nameView.setText(user.getDisplayName());
        Picasso.get().load(user.getPhotoUrl()).into(profilePic);*/
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
