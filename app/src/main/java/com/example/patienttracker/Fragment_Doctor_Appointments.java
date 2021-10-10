package com.example.patienttracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment_Doctor_Appointments extends Fragment {

    public Fragment_Doctor_Appointments() {
        // Required empty public constructor
    }

    public static Fragment_Doctor_Appointments newInstance() {
        Fragment_Doctor_Appointments fragment = new Fragment_Doctor_Appointments();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_appointments, container, false);
    }
}