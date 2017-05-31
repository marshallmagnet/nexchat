package com.nexuchat.nexusninja2.nexchat;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUpActivity extends AppCompatActivity
{
    private TextView txtLogin;
    private Button btnSignUp;
    private EditText etMail, etPassword, etFullName;
    private RadioGroup rbGroup;
    private RadioButton rbSelected;
    private FirebaseAuth mAuth;
    private String photoUrl;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //storageReference = FirebaseStorage.getInstance().getReference();

        txtLogin = (TextView)findViewById(R.id.txtLogin);
        etMail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etFullName = (EditText)findViewById(R.id.etFullname);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        txtLogin.setOnClickListener(onClick());
        btnSignUp.setOnClickListener(onClick());
    }

    private void SignUpUser(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            Toaster("Registration failed");
                            mAuth.signOut();
                        }
                        else if (task.isSuccessful())
                        {
                            StoreData(task.getResult().getUser(), etFullName.getText().toString());
                        }
                    }
                });
    }

    private void UpdateProfile()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(etFullName.getText().toString())
                .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/android-ios-test.appspot.com/o/no_image.png?alt=media&token=1c4d55b0-452c-485b-acb0-221fffcf7cb7"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            mAuth.signOut();
                            GoLogin();
                        }
                        else if(!task.isSuccessful())
                        {
                            Toaster("Error!");
                        }
                    }
                });
    }

    private void StoreData(FirebaseUser mUser, String fullname)
    {
        rbGroup = (RadioGroup)findViewById(R.id.rgGender);
        rbSelected = (RadioButton)findViewById(rbGroup.getCheckedRadioButtonId());
        //String dbUserId = databaseReference.push().getKey();
        //User user = new User(email, password, fullname, rbSelected.getText().toString(), mUser.getUid());

        if (mUser.getPhotoUrl() == null)
        {
            photoUrl = "https://firebasestorage.googleapis.com/v0/b/android-ios-test.appspot.com/o/no_image.png?alt=media&token=1c4d55b0-452c-485b-acb0-221fffcf7cb7";
        }
        else if (mUser.getPhotoUrl() != null)
        {
            photoUrl = mUser.getPhotoUrl().toString();
        }

        User user = new User(mUser.getEmail(), fullname, photoUrl);
        databaseReference.child("Users").child(mUser.getUid()).setValue(user);
        UpdateProfile();
    }

    private  void GoLogin()
    {
        Intent homeIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(homeIntent);
    }
    private void Toaster(String message)
    {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener onClick()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int buttonID = v.getId();
                if(buttonID == R.id.btnSignUp)
                {
                    SignUpUser(etMail.getText().toString(), etPassword.getText().toString());
                }
                else if (buttonID == R.id.txtLogin)
                {
                    GoLogin();
                }
            }
        };
    }
}
