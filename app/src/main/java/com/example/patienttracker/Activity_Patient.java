package com.example.patienttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.HashMap;
import java.util.Map;

public class Activity_Patient extends AppCompatActivity {

    //Variables
    public static final String TAG = "PatientActivity";

    private Map<String, Object> patientData = new HashMap<>();

    private String userPhone = "";
    private String userEmail = "";
    private String userPassword = "";
    private String userFName = "";
    private String userLName = "";
    private String userID = "";

    //Fragment
    private Fragment_Patient_Home fragment_patient_home;
    private Fragment_Patient_Lookup fragment_patient_lookup;
    private Fragment_Patient_History fragment_patient_booking;

    //Widgets
    private MeowBottomNavigation meowBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        Log.d(TAG, "onCreate: im here");
        
        //get variables from previous activity
        final Intent intent = getIntent();
        patientData = (Map<String, Object>) intent.getSerializableExtra(Activity_Patient_Login.patientDataKEY);
        userPhone = intent.getStringExtra(Activity_Patient_Login.patientPhoneKEY);
        userEmail = (String) patientData.get("Email");
        userFName = (String) patientData.get("FirstName");
        userLName = (String) patientData.get("LastName");

        //send data to Each fragment
        fragment_patient_home = Fragment_Patient_Home.newInstance(
                userFName,userLName,userPhone,userEmail);
//        fragment_patient_lookup = Fragment_Patient_Lookup.newInstance();
//        fragment_patient_booking = Fragment_Patient_Booking.newInstance();

        //Assign variable
        meowBottomNavigation = findViewById(R.id.bottom_navigation);

        //Add Menu items to Bottom Navigation
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_person));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_search));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_booking));

        //Accessing Menu items
        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                //Initialize fragment
                Fragment fragment= null;
                //Menu item selector
                switch (item.getId()){
                    case 1://When id is 1 Patient Home Page
                        loadFragment(fragment_patient_home);
//                        fragment = new Fragment_Patient_Home();
//                        loadFragment(fragment);
                        break;
                    case 2://When id is 2 Patient lookup
//                        loadFragment(fragment_patient_lookup);
                        fragment = new Fragment_Patient_Lookup();
                        loadFragment(fragment);
                        break;
                    case 3://When id is 3 Patient History
//                        loadFragment(fragment_patient_booking);
                        fragment = new Fragment_Patient_History();
                        loadFragment(fragment);
                        break;
                }
            }
        });

        //Set Patient Home Page to Default
        meowBottomNavigation.show(1,true);

        //When Select A Menu Item Do ...
        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //
            }
        });
        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        //Replace Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Activity_Patient_Login.class);
        startActivity(intent);
    }//end of onBackPressed
}