package com.example.finalapplication.phonebook;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalapplication.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    Activity activity;
    ArrayList<ContactModel> arrayList;

    public MainAdapter(Activity activity,ArrayList<ContactModel> arrayList){
        this.activity=activity;
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull  MainAdapter.ViewHolder holder, int position) {
        ContactModel model=arrayList.get(position);

        holder.tvName.setText(model.getName());
        holder.tvNumber.setText(model.getNumber());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(holder.tvNumber.getText().toString());
            }
        });

    }

    //TO MAKE PHONE CALL
    private void makePhoneCall(String number) {
        String dial="tel:" + number;
        activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
    }




    @Override
    public int getItemCount() {
        //Returning Array List
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvNumber;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tv_name);
            tvNumber=itemView.findViewById(R.id.tv_number);
            imageView=itemView.findViewById(R.id.iv_image);
        }
    }

}
