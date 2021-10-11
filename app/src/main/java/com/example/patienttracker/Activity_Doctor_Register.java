package com.example.patienttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class Activity_Doctor_Register extends AppCompatActivity {

    //Variables
    private String userPhone = "";
    private String userEmail = "";
    private String userPassword = "";
    private String userFName = "";
    private String userLName = "";
    private String userID = "";
    private String userFields = "";
    private String userQualifications = "";
    private String userYears = "";
    public static String []doctor_Fields
            = {
            "Practitioner",
            "OB/GYN",
            "Psychiatrist",
            "Dentist",
            "General Surgeon",
            "Dermatologist"};

    private boolean isPhoneCorrect = false;
    private boolean isEmailCorrect = false;
    private boolean isPasswordCorrect = false;
    private boolean isFirstNameCorrect = false;
    private boolean isLastNameCorrect = false;
    private boolean isIDCorrect = false;
    private boolean isFieldsCorrect = false;
    private boolean isQualificationsCorrect = false;
    private boolean isYearsCorrect = false;

    //Widgets
    private AutoCompleteTextView tv_Fields;
    private TextInputLayout il_phone;
    private TextInputLayout il_email;
    private TextInputLayout il_password;
    private TextInputLayout il_first_name;
    private TextInputLayout il_last_name;
    private TextInputLayout il_id;
    private TextInputLayout il_fields;
    private TextInputLayout il_qualifications;
    private TextInputLayout il_years;

    private EditText et_phone;
    private EditText et_email;
    private EditText et_password;
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_id;
    private EditText et_qualifications;
    private EditText et_years;

    private Button btnConfirm;
    private Button btnCancel;

    private Dialog dialog_successful;
    private Button btnLogin;

    //databse
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        tv_Fields = findViewById(R.id.TV_A_DoctorRegister_Fields);

        ArrayAdapter arrayAdapterDoctorFields
                = new ArrayAdapter(this,R.layout.dropdown_items_doctor_feilds,doctor_Fields);
        tv_Fields.setAdapter(arrayAdapterDoctorFields);

        il_phone            = findViewById(R.id.IL_A_DoctorRegister_Phone);
        il_email            = findViewById(R.id.IL_A_DoctorRegister_Email);
        il_password         = findViewById(R.id.IL_A_DoctorRegister_Password);
        il_first_name       = findViewById(R.id.IL_A_DoctorRegister_FirstName);
        il_last_name        = findViewById(R.id.IL_A_DoctorRegister_LastName);
        il_id               = findViewById(R.id.IL_A_DoctorRegister_ID);
        il_fields           = findViewById(R.id.IL_A_DoctorRegister_Fields);
        il_qualifications   = findViewById(R.id.IL_A_DoctorRegister_Qualifications);
        il_years            = findViewById(R.id.IL_A_DoctorRegister_Years);

        et_phone            = findViewById(R.id.ET_A_DoctorRegister_Phone);
        et_email            = findViewById(R.id.ET_A_DoctorRegister_Email);
        et_password         = findViewById(R.id.ET_A_DoctorRegister_Password);
        et_first_name       = findViewById(R.id.ET_A_DoctorRegister_FirstName);
        et_last_name        = findViewById(R.id.ET_A_DoctorRegister_LastName);
        et_id               = findViewById(R.id.ET_A_DoctorRegister_ID);
        et_qualifications   = findViewById(R.id.ET_A_DoctorRegister_Qualifications);
        et_years            = findViewById(R.id.ET_A_DoctorRegister_Years);

        btnConfirm          = findViewById(R.id.B_A_DoctorRegister_Confirm);
        btnCancel           = findViewById(R.id.B_A_DoctorRegister_Back);

        et_phone            .addTextChangedListener(phoneTextWatcher);
        et_email            .addTextChangedListener(emailTextWatcher);
        et_password         .addTextChangedListener(passwordTextWatcher);
        et_first_name       .addTextChangedListener(firstnameTextWatcher);
        et_last_name        .addTextChangedListener(lastnameTextWatcher);
        et_id               .addTextChangedListener(idTextWatcher);
        tv_Fields           .addTextChangedListener(fieldsTextWatcher);
        et_qualifications   .addTextChangedListener(qualificationsTextWatcher);
        et_years            .addTextChangedListener(yearsTextWatcher);

        dialog_successful = new Dialog(this);
        dialog_successful.setContentView(R.layout.dialog_successful_register);
        dialog_successful.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnLogin = dialog_successful.findViewById(R.id.B_D_Success_Continue);

    }//end of onCreate

    @Override//Life cycle after onCreate
    public void onStart(){
        super.onStart();

        btnConfirm.setOnClickListener(view -> {
            if(validateAllEditText()){
                //If all data are valid
                Log.d("Doctor Register", "All Input Details Validated ");

                //create a doctor with given credentials
                Map<String,Object> doctor = new HashMap<>();
                doctor.put("Phone", userPhone);
                doctor.put("Email",userEmail);
                doctor.put("Password", userPassword);
                doctor.put("FirstName", userFName);
                doctor.put("LastName", userLName);
                doctor.put("ID", userID);
                doctor.put("Fields", userFields);
                doctor.put("Qualifications", userQualifications);
                doctor.put("Years", userYears);

                //create new document with phone being id
                db.collection("Doctor").document(userPhone)
                        .set(doctor)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Doctor Register", "DocumentSnapshot successfully written!");
                                openSuccessfulDialog();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Doctor Register", "Error writing document", e);
                                openFailDialog();
                            }
                        });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }//end of onStart

    private void openSuccessfulDialog() {
        dialog_successful.show();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorLogin();
            }
        });
    }

    private void openFailDialog() {

    }

    private void DoctorLogin() {
        Intent intent = new Intent(Activity_Doctor_Register.this, Activity_Doctor_Login.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        if (){
//
//        }else {
//            super.onBackPressed();
//        }
        Intent intent = new Intent(Activity_Doctor_Register.this, Activity_Doctor_Login.class);
        startActivity(intent);
    }//end of onBackPressed

    //TextWatchers
    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userPhone = et_phone.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_phone.setError((null));
            btnConfirm.setEnabled(true);
        }
    };
    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userEmail = et_email.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_email.setError((null));
            btnConfirm.setEnabled(true);
        }
    };
    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userPassword = et_password.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_password.setError((null));
            btnConfirm.setEnabled(true);
        }
    };
    private TextWatcher firstnameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userFName = et_first_name.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_first_name.setError((null));
            btnConfirm.setEnabled(true);
        }
    };
    private TextWatcher lastnameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userLName = et_last_name.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_last_name.setError((null));
            btnConfirm.setEnabled(true);
        }
    };
    private TextWatcher idTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userID = et_id.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_id.setError((null));
            btnConfirm.setEnabled(true);
        }
    };
    private TextWatcher fieldsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userFields = tv_Fields.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_fields.setError(null);
            btnConfirm.setEnabled(true);
        }
    };
    private TextWatcher qualificationsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userQualifications = et_qualifications.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_qualifications.setError((null));
            btnConfirm.setEnabled(true);
        }
    };
    private TextWatcher yearsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userYears = et_years.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            il_years.setError((null));
            btnConfirm.setEnabled(true);
        }
    };


    //Validations
    private boolean validateAllEditText(){
        validatePhone();
        validateEmail();
        validatePassword();
        validateFirstName();
        validateLastName();
        validateID();
        validateFields();
        validateQualifications();
        validateYears();
        return (isPhoneCorrect &&
                isEmailCorrect &&
                isPasswordCorrect &&
                isFirstNameCorrect &&
                isLastNameCorrect &&
                isIDCorrect &&
                isFieldsCorrect &&
                isQualificationsCorrect &&
                isYearsCorrect);
    }

    private void validatePhone(){
        isPhoneCorrect = true;
        if (userPhone.isEmpty()){
            btnConfirm.setEnabled(false);
            isPhoneCorrect = false;
            il_phone.setError("Empty");
            il_phone.requestFocus();
        }else if(userPhone.length()<10){
            btnConfirm.setEnabled(false);
            isPhoneCorrect = false;
            il_phone.setError("Incomplete Phone Number");
            il_phone.requestFocus();
        }else if(userPhone.length()>15){
            btnConfirm.setEnabled(false);
            isPhoneCorrect = false;
            il_phone.setError(("Maximum Length Exceeded"));
            il_phone.requestFocus();
        }
    }

    private void validateEmail() {
        isEmailCorrect = true;
        if (userEmail.isEmpty()) {
            btnConfirm.setEnabled(false);
            isEmailCorrect = false;
            il_email.setError("Empty");
            il_email.requestFocus();
        }
    }

    private void validatePassword() {
        isPasswordCorrect = true;
        if (userPassword.isEmpty()) {
            btnConfirm.setEnabled(false);
            isPasswordCorrect = false;
            il_password.setError("Empty");
            il_password.requestFocus();
        }
    }

    private void validateFirstName() {
        isFirstNameCorrect = true;
        if (userFName.isEmpty()) {
            btnConfirm.setEnabled(false);
            isFirstNameCorrect = false;
            il_first_name.setError("Empty");
            il_first_name.requestFocus();
        }
    }

    private void validateLastName() {
        isLastNameCorrect = true;
        if (userLName.isEmpty()) {
            btnConfirm.setEnabled(false);
            isLastNameCorrect = false;
            il_last_name.setError("Empty");
            il_last_name.requestFocus();
        }
    }

    private void validateID() {
        isIDCorrect = true;
        if (userID.isEmpty()) {
            btnConfirm.setEnabled(false);
            isIDCorrect = false;
            il_id.setError("Empty");
            il_id.requestFocus();
        }
    }

    private void validateFields() {
        isFieldsCorrect = true;
        if (userFields.isEmpty()) {
            btnConfirm.setEnabled(false);
            isFieldsCorrect = false;
            il_fields.setError("Empty");
            il_fields.requestFocus();
        }
    }

    private void validateQualifications() {
        isQualificationsCorrect = true;
        if (userQualifications.isEmpty()) {
            btnConfirm.setEnabled(false);
            isQualificationsCorrect = false;
            il_qualifications.setError("Empty");
            il_qualifications.requestFocus();
        }
    }

    private void validateYears() {
        isYearsCorrect = true;
        if (userYears.isEmpty()) {
            btnConfirm.setEnabled(false);
            isYearsCorrect = false;
            il_years.setError("Empty");
            il_years.requestFocus();
        }
    }

}