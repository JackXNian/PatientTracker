package com.example.patienttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Activity_Patient_ViewAppointment extends AppCompatActivity {

    //variable
    private static final String TAG = "Activity Doctor Set Appointment";
    private static final String documentKey = "Appointment_Document_ID";
    private static final String datetimeKey = "Appointment_Date_Time";

    private String
            documentID_Booking, documentID_Doctor, documentID_Patient,
            datetime, description, prescription;

    private Map<String, Object> patientData ;

    //widgets
    private TextView tv_name_doctor,tv_name_patient,tv_datetime,tv_doctor_field;
    private EditText et_description,et_prescription;
    private Button btn_back;

    private Dialog dialog_successful;
    private Button btn_continue;

    //database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionDoctorReference = db.collection("Doctor");
    private CollectionReference collectionBookingReference = db.collection("Booking");
    private CollectionReference collectionPatientReference = db.collection("Patient");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_appointment);


        //get variables
        final Intent intent = getIntent();
        documentID_Booking = intent.getStringExtra(documentKey);
        datetime    = intent.getStringExtra(datetimeKey);

        tv_name_doctor  = findViewById(R.id.TV_A_Appointment_View_Doctor);
        tv_name_patient = findViewById(R.id.TV_A_Appointment_View_Patient);
        tv_datetime     = findViewById(R.id.TV_A_Appointment_View_DateTime);
        tv_doctor_field = findViewById(R.id.TV_A_Appointment_View_Doctor_Field);

        et_description  = findViewById(R.id.ET_A_Appointment_View_Description);
        et_prescription = findViewById(R.id.ET_A_Appointment_View_Prescription);

        btn_back = findViewById(R.id.B_A_Appointment_View_Confirm);

        dialog_successful = new Dialog(this);
        dialog_successful.setContentView(R.layout.dialog_successful_upload);
        dialog_successful.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_continue = dialog_successful.findViewById(R.id.B_D_Success_Continue);

        getAppointmentDetails();
        getAppointmentForm();

    }

    @Override
    protected void onStart() {
        super.onStart();

        tv_datetime     .setText("Date & Time : " + datetime);

        btn_back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void getAppointmentDetails(){
        collectionBookingReference
                .document(documentID_Booking)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note_Booking note = documentSnapshot.toObject(Note_Booking.class);
                        assert note != null;
                        documentID_Doctor = note.getDoctor_documentID();
                        documentID_Patient = note.getPatient_documentID();
                        getNames();
                    }
                });
    }

    public void  getNames(){
        collectionDoctorReference
                .document(documentID_Doctor)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note_Doctor note = documentSnapshot.toObject(Note_Doctor.class);
                        assert note != null;
                        tv_name_doctor.setText("Doctor : " + note.getFirstName() + " " + note.getLastName());
                        tv_doctor_field.setText("Doctor Field : " + note.getFields());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tv_name_doctor.setText("ERROR GETTING DOCTOR NAME");
                    }
                });

        collectionPatientReference
                .document(documentID_Patient)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note_Patient note = documentSnapshot.toObject(Note_Patient.class);
                        assert note != null;
                        tv_name_patient .setText("Patient : " + note.getFirstName() + " " + note.getLastName());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tv_name_patient .setText("ERROR GETTING PATIENT NAME");
                    }
                });
    }

    private void getAppointmentForm(){
        collectionBookingReference.document(documentID_Booking)
                .collection("SubCollection").document("Form")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note_Appointment_Form note = documentSnapshot.toObject(Note_Appointment_Form.class);
                        if (note != null){
                            Log.d(TAG, "Description : " + note.getDescription() + " : " + note.getPrescription());
                            et_description.setText(note.getDescription());
                            et_prescription.setText(note.getPrescription());
                        }else{
                            et_description.setText("seems like doctor hasn't upload this information");
                            et_prescription.setText("seems like doctor hasn't upload this information");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: failed getting appointment form");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}