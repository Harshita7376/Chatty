package com.example.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.MassegeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MassegeListAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<MassegeModel> massege;
    String userid;
    public MassegeListAdapter(Context context,ArrayList<MassegeModel> massege,String userid)
    {
     this.context=context;
     this.massege=massege;
     this.userid=userid;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if (viewType==1)
        {
            View v= LayoutInflater.from(context).inflate(R.layout.samplesendermsgdesign,parent,false);
            return new senderViewHolder(v);
        }
        else
        {
          View v=LayoutInflater.from(context).inflate(R.layout.samplereceivermsgdesign,parent,false);
          return new receiverViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        if (holder.getClass()==senderViewHolder.class)
        {
            ((senderViewHolder) holder).txt_sendermsg.setText(massege.get(position).massege);
            ((senderViewHolder) holder).txt_sendermsgtime.setText(massege.get(position).time);
           // senderViewHolder.holder.txt_sendermsg.setText(massege.get(position).massege);
            //txt_sendermsgtime.setText(massege.get(position).time);
        }
        else
        {
            ((receiverViewHolder) holder).txt_receivermsg.setText(massege.get(position).massege);
            ((receiverViewHolder) holder).txt_receivermsgtime.setText(massege.get(position).time);
           // txt_receivermsg.setText(massege.get(position).massege);
            //txt_receivermsgtime.setText(massege.get(position).time);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Configration");
                alert.setMessage("Do you want to delete this massege");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("massege").child(userid)
                                .child(massege.get(position).id).setValue(null);
                        dialogInterface.dismiss();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return massege.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (massege.get(position).senderid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return 1;

        else
            return 2;

    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        TextView txt_sendermsg,txt_sendermsgtime;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_sendermsg=itemView.findViewById(R.id.txt_sendermsg);
            txt_sendermsgtime=itemView.findViewById(R.id.txt_sendermsgtime);
        }
    }

    class receiverViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_receivermsg,txt_receivermsgtime;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_receivermsg=itemView.findViewById(R.id.txt_receivermsg);
            txt_receivermsgtime=itemView.findViewById(R.id.txt_receivermsgtime);
        }
    }
}
