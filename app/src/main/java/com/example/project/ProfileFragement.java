package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileFragement extends Fragment {
    ImageView top1,photo;
    public ProfileFragement() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        //------Set Profile-----
        top1=v.findViewById(R.id.top1);
        photo=v.findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(in,1);
            }
        });

        TextView top2=v.findViewById(R.id.top2);
        TextView top9=v.findViewById(R.id.top9);
        TextView top7=v.findViewById(R.id.top7);
        TextView top2a=v.findViewById(R.id.top2a);
        TextView end=v.findViewById(R.id.end);
        TextView txt_pabout=v.findViewById(R.id.txt_pabout);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                top2.setText(snapshot.child("Name").getValue().toString());
                top9.setText(snapshot.child("Email").getValue().toString());
                top7.setText(snapshot.child("Name").getValue().toString());
                top2a.setText(snapshot.child("Email").getValue().toString());
                end.setText(snapshot.child("Password").getValue().toString());
                Picasso.get()
                        .load(snapshot.child("pic").getValue(String.class))
                        .placeholder(R.drawable.baseline_person_24)
                        .error(R.drawable.baseline_person_24)
                        .into(top1);
                txt_pabout.setText(snapshot.child("about").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //-----select all info about current from database------
        //----for logout-------
        TextView img_logout1=v.findViewById(R.id.img_logout1);
        img_logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent in=new Intent(getContext(),Login.class);
                startActivity(in);
            }
        });

        //change about user on click of pencil
        ImageView img_changeabout =v.findViewById(R.id.img_changeabout);
        img_changeabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                alert.setTitle("About");
                alert.setMessage("Tell me your current feeling..");
                EditText input=new EditText(getContext());
                alert.setView(input);
                alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String,Object>data=new HashMap<>();
                        data.put("about",input.getText().toString());
                     FirebaseDatabase.getInstance().getReference().child("users").
                             child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(data);
                     txt_pabout.setText(input.getText());
                     dialogInterface.dismiss();
                    }
                });
                alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                        alert.show();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1)
        {
            top1.setImageURI(data.getData());
            FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    HashMap<String,Object> data=new HashMap<>();
                                    data.put("pic",uri.toString());
                                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                            updateChildren(data);
                                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }
    }
}