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

public class Activity_Doctor_Login extends AppCompatActivity {

    //Variables
    private String enteredPhone = "";
    private String enteredPassword = "";

    private boolean isPhoneCorrect = false;
    private boolean isPasswordCorrect = false;

    private Map<String, Object> doctorData = new HashMap<>();

    public static final String TAG = "DoctorLoginActivity";
    public final static String doctorDataKEY  = "nx971202";
    public final static String doctorPhoneKEY = "DoctorPhone";

    //Widgets
    private Button btn_Login;
    private Button btn_Register;
    private Button btn_Forgot;

    private TextInputLayout il_phone;
    private TextInputLayout il_password;

    private EditText et_phone;
    private EditText et_password;

//    private TextView testing;

    //databse
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        il_phone        = findViewById(R.id.IL_A_DoctorLogin_Phone);
        il_password     = findViewById(R.id.IL_A_DoctorLogin_Password);

        et_phone        = findViewById(R.id.ET_A_DoctorLogin_Phone);
        et_password     = findViewById(R.id.ET_A_DoctorLogin_Password);

        et_phone        .addTextChangedListener(phoneTextWatcher);
        et_password     .addTextChangedListener(passwordTextWatcher);

        btn_Login       = findViewById(R.id.B_A_DoctorLogin_Login);
        btn_Register    = findViewById(R.id.B_A_DoctorLogin_Register);
        btn_Forgot      = findViewById(R.id.B_A_DoctorLogin_Forget);

//        testing = findViewById(R.id.testtext);

    }//end of onCreate

    @Override//Life cycle after onCreate
    protected void onStart() {
        super.onStart();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorLogin();
            }
        });
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorRegister();
            }
        });
        btn_Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorForgot();
            }
        });

        et_phone.setText("0824630844");
        et_password.setText("971202");
    }

    //Buttons
    private void DoctorLogin() {
        validateLoginDetail();
    }
    private void DoctorRegister() {
        Intent intent = new Intent(this, Activity_Doctor_Register.class);
        startActivity(intent);
    }
    private void DoctorForgot() {

    }
    private void ClearAll(){
        enteredPhone = "";
        enteredPassword = "";

        isPhoneCorrect = false;
        isPasswordCorrect = false;

        doctorData = new HashMap<>();

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
        //search for an doctor with that phone number entered
        db.collection("Doctor").document(enteredPhone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    //doctor found with the entered phone number
                    doctorData = documentSnapshot.getData();
                    validateOnlinePassword(doctorData);
                }else {
                    //no doctor with that phone number found
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
            Intent intent = new Intent(this, Activity_Doctor.class);
            //passing values to user activity
            intent.putExtra(doctorDataKEY, (Serializable) data);
            intent.putExtra(doctorPhoneKEY,enteredPhone);
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