package com.example.patienttracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Fragment_Doctor_Availability extends Fragment {

    //variables
    private static final String TAG      = "FragmentDoctorAvailability";
    private static final String phoneKey = "phonenumber";
    private String myuserphone ;
    private final long dateToday = new Date().getTime();

    private String doctorPhone;
    private Boolean  isHalfHourSlots = false;
    private Map<String,Boolean> daysOfTheWeek = new HashMap<>();
    private Map<String,Boolean> timeSlots = new HashMap<>();

    //widgets
    private LinearLayout ll_timeSlots_hour,ll_timeSlots_half_hour;
    private CalendarView calendarView;

    private Button btn_setAvailability;

    //date range picker
    private MaterialDatePicker.Builder<Pair<Long, Long>> builderRangePicker
            = MaterialDatePicker.Builder.dateRangePicker();

    //databse
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Doctor");

    public Fragment_Doctor_Availability() {
        // Required empty public constructor
    }

    public static Fragment_Doctor_Availability newInstance(String Phone) {
        Fragment_Doctor_Availability fragment = new Fragment_Doctor_Availability();
        Bundle bundle = new Bundle();
        bundle.putString(phoneKey,Phone);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d(TAG, "onCreate: Arguments NOT null");
            myuserphone = getArguments().getString(phoneKey);
        }

    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_availability, container, false);

        btn_setAvailability = view.findViewById(R.id.B_F_DoctorA_SetAvailability);

        ll_timeSlots_hour = view.findViewById(R.id.LL_F_DoctorA_TimeButtons_Hour);
        ll_timeSlots_half_hour = view.findViewById(R.id.LL_F_DoctorA_TimeButtons_Half_hour);

        calendarView = view.findViewById(R.id.CV_F_DoctorA_Calender);
        calendarView.setMinDate((dateToday));//set CalendarView MinDate to "today"

        getAvailableTimeSlots();
        return view;
    }

    @Nullable
    @Override
    public void onStart() {
        super.onStart();

        btn_setAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Activity_Doctor_SetAvailability.class);
                intent.putExtra(phoneKey,myuserphone);
                startActivity(intent);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

            }
        });

    }

    private void getAvailableTimeSlots() {

    }
}