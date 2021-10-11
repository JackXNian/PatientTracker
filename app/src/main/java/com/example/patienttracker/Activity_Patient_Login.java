package com.example.patienttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Activity_Patient_Login extends AppCompatActivity {

    //Variables
    private String enteredPhone = "";
    private String enteredPassword = "";

    private boolean isPhoneCorrect = false;
    private boolean isPasswordCorrect = false;

    private Map<String, Object> patientData = new HashMap<>();

    public static final String TAG = "PatientLoginActivity";
    public final static String patientDataKEY        = "971202xn";
    public final static String patientPhoneKEY = "PatientPhone";

    //Widgets
    private Button btn_Login;
    private Button btn_Register;
    private Button btn_Forgot;

    private TextInputLayout il_phone;
    private TextInputLayout il_password;

    private EditText et_phone;
    private EditText et_password;

    //databse
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        il_phone        = findViewById(R.id.IL_A_PatientLogin_Phone);
        il_password     = findViewById(R.id.IL_A_PatientLogin_Password);

        et_phone        = findViewById(R.id.ET_A_PatientLogin_Phone);
        et_password     = findViewById(R.id.ET_A_PatientLogin_Password);

        et_phone        .addTextChangedListener(phoneTextWatcher);
        et_password     .addTextChangedListener(passwordTextWatcher);

        btn_Login = findViewById(R.id.B_A_PatientLogin_Login);
        btn_Register = findViewById(R.id.B_A_PatientLogin_Register);
        btn_Forgot = findViewById(R.id.B_A_PatientLogin_Forget);


    }//end of onCreate

    @Override//Life cycle after onCreate
    protected void onStart() {
        super.onStart();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientLogin();
            }
        });
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientRegister();
            }
        });
        btn_Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientForgot();
            }
        });

        et_phone.setText("0824630844");
        et_password.setText("abcd");
    }

    private void PatientLogin() {
        validateLoginDetail();
    }

    private void PatientRegister() {
        Intent intent = new Intent(this, Activity_Patient_Register.class);
        startActivity(intent);
    }

    private void PatientForgot() {
    }

    private void ClearAll(){
        enteredPhone = "";
        enteredPassword = "";

        isPhoneCorrect = false;
        isPasswordCorrect = false;

        patientData = new HashMap<>();

        et_phone.setText("");
        et_password.setText("");
        il_phone.clearFocus();
        il_password.clearFocus();
    }

    //TextWatchers
    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            enteredPhone = et_phone.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_phone.setError((null));
            btn_Login.setEnabled(true);
        }
    };
    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            enteredPassword = et_password.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_password.setError((null));
            btn_Login.setEnabled(true);
        }
    };

    //Validation
    private boolean validateLoginDetail() {
        if ( validatePhoneNumber() && validatePassword() )
        {
            validateOnline();
        }

        return false;
    }

    private boolean validatePhoneNumber(){

        if (enteredPhone.isEmpty()){
            btn_Login.setEnabled(false);
            isPhoneCorrect = false;
            il_phone.setError("Empty");
            il_phone.requestFocus();
        }else {
            isPhoneCorrect = true;
        }
        return  isPhoneCorrect;
    }

    private boolean validatePassword() {

        if (enteredPassword.isEmpty()){
            btn_Login.setEnabled(false);
            isPasswordCorrect=false;
            il_password.setError("Empty");
            il_password.requestFocus();
        }else {
            isPasswordCorrect = true;
        }
        return isPasswordCorrect;
    }

    private void validateOnline(){
        //search for an patient with that phone number entered
        db.collection("Patient").document(enteredPhone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            //patient found with the entered phone number
                            patientData = documentSnapshot.getData();

                            validateOnlinePassword(patientData);
                        }else {
                            //no patient with that phone number found
                            btn_Login.setEnabled(false);
                            isPhoneCorrect = false;
                            il_phone.setError("Phone Number Does Not Exists");
                            il_phone.requestFocus();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void validateOnlinePassword(Map<String, Object> data){
        String userPassword = (String) data.get("Password");

        if (enteredPassword.equals(userPassword)){
//            Log.d(TAG, "validateOnlinePassword: im here");
            Intent intent = new Intent(this, Activity_Patient.class);
            //passing values to user activity
            intent.putExtra(patientDataKEY, (Serializable) data);
            intent.putExtra(patientPhoneKEY,enteredPhone);
            startActivity(intent);
            ClearAll();
        }else {
            btn_Login.setEnabled(false);
            isPasswordCorrect=false;
            il_password.setError("Incorrect Password");
            il_password.requestFocus();
        }
    }

    @Override
    public void onBackPressed() {
//        if (){
//
//        }else {
//            super.onBackPressed();
//        }
        Intent intent = new Intent(this, Activity_Main.class);
        startActivity(intent);
    }//end of onBackPressed

}