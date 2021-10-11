package com.example.patienttracker;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Note_Appointment_Doctor extends RecyclerView.Adapter<Adapter_Note_Appointment_Doctor.AppointmentViewHolder> {
    private ArrayList<Note_Appointment> mAppList;
    private String documentID,name,datetime,number;

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

    public Adapter_Note_Appointment_Doctor(ArrayList<Note_Appointment> AppHistList){
        mAppList = AppHistList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_appointment_card,parent,false);
//        AppointmentViewHolder avh = new AppointmentViewHolder(v);
        return new AppointmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Note_Appointment current = mAppList.get(position);
        documentID = current.getDocumentID();
        datetime = current.getAppointmentDateTime();
        name = current.getName();
        number = current.getNumber();

        holder.tv_Appointment_Date.setText(datetime);
        holder.tv_Name.setText(name);
        holder.tv_Number.setText(number);
        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), current.getDocumentID(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), Activity_Doctor_SetAppointment.class);
                intent.putExtra("Appointment_Document_ID",documentID);
                intent.putExtra("Appointment_Date_Time",datetime);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }
}
