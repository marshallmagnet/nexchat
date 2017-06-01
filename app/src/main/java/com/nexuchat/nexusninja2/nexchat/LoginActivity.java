package com.nexuchat.nexusninja2.nexchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient googleApiClient;

    private TextView txtSignUp;
    private EditText etMail, etPassword;
    private Button btnLogin, btnFb;
    private SignInButton btnGoogle;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
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

        GoogleSignInOptions googleSignOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignOptions)
                .build();

        etMail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        txtSignUp = (TextView)findViewById(R.id.txtSignUp);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnFb = (Button)findViewById(R.id.btnFb);
        btnGoogle = (SignInButton) findViewById(R.id.btnGoogle);

        txtSignUp.setOnClickListener(onClick());
        btnLogin.setOnClickListener(onClick());
        btnFb.setOnClickListener(onClick());
        btnGoogle.setOnClickListener(onClick());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else
            {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void StoreData(FirebaseUser mUser)
    {
        User user = new User(mUser.getEmail(), mUser.getDisplayName(), mUser.getPhotoUrl().toString());
        databaseReference.child("Users").child(mUser.getUid()).setValue(user);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            StoreData(user);
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toaster("Authentication failed.");
                        }
                    }
                });
    }

    private void SignInGoogle()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                    SignInGoogle();
                }
            }
        };
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }
}