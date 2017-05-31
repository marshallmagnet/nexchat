package com.nexuchat.nexusninja2.nexchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private TextView txtSignUp;
    private EditText etMail, etPassword;
    private Button btnLogin, btnFb, btnGoogle;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mAuthlistener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null)
                {
                    GoHome();
                }
            }
        };

        etMail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        txtSignUp = (TextView)findViewById(R.id.txtSignUp);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnFb = (Button)findViewById(R.id.btnFb);
        btnGoogle = (Button)findViewById(R.id.btnGoogle);

        txtSignUp.setOnClickListener(onClick());
        btnLogin.setOnClickListener(onClick());
        btnFb.setOnClickListener(onClick());
        btnGoogle.setOnClickListener(onClick());

        mAuthlistener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null)
                {
                    GoHome();
                }
                else if (currentUser == null)
                {

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

    private void LoginUser(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(!task.isSuccessful())
                {
                    Toaster("Authentication failed.");
                }
            }
        });
    }

    private void GoHome()
    {
        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(homeIntent);
    }

    private void GoSignUp()
    {
        Intent homeIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(homeIntent);
    }

    private void Toaster(String message)
    {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener onClick()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int buttonID = v.getId();

                if(buttonID == R.id.txtSignUp)
                {
                    GoSignUp();
                }
                else if(buttonID == R.id.btnLogin)
                {
                    LoginUser(etMail.getText().toString(), etPassword.getText().toString());
                }
                else if(buttonID == R.id.btnFb)
                {

                }
                else if(buttonID == R.id.btnGoogle)
                {

                }
            }
        };
    }
}
