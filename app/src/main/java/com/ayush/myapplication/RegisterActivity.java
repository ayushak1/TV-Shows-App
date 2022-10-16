package com.ayush.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

//import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputName,inputEmail,inputPassword;
    private Button btnLogin,goback;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        btnLogin=(Button) findViewById(R.id.forgotPassword);
        btnLogin.setOnClickListener(this);

        goback=(Button) findViewById(R.id.goback);
        goback.setOnClickListener(this);

        inputName=(EditText) findViewById(R.id.inputName);
        inputEmail=(EditText)findViewById(R.id.inputEmail);
        inputPassword=(EditText) findViewById(R.id.inputPassword);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgotPassword:
                registeruser();
                break;
            case R.id.goback:
                onBackPressed();
                break;
       }
    }
    private void registeruser(){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String Name = inputName.getText().toString().trim();

        if(Name.isEmpty()){
            inputName.setError("FULL NAME IS REQUIRED");
            inputName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            inputEmail.setError("EMAIL  IS REQUIRED");
            inputEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Please Provide Valid Email");
            inputEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            inputPassword.setError("PASSWORD IS REQUIRED");
            inputPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            inputPassword.setError("Min Password Length Should Be 6 characters!");
            inputPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(Name,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "SUCCESSFULLY REGISTERED", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                            else{
                                                Toast.makeText(RegisterActivity.this, "Failed to register TRY AGAIN!", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterActivity.this, "Failed to register TRY AGAIN!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                });
    }
}