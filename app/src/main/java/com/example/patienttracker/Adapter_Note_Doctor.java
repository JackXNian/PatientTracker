package com.example.patienttracker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class Adapter_Note_Doctor extends FirestoreRecyclerAdapter<Note_Doctor, Adapter_Note_Doctor.Holder_Note_Doctor_Info> {
    private OnItemClickListener onItemClickListener;

    public Adapter_Note_Doctor(@NonNull FirestoreRecyclerOptions<Note_Doctor> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull Holder_Note_Doctor_Info holder, int position, @NonNull Note_Doctor model) {
        holder.tv_Name.setText(model.getFirstName() + " " + model.getLastName());
        holder.tv_Number.setText("Tel : " + model.getPhone());
        holder.tv_qualification.setText("Qualification : " + model.getQualifications());
        holder.tv_Years.setText("Years of experience : " + model.getYears());
    }

    @NonNull
    @Override
    public Holder_Note_Doctor_Info onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_doctor_card,parent,false);
        return new Holder_Note_Doctor_Info(view);
    }

    class Holder_Note_Doctor_Info extends RecyclerView.ViewHolder{
        TextView tv_Name, tv_Number, tv_qualification, tv_Years;

        public Holder_Note_Doctor_Info(View itemView){
            super(itemView);
            tv_Name          = itemView.findViewById(R.id.TV_C_DoctorInfo_Name);
            tv_Number        = itemView.findViewById(R.id.TV_C_DoctorInfo_Number);
            tv_qualification = itemView.findViewById(R.id.TV_C_DoctorInfo_Qualification);
            tv_Years         = itemView.findViewById(R.id.TV_C_DoctorInfo_Years);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    //NO_POSITION is for deleted records
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null){
                        onItemClickListener
                                .onItemClick(getSnapshots().getSnapshot(position),position );
                    }
                }
            });
        }
    }

    public interface  OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
