package com.example.patienttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Map;

public class Activity_Doctor_SetAppointment extends AppCompatActivity {

    //variable
    private static final String TAG = "Activity Doctor Set Appointment";
    private static final String documentKey = "Appointment_Document_ID";
    private static final String datetimeKey = "Appointment_Date_Time";

    private String
            documentID_Booking, documentID_Doctor, documentID_Patient,
            datetime, description, prescription;

    private Map<String, Object> doctorData ;

    //widgets
    private TextView tv_name_doctor,tv_name_patient,tv_datetime,tv_doctor_field;
    private EditText et_description,et_prescription;
    private Button btn_confirm;

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
        setContentView(R.layout.activity_doctor_set_appointment);

        //get variables
        final Intent intent = getIntent();
        documentID_Booking = intent.getStringExtra(documentKey);
        datetime    = intent.getStringExtra(datetimeKey);

        tv_name_doctor  = findViewById(R.id.TV_A_Appointment_Doctor);
        tv_name_patient = findViewById(R.id.TV_A_Appointment_Patient);
        tv_datetime     = findViewById(R.id.TV_A_Appointment_DateTime);
        tv_doctor_field = findViewById(R.id.TV_A_Appointment_Doctor_Field);

        et_description  = findViewById(R.id.ET_A_Appointment_Description);
        et_prescription = findViewById(R.id.ET_A_Appointment_Prescription);

        btn_confirm     = findViewById(R.id.B_A_Appointment_Confirm);

//        Toast.makeText(getApplicationContext(),documentID_Booking,Toast.LENGTH_SHORT).show();

        dialog_successful = new Dialog(this);
        dialog_successful.setContentView(R.layout.dialog_successful_upload);
        dialog_successful.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_continue = dialog_successful.findViewById(R.id.B_D_Success_Continue);

        getAppointmentDetails();
        getAppointmentForm();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        tv_datetime     .setText("Date & Time : " + datetime);

        et_description  .addTextChangedListener(descriptionTextWatcher);
        et_prescription .addTextChangedListener(prescriptionTextWatcher);

        btn_confirm.setOnClickListener(v -> {
            setAppointmentFrom();
        });

    }

    //TextWatchers
    private TextWatcher descriptionTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            description = et_description.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    };
    private TextWatcher prescriptionTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            prescription = et_prescription.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    };

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
                        doctorData = documentSnapshot.getData();
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

    private void setAppointmentFrom(){
        Note_Appointment_Form note = new Note_Appointment_Form(description, prescription);

        collectionBookingReference.document(documentID_Booking)
                .collection("SubCollection").document("Form")
                .set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        openSuccessfulDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        openFailDialog();
                    }
                });
    }
    private void getAppointmentForm(){
        collectionBookingReference.document(documentID_Booking)
                .collection("SubCollection").document("Form")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note_Appointment_Form note = documentSnapshot.toObject(Note_Appointment_Form.class);
                        if (note != null){
                            et_description.setText(note.getDescription());
                            et_prescription.setText(note.getPrescription());
                        }else{
                            Log.d(TAG, "onSuccess: note = null");
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

    private void openSuccessfulDialog() {
        dialog_successful.show();
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToDoctorPage();
            }
        });
    }
    private void openFailDialog() {

    }
    private void backToDoctorPage(){
        Intent intent = new Intent(this, Activity_Doctor.class);
        //passing values to user activity
        intent.putExtra(Activity_Doctor_Login.doctorDataKEY, (Serializable) doctorData);
        intent.putExtra(Activity_Doctor_Login.doctorPhoneKEY,documentID_Doctor);
        startActivity(intent);
    }


}