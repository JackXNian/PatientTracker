package com.example.patienttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Activity_Doctor_SetAvailability extends AppCompatActivity {

    private static final String TAG = "Activity Doctor Set Availability";

    //variables
    private static final String phoneKey = "phonenumber";

    private Boolean
            monday = false, tuesday = false, wednesday = false, thursday = false,
            friday = false, saturday = false, sunday = false;
    private String doctorPhone;
    private Boolean  isHalfHourSlots = false;
    private Map<String,Boolean> daysOfTheWeek = new HashMap<>();
    private Map<String,Boolean> timeSlots = new HashMap<>();
    private Map<String, Object> doctorData = new HashMap<>();

    //widgets
    private Button btn_confirm;
    private Button
            btn_time01, btn_time02, btn_time03, btn_time04,
            btn_time05, btn_time06, btn_time07, btn_time08,
            btn_time09, btn_time10, btn_time11, btn_time12,
            btn_time13, btn_time14, btn_time15, btn_time16,
            btn_time17, btn_time18, btn_time19, btn_time20,
            btn_time21, btn_time22, btn_time23, btn_time24,
            btn_time25, btn_time26, btn_time27, btn_time28,
            btn_time29, btn_time30, btn_time31, btn_time32,
            btn_time33, btn_time34, btn_time35, btn_time36,
            btn_time37, btn_time38, btn_time39, btn_time40,
            btn_time41, btn_time42, btn_time43, btn_time44,
            btn_time45, btn_time46, btn_time47, btn_time48;
    private SwitchMaterial switch_monday,switch_tuesday,switch_wednesday,
            switch_thursday, switch_friday, switch_saturday, switch_sunday,
            switch_timeSlot;
    private TextView tv_timeSlot;

    private LinearLayout ll_time_slots_hour, ll_time_slots_half_hour;

    private Dialog dialog_successful;
    private Button btn_continue;

    //databse
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Doctor");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_set_availability);

        Intent intent = getIntent();
        doctorPhone = intent.getStringExtra(phoneKey);

        switch_monday = findViewById(R.id.S_A_DoctorSetA_Monday);
        switch_tuesday = findViewById(R.id.S_A_DoctorSetA_Tuesday);
        switch_wednesday = findViewById(R.id.S_A_DoctorSetA_Wednesday);
        switch_thursday = findViewById(R.id.S_A_DoctorSetA_Thursday);
        switch_friday = findViewById(R.id.S_A_DoctorSetA_Friday);
        switch_saturday = findViewById(R.id.S_A_DoctorSetA_Saturday);
        switch_sunday = findViewById(R.id.S_A_DoctorSetA_Sunday);

        switch_timeSlot = findViewById(R.id.S_A_DoctorSetA_TimeSlot);
        tv_timeSlot = findViewById(R.id.TV_A_DoctorSetA_WorkTimeSlots);

        ll_time_slots_hour = findViewById(R.id.LL_A_DoctorSetA_TimeButtons_Hour);
        ll_time_slots_half_hour = findViewById(R.id.LL_A_DoctorSetA_TimeButtons_Half_Hour);

        btn_confirm = findViewById(R.id.B_A_DoctorSetA_Confirm);

        dialog_successful = new Dialog(this);
        dialog_successful.setContentView(R.layout.dialog_successful_update);
        dialog_successful.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_continue = dialog_successful.findViewById(R.id.B_D_Success_Continue);

        getDoctorDate();

        setHourTimeSlots();
        getAvailability();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        daysOfTheWeek.put("monday",monday);
        daysOfTheWeek.put("tuesday",tuesday);
        daysOfTheWeek.put("wednesday",wednesday);
        daysOfTheWeek.put("thursday",thursday);
        daysOfTheWeek.put("friday",friday);
        daysOfTheWeek.put("saturday",saturday);
        daysOfTheWeek.put("sunday",sunday);

        switch_monday.setOnClickListener(view -> {
            monday = switch_monday.isChecked();
            daysOfTheWeek.put("monday",monday);
        });
        switch_tuesday.setOnClickListener(view -> {
            tuesday = switch_tuesday.isChecked();
            daysOfTheWeek.put("tuesday",tuesday);
        });
        switch_wednesday.setOnClickListener(view -> {
            wednesday = switch_wednesday.isChecked();
            daysOfTheWeek.put("wednesday",wednesday);
        });
        switch_thursday.setOnClickListener(view -> {
            thursday = switch_thursday.isChecked();
            daysOfTheWeek.put("thursday",thursday);
        });
        switch_friday.setOnClickListener(view -> {
            friday = switch_friday.isChecked();
            daysOfTheWeek.put("friday",friday);
        });
        switch_saturday.setOnClickListener(view -> {
            saturday = switch_saturday.isChecked();
            daysOfTheWeek.put("saturday",saturday);
        });
        switch_sunday.setOnClickListener(view -> {
            sunday = switch_sunday.isChecked();
            daysOfTheWeek.put("sunday",sunday);
        });


        switch_timeSlot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isHalfHourSlots = switch_timeSlot.isChecked();
                if (isHalfHourSlots){
                    tv_timeSlot.setText("Time Slots Interval : 30 minutes");
                    ll_time_slots_half_hour.setVisibility(View.VISIBLE);
                    ll_time_slots_hour.setVisibility(View.GONE);
                    setHalfHourTimeSlots();
                } else{
                    tv_timeSlot.setText("Time Slots Interval : 60 minutes");
                    ll_time_slots_half_hour.setVisibility(View.GONE);
                    ll_time_slots_hour.setVisibility(View.VISIBLE);
                    setHourTimeSlots();
                }
            }
        });

        btn_confirm.setOnClickListener(view -> {
            setAvailability();
        });

    }

    public void setHourTimeSlots() {

        if (!(timeSlots.size() == 24)){
            timeSlots = new HashMap<>();
            for (int i = 1; i <= 24; i++) {
                if (i<10){
                    timeSlots.put("time0"+i,false);
                }else {
                    timeSlots.put("time"+i,false);
                }
            }
        }else {
            Log.d(TAG, "setHourTimeSlots: ?????");
        }


//        link all the buttons
        btn_time01 = findViewById(R.id.B_A_DoctorSetA_time01);
        btn_time02 = findViewById(R.id.B_A_DoctorSetA_time02);
        btn_time03 = findViewById(R.id.B_A_DoctorSetA_time03);
        btn_time04 = findViewById(R.id.B_A_DoctorSetA_time04);

        btn_time05 = findViewById(R.id.B_A_DoctorSetA_time05);
        btn_time06 = findViewById(R.id.B_A_DoctorSetA_time06);
        btn_time07 = findViewById(R.id.B_A_DoctorSetA_time07);
        btn_time08 = findViewById(R.id.B_A_DoctorSetA_time08);

        btn_time09 = findViewById(R.id.B_A_DoctorSetA_time09);
        btn_time10 = findViewById(R.id.B_A_DoctorSetA_time10);
        btn_time11 = findViewById(R.id.B_A_DoctorSetA_time11);
        btn_time12 = findViewById(R.id.B_A_DoctorSetA_time12);

        btn_time13 = findViewById(R.id.B_A_DoctorSetA_time13);
        btn_time14 = findViewById(R.id.B_A_DoctorSetA_time14);
        btn_time15 = findViewById(R.id.B_A_DoctorSetA_time15);
        btn_time16 = findViewById(R.id.B_A_DoctorSetA_time16);

        btn_time17 = findViewById(R.id.B_A_DoctorSetA_time17);
        btn_time18 = findViewById(R.id.B_A_DoctorSetA_time18);
        btn_time19 = findViewById(R.id.B_A_DoctorSetA_time19);
        btn_time20 = findViewById(R.id.B_A_DoctorSetA_time20);

        btn_time21 = findViewById(R.id.B_A_DoctorSetA_time21);
        btn_time22 = findViewById(R.id.B_A_DoctorSetA_time22);
        btn_time23 = findViewById(R.id.B_A_DoctorSetA_time23);
        btn_time24 = findViewById(R.id.B_A_DoctorSetA_time24);

//        reset all the buttons
        disableWorkTime(btn_time01);
        disableWorkTime(btn_time02);
        disableWorkTime(btn_time03);
        disableWorkTime(btn_time04);
        disableWorkTime(btn_time05);
        disableWorkTime(btn_time06);
        disableWorkTime(btn_time07);
        disableWorkTime(btn_time08);

        disableWorkTime(btn_time09);
        disableWorkTime(btn_time10);
        disableWorkTime(btn_time11);
        disableWorkTime(btn_time12);
        disableWorkTime(btn_time13);
        disableWorkTime(btn_time14);
        disableWorkTime(btn_time15);
        disableWorkTime(btn_time16);

        disableWorkTime(btn_time17);
        disableWorkTime(btn_time18);
        disableWorkTime(btn_time19);
        disableWorkTime(btn_time20);
        disableWorkTime(btn_time21);
        disableWorkTime(btn_time22);
        disableWorkTime(btn_time23);
        disableWorkTime(btn_time24);

//        buttons onlick listen for background and boolean change
        btn_time01.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time01");
            changeWorkTime(btn_time01,temp_boolean);
            timeSlots.put("time01",!temp_boolean );
        });
        btn_time02.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time02");
            changeWorkTime(btn_time02,temp_boolean);
            timeSlots.put("time02",!temp_boolean  );
        });
        btn_time03.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time03");
            changeWorkTime(btn_time03,temp_boolean);
            timeSlots.put("time03",!temp_boolean  );
        });
        btn_time04.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time04");
            changeWorkTime(btn_time04,temp_boolean);
            timeSlots.put("time04",!temp_boolean  );
        });

        btn_time05.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time05");
            changeWorkTime(btn_time05,temp_boolean);
            timeSlots.put("time05",!temp_boolean );
        });
        btn_time06.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time06");
            changeWorkTime(btn_time06,temp_boolean);
            timeSlots.put("time06",!temp_boolean  );
        });
        btn_time07.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time07");
            changeWorkTime(btn_time07,temp_boolean);
            timeSlots.put("time07",!temp_boolean  );
        });
        btn_time08.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time08");
            changeWorkTime(btn_time08,temp_boolean);
            timeSlots.put("time08",!temp_boolean  );
        });

        btn_time09.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time09");
            changeWorkTime(btn_time09,temp_boolean);
            timeSlots.put("time09",!temp_boolean );
        });
        btn_time10.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time10");
            changeWorkTime(btn_time10,temp_boolean);
            timeSlots.put("time10",!temp_boolean  );
        });
        btn_time11.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time11");
            changeWorkTime(btn_time11,temp_boolean);
            timeSlots.put("time11",!temp_boolean  );
        });
        btn_time12.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time12");
            changeWorkTime(btn_time12,temp_boolean);
            timeSlots.put("time12",!temp_boolean  );
        });

        btn_time13.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time13");
            changeWorkTime(btn_time13,temp_boolean);
            timeSlots.put("time13",!temp_boolean  );
        });
        btn_time14.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time14");
            changeWorkTime(btn_time14,temp_boolean);
            timeSlots.put("time14",!temp_boolean  );
        });
        btn_time15.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time15");
            changeWorkTime(btn_time15,temp_boolean);
            timeSlots.put("time15",!temp_boolean  );
        });
        btn_time16.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time16");
            changeWorkTime(btn_time16,temp_boolean);
            timeSlots.put("time16",!temp_boolean  );
        });

        btn_time17.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time17");
            changeWorkTime(btn_time17,temp_boolean);
            timeSlots.put("time17",!temp_boolean  );
        });
        btn_time18.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time18");
            changeWorkTime(btn_time18,temp_boolean);
            timeSlots.put("time18",!temp_boolean  );
        });
        btn_time19.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time19");
            changeWorkTime(btn_time19,temp_boolean);
            timeSlots.put("time19",!temp_boolean  );
        });
        btn_time20.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time20");
            changeWorkTime(btn_time20,temp_boolean);
            timeSlots.put("time20",!temp_boolean  );
        });

        btn_time21.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time21");
            changeWorkTime(btn_time21,temp_boolean);
            timeSlots.put("time21",!temp_boolean  );
        });
        btn_time22.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time22");
            changeWorkTime(btn_time22,temp_boolean);
            timeSlots.put("time22",!temp_boolean  );
        });
        btn_time23.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time23");
            changeWorkTime(btn_time23,temp_boolean);
            timeSlots.put("time23",!temp_boolean  );
        });
        btn_time24.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time24");
            changeWorkTime(btn_time24,temp_boolean);
            timeSlots.put("time24",!temp_boolean  );
        });


    }

    public void setHalfHourTimeSlots() {

        if (!(timeSlots.size() == 48)){
            timeSlots = new HashMap<>();
            for (int i = 1; i <= 48; i++) {
                if (i<10){
                    timeSlots.put("time0"+i,false);
                }else {
                    timeSlots.put("time"+i,false);
                }
            }
        }

//        link all the buttons
        btn_time01 = findViewById(R.id.B_A_DoctorSetA_timeH01);
        btn_time02 = findViewById(R.id.B_A_DoctorSetA_timeH02);
        btn_time03 = findViewById(R.id.B_A_DoctorSetA_timeH03);
        btn_time04 = findViewById(R.id.B_A_DoctorSetA_timeH04);

        btn_time05 = findViewById(R.id.B_A_DoctorSetA_timeH05);
        btn_time06 = findViewById(R.id.B_A_DoctorSetA_timeH06);
        btn_time07 = findViewById(R.id.B_A_DoctorSetA_timeH07);
        btn_time08 = findViewById(R.id.B_A_DoctorSetA_timeH08);

        btn_time09 = findViewById(R.id.B_A_DoctorSetA_timeH09);
        btn_time10 = findViewById(R.id.B_A_DoctorSetA_timeH10);
        btn_time11 = findViewById(R.id.B_A_DoctorSetA_timeH11);
        btn_time12 = findViewById(R.id.B_A_DoctorSetA_timeH12);

        btn_time13 = findViewById(R.id.B_A_DoctorSetA_timeH13);
        btn_time14 = findViewById(R.id.B_A_DoctorSetA_timeH14);
        btn_time15 = findViewById(R.id.B_A_DoctorSetA_timeH15);
        btn_time16 = findViewById(R.id.B_A_DoctorSetA_timeH16);

        btn_time17 = findViewById(R.id.B_A_DoctorSetA_timeH17);
        btn_time18 = findViewById(R.id.B_A_DoctorSetA_timeH18);
        btn_time19 = findViewById(R.id.B_A_DoctorSetA_timeH19);
        btn_time20 = findViewById(R.id.B_A_DoctorSetA_timeH20);

        btn_time21 = findViewById(R.id.B_A_DoctorSetA_timeH21);
        btn_time22 = findViewById(R.id.B_A_DoctorSetA_timeH22);
        btn_time23 = findViewById(R.id.B_A_DoctorSetA_timeH23);
        btn_time24 = findViewById(R.id.B_A_DoctorSetA_timeH24);

        btn_time25 = findViewById(R.id.B_A_DoctorSetA_timeH25);
        btn_time26 = findViewById(R.id.B_A_DoctorSetA_timeH26);
        btn_time27 = findViewById(R.id.B_A_DoctorSetA_timeH27);
        btn_time28 = findViewById(R.id.B_A_DoctorSetA_timeH28);

        btn_time29 = findViewById(R.id.B_A_DoctorSetA_timeH29);
        btn_time30 = findViewById(R.id.B_A_DoctorSetA_timeH30);
        btn_time31 = findViewById(R.id.B_A_DoctorSetA_timeH31);
        btn_time32 = findViewById(R.id.B_A_DoctorSetA_timeH32);

        btn_time33 = findViewById(R.id.B_A_DoctorSetA_timeH33);
        btn_time34 = findViewById(R.id.B_A_DoctorSetA_timeH34);
        btn_time35 = findViewById(R.id.B_A_DoctorSetA_timeH35);
        btn_time36 = findViewById(R.id.B_A_DoctorSetA_timeH36);

        btn_time37 = findViewById(R.id.B_A_DoctorSetA_timeH37);
        btn_time38 = findViewById(R.id.B_A_DoctorSetA_timeH38);
        btn_time39 = findViewById(R.id.B_A_DoctorSetA_timeH39);
        btn_time40 = findViewById(R.id.B_A_DoctorSetA_timeH40);

        btn_time41 = findViewById(R.id.B_A_DoctorSetA_timeH41);
        btn_time42 = findViewById(R.id.B_A_DoctorSetA_timeH42);
        btn_time43 = findViewById(R.id.B_A_DoctorSetA_timeH43);
        btn_time44 = findViewById(R.id.B_A_DoctorSetA_timeH44);

        btn_time45 = findViewById(R.id.B_A_DoctorSetA_timeH45);
        btn_time46 = findViewById(R.id.B_A_DoctorSetA_timeH46);
        btn_time47 = findViewById(R.id.B_A_DoctorSetA_timeH47);
        btn_time48 = findViewById(R.id.B_A_DoctorSetA_timeH48);

//        reset all the buttons
        disableWorkTime(btn_time01);
        disableWorkTime(btn_time02);
        disableWorkTime(btn_time03);
        disableWorkTime(btn_time04);
        disableWorkTime(btn_time05);
        disableWorkTime(btn_time06);
        disableWorkTime(btn_time07);
        disableWorkTime(btn_time08);

        disableWorkTime(btn_time09);
        disableWorkTime(btn_time10);
        disableWorkTime(btn_time11);
        disableWorkTime(btn_time12);
        disableWorkTime(btn_time13);
        disableWorkTime(btn_time14);
        disableWorkTime(btn_time15);
        disableWorkTime(btn_time16);

        disableWorkTime(btn_time17);
        disableWorkTime(btn_time18);
        disableWorkTime(btn_time19);
        disableWorkTime(btn_time20);
        disableWorkTime(btn_time21);
        disableWorkTime(btn_time22);
        disableWorkTime(btn_time23);
        disableWorkTime(btn_time24);

        disableWorkTime(btn_time25);
        disableWorkTime(btn_time26);
        disableWorkTime(btn_time27);
        disableWorkTime(btn_time28);
        disableWorkTime(btn_time29);
        disableWorkTime(btn_time30);
        disableWorkTime(btn_time31);
        disableWorkTime(btn_time32);

        disableWorkTime(btn_time33);
        disableWorkTime(btn_time34);
        disableWorkTime(btn_time35);
        disableWorkTime(btn_time36);
        disableWorkTime(btn_time37);
        disableWorkTime(btn_time38);
        disableWorkTime(btn_time39);
        disableWorkTime(btn_time40);

        disableWorkTime(btn_time41);
        disableWorkTime(btn_time42);
        disableWorkTime(btn_time43);
        disableWorkTime(btn_time44);
        disableWorkTime(btn_time45);
        disableWorkTime(btn_time46);
        disableWorkTime(btn_time47);
        disableWorkTime(btn_time48);

//        buttons onlick listen for background and boolean change
        btn_time01.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time01");
            changeWorkTime(btn_time01,temp_boolean);
            timeSlots.put("time01",!temp_boolean );
        });
        btn_time02.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time02");
            changeWorkTime(btn_time02,temp_boolean);
            timeSlots.put("time02",!temp_boolean  );
        });
        btn_time03.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time03");
            changeWorkTime(btn_time03,temp_boolean);
            timeSlots.put("time03",!temp_boolean  );
        });
        btn_time04.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time04");
            changeWorkTime(btn_time04,temp_boolean);
            timeSlots.put("time04",!temp_boolean  );
        });

        btn_time05.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time05");
            changeWorkTime(btn_time05,temp_boolean);
            timeSlots.put("time05",!temp_boolean );
        });
        btn_time06.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time06");
            changeWorkTime(btn_time06,temp_boolean);
            timeSlots.put("time06",!temp_boolean  );
        });
        btn_time07.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time07");
            changeWorkTime(btn_time07,temp_boolean);
            timeSlots.put("time07",!temp_boolean  );
        });
        btn_time08.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time08");
            changeWorkTime(btn_time08,temp_boolean);
            timeSlots.put("time08",!temp_boolean  );
        });

        btn_time09.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time09");
            changeWorkTime(btn_time09,temp_boolean);
            timeSlots.put("time09",!temp_boolean );
        });
        btn_time10.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time10");
            changeWorkTime(btn_time10,temp_boolean);
            timeSlots.put("time10",!temp_boolean  );
        });
        btn_time11.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time11");
            changeWorkTime(btn_time11,temp_boolean);
            timeSlots.put("time11",!temp_boolean  );
        });
        btn_time12.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time12");
            changeWorkTime(btn_time12,temp_boolean);
            timeSlots.put("time12",!temp_boolean  );
        });

        btn_time13.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time13");
            changeWorkTime(btn_time13,temp_boolean);
            timeSlots.put("time13",!temp_boolean  );
        });
        btn_time14.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time14");
            changeWorkTime(btn_time14,temp_boolean);
            timeSlots.put("time14",!temp_boolean  );
        });
        btn_time15.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time15");
            changeWorkTime(btn_time15,temp_boolean);
            timeSlots.put("time15",!temp_boolean  );
        });
        btn_time16.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time16");
            changeWorkTime(btn_time16,temp_boolean);
            timeSlots.put("time16",!temp_boolean  );
        });

        btn_time17.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time17");
            changeWorkTime(btn_time17,temp_boolean);
            timeSlots.put("time17",!temp_boolean  );
        });
        btn_time18.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time18");
            changeWorkTime(btn_time18,temp_boolean);
            timeSlots.put("time18",!temp_boolean  );
        });
        btn_time19.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time19");
            changeWorkTime(btn_time19,temp_boolean);
            timeSlots.put("time19",!temp_boolean  );
        });
        btn_time20.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time20");
            changeWorkTime(btn_time20,temp_boolean);
            timeSlots.put("time20",!temp_boolean  );
        });

        btn_time21.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time21");
            changeWorkTime(btn_time21,temp_boolean);
            timeSlots.put("time21",!temp_boolean  );
        });
        btn_time22.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time22");
            changeWorkTime(btn_time22,temp_boolean);
            timeSlots.put("time22",!temp_boolean  );
        });
        btn_time23.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time23");
            changeWorkTime(btn_time23,temp_boolean);
            timeSlots.put("time23",!temp_boolean  );
        });
        btn_time24.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time24");
            changeWorkTime(btn_time24,temp_boolean);
            timeSlots.put("time24",!temp_boolean  );
        });

        btn_time25.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time25");
            changeWorkTime(btn_time25,temp_boolean);
            timeSlots.put("time25",!temp_boolean  );
        });
        btn_time26.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time26");
            changeWorkTime(btn_time26,temp_boolean);
            timeSlots.put("time26",!temp_boolean  );
        });
        btn_time27.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time27");
            changeWorkTime(btn_time27,temp_boolean);
            timeSlots.put("time27",!temp_boolean  );
        });
        btn_time28.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time28");
            changeWorkTime(btn_time28,temp_boolean);
            timeSlots.put("time28",!temp_boolean  );
        });

        btn_time29.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time29");
            changeWorkTime(btn_time29,temp_boolean);
            timeSlots.put("time29",!temp_boolean  );
        });
        btn_time30.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time30");
            changeWorkTime(btn_time30,temp_boolean);
            timeSlots.put("time30",!temp_boolean  );
        });
        btn_time31.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time31");
            changeWorkTime(btn_time31,temp_boolean);
            timeSlots.put("time31",!temp_boolean  );
        });
        btn_time32.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time32");
            changeWorkTime(btn_time32,temp_boolean);
            timeSlots.put("time32",!temp_boolean  );
        });

        btn_time33.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time33");
            changeWorkTime(btn_time33,temp_boolean);
            timeSlots.put("time33",!temp_boolean  );
        });
        btn_time34.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time34");
            changeWorkTime(btn_time34,temp_boolean);
            timeSlots.put("time34",!temp_boolean  );
        });
        btn_time35.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time35");
            changeWorkTime(btn_time35,temp_boolean);
            timeSlots.put("time35",!temp_boolean  );
        });
        btn_time36.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time36");
            changeWorkTime(btn_time36,temp_boolean);
            timeSlots.put("time36",!temp_boolean  );
        });

        btn_time37.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time37");
            changeWorkTime(btn_time37,temp_boolean);
            timeSlots.put("time37",!temp_boolean  );
        });
        btn_time38.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time38");
            changeWorkTime(btn_time38,temp_boolean);
            timeSlots.put("time38",!temp_boolean  );
        });
        btn_time39.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time39");
            changeWorkTime(btn_time39,temp_boolean);
            timeSlots.put("time39",!temp_boolean  );
        });
        btn_time40.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time40");
            changeWorkTime(btn_time40,temp_boolean);
            timeSlots.put("time40",!temp_boolean  );
        });

        btn_time41.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time41");
            changeWorkTime(btn_time41,temp_boolean);
            timeSlots.put("time41",!temp_boolean  );
        });
        btn_time42.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time42");
            changeWorkTime(btn_time42,temp_boolean);
            timeSlots.put("time42",!temp_boolean  );
        });
        btn_time43.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time43");
            changeWorkTime(btn_time43,temp_boolean);
            timeSlots.put("time43",!temp_boolean  );
        });
        btn_time44.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time44");
            changeWorkTime(btn_time44,temp_boolean);
            timeSlots.put("time44",!temp_boolean  );
        });

        btn_time45.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time45");
            changeWorkTime(btn_time45,temp_boolean);
            timeSlots.put("time45",!temp_boolean  );
        });
        btn_time46.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time46");
            changeWorkTime(btn_time46,temp_boolean);
            timeSlots.put("time46",!temp_boolean  );
        });
        btn_time47.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time47");
            changeWorkTime(btn_time47,temp_boolean);
            timeSlots.put("time47",!temp_boolean  );
        });
        btn_time48.setOnClickListener(view -> {
            Boolean temp_boolean = timeSlots.get("time48");
            changeWorkTime(btn_time48,temp_boolean);
            timeSlots.put("time48",!temp_boolean  );
        });

    }

    private void changeWorkTime(Button btn, Boolean time){
        if (time){
            disableWorkTime(btn);
        }else{
            enableWorkTime(btn);
        }

    }

    private void disableWorkTime(Button btn){
        Drawable buttonDrawable = btn.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, Color.parseColor("#FF3B3B"));
        btn.setBackground(buttonDrawable);

    }

    private void enableWorkTime(Button btn){

        Drawable buttonDrawable = btn.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //"#12e0a2" as Doctor Green
        DrawableCompat.setTint(buttonDrawable, Color.parseColor("#12e0a2"));
        btn.setBackground(buttonDrawable);

    }

    private void setAvailability(){
        Note_Doctor_Availability note
                = new Note_Doctor_Availability(daysOfTheWeek, timeSlots,  isHalfHourSlots);
        collectionReference.document(doctorPhone)
                .collection("SubCollection").document("Availability")
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

    private void getAvailability(){

        collectionReference.document(doctorPhone)
                .collection("SubCollection").document("Availability")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note_Doctor_Availability note = documentSnapshot.toObject(Note_Doctor_Availability.class);

                        if (note != null){
                            daysOfTheWeek = note.getDaysOfWeek();
                            timeSlots = note.getTimeSlots();
                            isHalfHourSlots = note.getHalfHourSlots();

                            switch_monday.setChecked(daysOfTheWeek.get("monday"));
                            switch_tuesday.setChecked(daysOfTheWeek.get("tuesday"));
                            switch_wednesday.setChecked(daysOfTheWeek.get("wednesday"));
                            switch_thursday.setChecked(daysOfTheWeek.get("thursday"));
                            switch_friday.setChecked(daysOfTheWeek.get("friday"));
                            switch_saturday.setChecked(daysOfTheWeek.get("saturday"));
                            switch_sunday.setChecked(daysOfTheWeek.get("sunday"));

                            switch_timeSlot.setChecked(isHalfHourSlots);

                            if (timeSlots.get("time01")){
                                enableWorkTime(btn_time01);
                            }
                            if (timeSlots.get("time02")){
                                enableWorkTime(btn_time02);
                            }
                            if (timeSlots.get("time03")){
                                enableWorkTime(btn_time03);
                            }
                            if (timeSlots.get("time04")){
                                enableWorkTime(btn_time04);
                            }

                            if (timeSlots.get("time05")){
                                enableWorkTime(btn_time05);
                            }
                            if (timeSlots.get("time06")){
                                enableWorkTime(btn_time06);
                            }
                            if (timeSlots.get("time07")){
                                enableWorkTime(btn_time07);
                            }
                            if (timeSlots.get("time08")){
                                enableWorkTime(btn_time08);
                            }

                            if (timeSlots.get("time09")){
                                enableWorkTime(btn_time09);
                            }
                            if (timeSlots.get("time10")){
                                enableWorkTime(btn_time10);
                            }
                            if (timeSlots.get("time11")){
                                enableWorkTime(btn_time11);
                            }
                            if (timeSlots.get("time12")){
                                enableWorkTime(btn_time12);
                            }

                            if (timeSlots.get("time13")){
                                enableWorkTime(btn_time13);
                            }
                            if (timeSlots.get("time14")){
                                enableWorkTime(btn_time14);
                            }
                            if (timeSlots.get("time15")){
                                enableWorkTime(btn_time15);
                            }
                            if (timeSlots.get("time16")){
                                enableWorkTime(btn_time16);
                            }

                            if (timeSlots.get("time17")){
                                enableWorkTime(btn_time17);
                            }
                            if (timeSlots.get("time18")){
                                enableWorkTime(btn_time18);
                            }
                            if (timeSlots.get("time19")){
                                enableWorkTime(btn_time19);
                            }
                            if (timeSlots.get("time20")){
                                enableWorkTime(btn_time20);
                            }

                            if (timeSlots.get("time21")){
                                enableWorkTime(btn_time21);
                            }
                            if (timeSlots.get("time22")){
                                enableWorkTime(btn_time22);
                            }
                            if (timeSlots.get("time23")){
                                enableWorkTime(btn_time23);
                            }
                            if (timeSlots.get("time24")){
                                enableWorkTime(btn_time24);
                            }

                            if (isHalfHourSlots){
                                if (timeSlots.get("time25")){
                                    enableWorkTime(btn_time25);
                                }
                                if (timeSlots.get("time26")){
                                    enableWorkTime(btn_time26);
                                }
                                if (timeSlots.get("time27")){
                                    enableWorkTime(btn_time27);
                                }
                                if (timeSlots.get("time28")){
                                    enableWorkTime(btn_time28);
                                }

                                if (timeSlots.get("time29")){
                                    enableWorkTime(btn_time29);
                                }
                                if (timeSlots.get("time30")){
                                    enableWorkTime(btn_time30);
                                }
                                if (timeSlots.get("time31")){
                                    enableWorkTime(btn_time31);
                                }
                                if (timeSlots.get("time32")){
                                    enableWorkTime(btn_time32);
                                }

                                if (timeSlots.get("time33")){
                                    enableWorkTime(btn_time33);
                                }
                                if (timeSlots.get("time34")){
                                    enableWorkTime(btn_time34);
                                }
                                if (timeSlots.get("time35")){
                                    enableWorkTime(btn_time35);
                                }
                                if (timeSlots.get("time36")){
                                    enableWorkTime(btn_time36);
                                }

                                if (timeSlots.get("time37")){
                                    enableWorkTime(btn_time37);
                                }
                                if (timeSlots.get("time38")){
                                    enableWorkTime(btn_time38);
                                }
                                if (timeSlots.get("time39")){
                                    enableWorkTime(btn_time39);
                                }
                                if (timeSlots.get("time40")){
                                    enableWorkTime(btn_time40);
                                }

                                if (timeSlots.get("time41")){
                                    enableWorkTime(btn_time41);
                                }
                                if (timeSlots.get("time42")){
                                    enableWorkTime(btn_time42);
                                }
                                if (timeSlots.get("time43")){
                                    enableWorkTime(btn_time43);
                                }
                                if (timeSlots.get("time44")){
                                    enableWorkTime(btn_time44);
                                }

                                if (timeSlots.get("time45")){
                                    enableWorkTime(btn_time45);
                                }
                                if (timeSlots.get("time46")){
                                    enableWorkTime(btn_time46);
                                }
                                if (timeSlots.get("time47")){
                                    enableWorkTime(btn_time47);
                                }
                                if (timeSlots.get("time48")){
                                    enableWorkTime(btn_time48);
                                }
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
        intent.putExtra(Activity_Doctor_Login.doctorPhoneKEY,doctorPhone);
        startActivity(intent);
    }

    private void getDoctorDate(){
        collectionReference
                .document(doctorPhone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        doctorData = documentSnapshot.getData();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        backToDoctorPage();
    }
}