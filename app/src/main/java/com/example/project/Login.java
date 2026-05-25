package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView txtregister, img_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtregister = findViewById(R.id.txtregister);
        img_login = findViewById(R.id.img_login);
        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pre = new Intent(Login.this,MainActivity.class);
                startActivity(pre);
            }
        });
//-------------click here to login-----------

                EditText loginemail = findViewById(R.id.login_email);
                EditText loginpass = findViewById(R.id.login_pass);


                img_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = loginemail.getText().toString();
                        String pass = loginpass.getText().toString();
                        if (!email.isEmpty() && !pass.isEmpty())
                        {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).
                                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Intent in = new Intent(Login.this, Ragisteration.class);
                                                startActivity(in);
                                            } else {
                                                Toast.makeText(Login.this, "Email Id and Password Invalid", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Login.this, "Please fill all fields properly", Toast.LENGTH_LONG).show();
                        }
                //        Intent reg = new Intent(Login.this, Ragisteration.class);
                  //      startActivity(reg);

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
