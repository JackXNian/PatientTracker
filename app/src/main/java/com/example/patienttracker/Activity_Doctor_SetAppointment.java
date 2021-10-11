package com.example.patienttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Activity_Doctor_SetAppointment extends AppCompatActivity {

    //variable
    private static final String TAG = "Activity Doctor Set Appointment";
    private static final String documentKey = "Appointment_Document_ID";
    private static final String datetimeKey = "Appointment_Date_Time";

    private String
            documentID_Booking,documentID_Doctor,documentID_Patient,
            name_doctor,name_patient,datetime,description,prescription;

    //widgets
    private TextView tv_name_doctor,tv_name_patient,tv_datetime;
    private EditText et_description,et_prescription;
    private Button btn_confirm;

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
//        Log.d(TAG,documentID +" -- "+datetime);
        getAppointmentDetails();

        tv_name_doctor  = findViewById(R.id.TV_A_Appointment_Doctor);
        tv_name_patient = findViewById(R.id.TV_A_Appointment_Patient);
        tv_datetime     = findViewById(R.id.TV_A_Appointment_DateTime);

        et_description  = findViewById(R.id.ET_A_Appointment_Description);
        et_prescription = findViewById(R.id.ET_A_Appointment_Prescription);

        btn_confirm     = findViewById(R.id.B_A_Appointment_Confirm);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        tv_name_doctor  .setText("Doctor : " + name_doctor);
        tv_name_patient .setText("Patient : " + name_patient);
        tv_datetime     .setText("Date & Time : " + datetime);

        et_description  .setText(description);
        et_prescription .setText(prescription);

        et_description  .addTextChangedListener(descriptionTextWatcher);
        et_prescription .addTextChangedListener(prescriptionTextWatcher);

    }

    //TextWatchers
    private TextWatcher descriptionTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            description = et_description.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    };
    private TextWatcher prescriptionTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            prescription = et_prescription.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    };

    void getAppointmentDetails(){

    }
}