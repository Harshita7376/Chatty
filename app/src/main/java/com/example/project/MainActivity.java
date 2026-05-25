package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView txtlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtlogin=findViewById(R.id.txt_login);
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lo=new Intent(MainActivity.this,Login.class);
                startActivity(lo);
            }
        });
        //-----click here to resister
        TextView imgregister=findViewById(R.id.register);
        imgregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   FirebaseDatabase.getInstance().getReference().setValue("Techpile Technology");
                //  Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_LONG).show();\
                EditText txtname=findViewById(R.id.txtname);
                EditText txtemail=findViewById(R.id.txtemail);
                EditText txtpass=findViewById(R.id.txtpass);
                String name=txtname.getText().toString();
                String email=txtemail.getText().toString();
                String pass=txtpass.getText().toString();

             //  FirebaseDatabase.getInstance().getReference().setValue((name+email+pass+""));
                if (!name.isEmpty() && !email.isEmpty() &&  !pass.isEmpty()) {
                    ProgressDialog dialog=new ProgressDialog(MainActivity.this);
                            dialog.setTitle("Please wait...");
                            dialog.show();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                HashMap<String, String> user = new HashMap<>();
                                user.put("Name", name);
                                user.put("Email", email);
                                user.put("Password", pass);
                                FirebaseDatabase.getInstance().getReference().child("users").
                                        child(uid).setValue(user) ;

                                Toast.makeText(MainActivity.this, "Now you are registered", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // Toast.makeText(MainActivity.this, name+email+pass+gender+"", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}