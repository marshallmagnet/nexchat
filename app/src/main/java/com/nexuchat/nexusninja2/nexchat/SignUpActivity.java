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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity
{
    private TextView txtLogin;
    private Button btnSignUp;
    private EditText etMail, etPassword, etFullName;
    private RadioGroup rbGroup;
    private RadioButton rbSelected;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
                            StoreData(task.getResult().getUser(), etMail.getText().toString(), etPassword.getText().toString(), etFullName.getText().toString());
                            mAuth.signOut();
                        }
                    }
                });
    }

    private void StoreData(FirebaseUser mUser, String email, String password, String fullname)
    {
        rbGroup = (RadioGroup)findViewById(R.id.rgGender);
        rbSelected = (RadioButton)findViewById(rbGroup.getCheckedRadioButtonId());
        String dbUserId = databaseReference.push().getKey();
        User user = new User(email, password, fullname, rbSelected.getText().toString(), mUser.getUid());
        databaseReference.child("users").child(dbUserId).setValue(user);
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
