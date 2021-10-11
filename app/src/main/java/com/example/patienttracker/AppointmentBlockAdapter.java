package com.example.patienttracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentBlockAdapter extends RecyclerView.Adapter<AppointmentBlockAdapter.AppointmentViewHolder> {
    private  ArrayList<AppointmentBlock> mAppList;

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_Appointment_Date;
        public TextView tv_Name;
        public TextView tv_Number;
        public Button btn_view;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
                tv_Appointment_Date = itemView.findViewById(R.id.TV_C_Appointment_AppDate);
                tv_Name = itemView.findViewById(R.id.TV_C_Appointment_Name);
                tv_Number = itemView.findViewById(R.id.TV_C_Appointment_Number);
                btn_view = itemView.findViewById(R.id.B_C_Appointment_View);
        }
    }

    public AppointmentBlockAdapter(ArrayList<AppointmentBlock> AppHistList){
        mAppList = AppHistList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_block,parent,false);
//        AppointmentViewHolder avh = new AppointmentViewHolder(v);
        return new AppointmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentBlock current = mAppList.get(position);
        holder.tv_Appointment_Date.setText(current.getAppointmentDateTime());
        holder.tv_Name.setText(current.getName());
        holder.tv_Number.setText(current.getNumber());

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), current.getDocumentID(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }
}
