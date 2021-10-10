package com.example.patienttracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentBlockAdapter extends RecyclerView.Adapter<AppointmentBlockAdapter.AppointmentViewHolder> {
    private  ArrayList<AppointmentBlock> mAppList;

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder{
        public TextView tvAppDate;
        public TextView tvDocName;
        public TextView tvDocNum;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
                tvAppDate = itemView.findViewById(R.id.TV_AppDate);
                tvDocName = itemView.findViewById(R.id.TV_DoctorName);
                tvDocNum = itemView.findViewById(R.id.TV_DoctorNumber);
        }
    }

    public AppointmentBlockAdapter(ArrayList<AppointmentBlock> AppHistList){
        mAppList = AppHistList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_block,parent,false);
        AppointmentViewHolder avh = new AppointmentViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentBlock current = mAppList.get(position);
        holder.tvAppDate.setText(current.getAppointmentDate());
        holder.tvDocName.setText(current.getDoctorName());
        holder.tvDocNum.setText(current.getDoctorNum());


    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }
}
