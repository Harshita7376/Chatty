
package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.model.MassegeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
public class Massenger extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massenger);
        Intent in=getIntent();
        String name=in.getStringExtra("name");
        String receiveruid=in.getStringExtra("uid");
        String senderuid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ImageView profile=findViewById(R.id.profile_image);
        Picasso.get()
                .load(in.getStringExtra("pic"))
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(profile);
        TextView txt_receivername=findViewById(R.id.txt_receivername);
        txt_receivername.setText(name);

        EditText et_mymassege=findViewById(R.id.et_mymassege);
        ImageView img_sendmassege=findViewById(R.id.img_sendmassege);

        //-----btnsend massege click event-------
        img_sendmassege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mymassege=et_mymassege.getText().toString();
                if(!mymassege.isEmpty())
                {
                    HashMap<String,String> massege=new HashMap<>();
                    massege.put("msg",mymassege);
                    massege.put("senderid",senderuid);
                    SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat time=new SimpleDateFormat("hh:mm aa");
                    massege.put("date",date.format(new Date()));
                    massege.put("time", time.format(new Date()));
                    FirebaseDatabase.getInstance().getReference().child("massege").child(senderuid+receiveruid).push().setValue(massege);
                    FirebaseDatabase.getInstance().getReference().child("massege").

                            child(receiveruid+senderuid).push().setValue(massege);
                    et_mymassege.setText("");


                }
                else {
                    Toast.makeText(Massenger.this,"Don't click me,without msg",Toast.LENGTH_LONG).show();
                }
            }
        });
        //------back button------
        ImageView img_back1=findViewById(R.id.img_back1);
        img_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Massenger.super.onBackPressed();
            }
        });
        //----select all massege between sender+receiver and bind in Recylcerview
        ArrayList<MassegeModel> massege=new ArrayList<>();
        RecyclerView recycler_msg=findViewById(R.id.recycler_msg);
        MassegeListAdapter adapter=new MassegeListAdapter(this,massege,senderuid+receiveruid);
        recycler_msg.setAdapter(adapter);
        recycler_msg.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase.getInstance().getReference().child("massege").child(senderuid+receiveruid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        massege.clear();
                        for (DataSnapshot data:snapshot.getChildren())
                        {
                            MassegeModel m=new MassegeModel();
                            m.id=data.getKey();
                            m.massege=data.child("msg").getValue(String.class);
                            m.senderid=data.child("senderid").getValue(String.class);
                            m.date=data.child("date").getValue(String.class);
                            m.time=data.child("time").getValue(String.class);
                            massege.add(m);
                        }
                        adapter.notifyDataSetChanged();
                        if (massege.size()>3)
                            recycler_msg.scrollToPosition(massege.size()-1);
                        Toast.makeText(Massenger.this,massege.size()+"",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void logout(View v)
    {
        FirebaseAuth.getInstance().signOut();
        Intent lo=new Intent(this,Login.class);
        startActivity(lo);
    }
}