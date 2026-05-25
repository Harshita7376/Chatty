package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class StartedPage extends AppCompatActivity {
    TextView start,signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started_page);
        start = findViewById(R.id.getstart);
        signUp=findViewById(R.id.signUp);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent in=new Intent(StartedPage.this,Login.class);
            startActivity(in);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(StartedPage.this,MainActivity.class);
                startActivity(in);
            }
        });
    }
    protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent=new Intent(this,Ragisteration.class);
            startActivity(intent);
        }
    }
}