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
        public TextView tvAppDate;
        public TextView tvDocName;
        public TextView tvDocNum;
        public Button btn_view;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
                tvAppDate = itemView.findViewById(R.id.TV_C_Appointment_AppDate);
                tvDocName = itemView.findViewById(R.id.TV_C_Appointment_DoctorName);
                tvDocNum = itemView.findViewById(R.id.TV_C_Appointment_DoctorNumber);
                btn_view = itemView.findViewById(R.id.B_C_Appointment_View);

                btn_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Hello Java point",Toast.LENGTH_SHORT).show();
                    }
                });
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
        holder.tvAppDate.setText(current.getAppointmentDate());
        holder.tvDocName.setText(current.getDoctorName());
        holder.tvDocNum.setText(current.getDoctorNum());

    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }
}
