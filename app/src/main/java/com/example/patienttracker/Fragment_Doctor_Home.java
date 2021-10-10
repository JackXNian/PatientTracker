package com.example.patienttracker;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Fragment_Doctor_Home extends Fragment {

    //variables
    private static final String TAG                 = "FragmentDoctorHome";

    private static final String firstNameKey        = "firstname";
    private static final String lastNameKey         = "lastname";
    private static final String phoneKey            = "phonenumber";
    private static final String emailKey            = "emailaddress";
    private static final String fieldsKey           = "fields";
    private static final String qualificationsKey   = "qulifications";
    private static final String yearsKey            = "yearsofexperience";

    private String myuserfname ;
    private String myuserlname ;
    private String myuserphone ;
    private String myuseremail ;
    private String myuserfield ;
    private String myuserquali ;
    private String myuseryears ;

    //widgets
    private TextView textViewUserFname ;
    private TextView textViewUserLname ;
    private TextView textViewUserphone ;
    private TextView textViewUseremail ;
    private TextView textViewUserfield ;
    private TextView textViewUserquali ;
    private TextView textViewUseryears ;
    private TextView textViewUserUserID ;

    public Fragment_Doctor_Home() {
        // Required empty public constructor
    }

    public static Fragment_Doctor_Home newInstance(
            String FName,String LName,String Phone,String Email,
            String Fields,String Qualifications, String Years) {
        Fragment_Doctor_Home fragment = new Fragment_Doctor_Home();
        Bundle bundle = new Bundle();
        bundle.putString(firstNameKey,FName);
        bundle.putString(lastNameKey,LName);
        bundle.putString(phoneKey,Phone);
        bundle.putString(emailKey,Email);
        bundle.putString(fieldsKey,Fields);
        bundle.putString(qualificationsKey,Qualifications);
        bundle.putString(yearsKey,Years);

        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d(TAG, "onCreate: Arguments NOT null");
            myuserfname = getArguments().getString(firstNameKey);
            myuserlname = getArguments().getString(lastNameKey);
            myuserphone = getArguments().getString(phoneKey);
            myuseremail = getArguments().getString(emailKey);
            myuserfield = getArguments().getString(fieldsKey);
            myuserquali = getArguments().getString(qualificationsKey);
            myuseryears = getArguments().getString(yearsKey);
        }
    }//End of onCrete

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override  //Cycle After onCrete
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor, container, false);
        textViewUserFname = view.findViewById(R.id.TV_F_DoctorH_First_Name);
        textViewUserLname = view.findViewById(R.id.TV_F_DoctorH_Last_Name);
        textViewUserphone = view.findViewById(R.id.TV_F_DoctorH_Phone);
        textViewUseremail = view.findViewById(R.id.TV_F_DoctorH_Email);
        textViewUserfield = view.findViewById(R.id.TV_F_DoctorH_Fields);
        textViewUserquali = view.findViewById(R.id.TV_F_DoctorH_Qualifications);
        textViewUseryears = view.findViewById(R.id.TV_F_DoctorH_Years);
        textViewUserUserID = view.findViewById(R.id.TV_F_DoctorH_userID);

        textViewUserFname.setText(myuserfname);
        textViewUserLname.setText(myuserlname);
        textViewUserphone.setText(myuserphone);
        textViewUseremail.setText(myuseremail);
        textViewUserfield.setText(myuserfield);
        textViewUserquali.setText(myuserquali);
        textViewUseryears.setText(myuseryears + " Years");
        textViewUserUserID.setText("Login ID : " + myuserphone);

        return view;
    }//end of onCreteView

    @SuppressLint("SetTextI18n")
    @Override  //Cycle After onCreteView
    public void onStart() {
        super.onStart();


    }//end of onStart

}