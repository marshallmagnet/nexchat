package com.nexuchat.nexusninja2.nexchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(onClick());

        mAuthlistener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null)
                {
                    //Toast.makeText(HomeActivity.this, "Successfully Logged In.", Toast.LENGTH_SHORT).show();
                }
                else if (currentUser == null)
                {
                    GoLogin();
                }
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //do something to update the user class
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthlistener != null)
        {
            mAuth.removeAuthStateListener(mAuthlistener);
        }
    }

    private void Logout()
    {
        mAuth.signOut();
        GoLogin();
    }

    private void GoLogin()
    {
        Intent homeIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(homeIntent);
    }

    private View.OnClickListener onClick()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int buttonID = v.getId();
                if(buttonID == R.id.btnLogout)
                {
                    Logout();
                }
            }
        };
    }
}
