package com.ayush.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class About_me extends AppCompatActivity {
    Button button23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        button23 = findViewById(R.id.button23);
        onClickButton();
    }

    private void onClickButton() {
        button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"ayushkarnn@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Enter Your Subject Here");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Enter Content Here Any Feedback/Bugs etc..");
                startActivity(Intent.createChooser(emailIntent, "Send mail To Dev. using..."));
            }
        });
    }
}