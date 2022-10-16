package com.ayush.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.myapplication.activities.mvvm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tvRegisterHere,Forgot;
    private EditText etLoginEmail, etLoginPass;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    public static final String SHARED_PREFS = "sharedprefs";



    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);
        tvRegisterHere.setOnClickListener(this);

        Forgot = (TextView) findViewById(R.id.Forgot);
        Forgot.setOnClickListener(this);

        btnLogin = (Button) findViewById(R.id.forgotPassword);
        btnLogin.setOnClickListener(this);

        progressBar2=(ProgressBar) findViewById(R.id.progressBar2);

        etLoginEmail = (EditText) findViewById(R.id.email);
        etLoginPass = (EditText) findViewById(R.id.etLoginPass);

        mAuth = FirebaseAuth.getInstance();

        checkBox();


    }

    private void checkBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String check = sharedPreferences.getString("email","");
        if(check.equals("true")){
            Toast.makeText(LoginActivity.this,"Login...",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, mvvm.class));
            finish();
            progressBar2.setVisibility(View.GONE);
        }

    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        String s1 = sh.getString("etLoginEmail","");
//        String s2 = sh.getString("etLoginPass","");
//
//        etLoginEmail.setText(s1);
//        etLoginPass.setText(s2);
//
//    }
//
//    @Override
//    public  void onPause(){
//        super.onPause();
//        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        SharedPreferences.Editor myEdit = sharedPreferences.edit();
//
//        myEdit.putString("etLoginEmail",etLoginEmail.getText().toString());
//        myEdit.putString("etLoginPass",etLoginPass.getText().toString());
//        myEdit.apply();
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRegisterHere:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.Forgot:
                startActivity(new Intent(LoginActivity.this, Forgot.class));
                break;
            case R.id.forgotPassword:
                userlogin();
                break;

        }
    }
    private void userlogin() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPass.getText().toString().trim();

        if (email.isEmpty()) {
            etLoginEmail.setError("Email is required");
            etLoginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etLoginEmail.setError("Please Provide Valid Email");
            etLoginEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etLoginPass.setError("Email is required");
            etLoginPass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etLoginPass.setError("Min Password Length Should Be 6 characters!");
            etLoginPass.requestFocus();
            return;
        }
        progressBar2.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email","true");
                    editor.putString("password","true");
                    editor.apply();

                    if (user.isEmailVerified()) {
                        checkBox();

                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check Your Mail To Verify also spam folder", Toast.LENGTH_LONG).show();
                        progressBar2.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed To login please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                }
            }
        });
    }
}
