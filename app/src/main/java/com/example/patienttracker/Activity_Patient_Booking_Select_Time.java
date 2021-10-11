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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Activity_Patient_Booking_Select_Time extends AppCompatActivity {

    private static final String TAG = "Activity Patient Booking Select";

    //variables
    private String doctor_document_id,doctor_document_email,doctor_document_name,patient_document_id, patient_document_email,patient_document_firstName,patient_document_lastName;
    private Note_Doctor doctor_information;
    public String selectedDate,selectedDayOfWeek;
    private final long dateToday = new Date().getTime();
    private String today;
    private Calendar calendar = Calendar.getInstance();
    private Map<String, Boolean> noteDaysOfTheWeek;
    private Map<String, Boolean> noteTimeSlots;
    private Boolean noteIsHalfHourSlots;
    private Note_Doctor_Availability note_doctor_availability;
    private Note_Doctor note_doctor_;
    private Map<String, Object> patientData = new HashMap<>();
    private Map<String, Button> btnMap;

    //widgets
    private CalendarView calendarView;
    private LinearLayout ll_null;
    private Button btn_back;
    private TextView tv_unavailable;
    private ScrollView sv_timeSlots;
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
    private LinearLayout ll_time_slots_hour,ll_time_slots_half_hour;
    private LinearLayout
            ll_time_hour_01, ll_time_hour_02, ll_time_hour_03,
            ll_time_hour_04, ll_time_hour_05, ll_time_hour_06,
            ll_time_half_01, ll_time_half_02, ll_time_half_03, ll_time_half_04,
            ll_time_half_05, ll_time_half_06, ll_time_half_07, ll_time_half_08,
            ll_time_half_09, ll_time_half_10, ll_time_half_11, ll_time_half_12;

    private Dialog dialog_successful;
    private Button btn_confirm;

    //databse
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionDoctorReference = db.collection("Doctor");
    private CollectionReference collectionPatientReference = db.collection("Patient");
    private CollectionReference collectionBookingReference = db.collection("Booking");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_booking_select_time);

        //get variables from previous activity
        Intent intent = getIntent();
        patient_document_id = intent.getStringExtra(Fragment_Patient_Home.phoneKey);
        patient_document_email = intent.getStringExtra(Fragment_Patient_Home.emailKey);
        patient_document_firstName = intent.getStringExtra(Fragment_Patient_Home.firstNameKey);
        patient_document_lastName = intent.getStringExtra(Fragment_Patient_Home.lastNameKey);
        doctor_document_id = intent.getStringExtra(Activity_Patient_Booking_Select_Doctor.doctorInformationKey);
        doctor_document_email = intent.getStringExtra(Activity_Patient_Booking_Select_Doctor.doctorEmailKey);
        doctor_document_name = intent.getStringExtra(Activity_Patient_Booking_Select_Doctor.doctorNameKey);
        //set widgets view
        calendarView = findViewById(R.id.CV_A_PatientBookingSelect_Calender);
        calendarView.setMinDate((dateToday));//set CalendarView MinDate to "today"
        ll_null = findViewById(R.id.LL_A_PatientBookingSelect_Null);
        tv_unavailable = findViewById(R.id.TV_A_PatientBookingSelect_Unavailable);

        sv_timeSlots = findViewById(R.id.SV_A_PatientBookingSelect_TimeSlots);

        ll_time_slots_hour = findViewById(R.id.LL_A_PatientBookingSelect_TimeButtons_Hour);
        ll_time_slots_half_hour = findViewById(R.id.LL_A_PatientBookingSelect_TimeButtons_Half_Hour);

        ll_time_hour_01 = findViewById(R.id.LL_A_PatientBookingSelect_Time00_03);
        ll_time_hour_02 = findViewById(R.id.LL_A_PatientBookingSelect_Time04_07);
        ll_time_hour_03 = findViewById(R.id.LL_A_PatientBookingSelect_Time08_11);
        ll_time_hour_04 = findViewById(R.id.LL_A_PatientBookingSelect_Time12_15);
        ll_time_hour_05 = findViewById(R.id.LL_A_PatientBookingSelect_Time16_19);
        ll_time_hour_06 = findViewById(R.id.LL_A_PatientBookingSelect_Time20_23);

        ll_time_half_01 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH00_03);
        ll_time_half_02 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH04_07);
        ll_time_half_03 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH08_11);
        ll_time_half_04 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH12_15);
        ll_time_half_05 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH16_19);
        ll_time_half_06 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH20_23);

        ll_time_half_07 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH24_27);
        ll_time_half_08 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH28_31);
        ll_time_half_09 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH32_35);
        ll_time_half_10 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH36_39);
        ll_time_half_11 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH40_43);
        ll_time_half_12 = findViewById(R.id.LL_A_PatientBookingSelect_TimeH44_47);

        btn_back = findViewById(R.id.B_A_PatientBookingSelect_Back);

        dialog_successful = new Dialog(this);
        dialog_successful.setContentView(R.layout.dialog_booking_successful);
        dialog_successful.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_confirm = dialog_successful.findViewById(R.id.B_D_Success_Home);

        today = formatDate(dateToday);
        selectedDate = today;

        getDayOfTheWeekANDgetDoctorAvailabilityToday(calendar.get(Calendar.DAY_OF_WEEK));
        Log.d(TAG, "Patient Email"+ patient_document_email);
    }

    @Override
    protected void onStart() {
        super.onStart();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, dayOfMonth);
                long millis = gregorianCalendar.getTimeInMillis();
                selectedDate = formatDate(millis);
                int intdayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                getDayOfTheWeekANDgetDoctorAvailabilityToday(intdayOfWeek);

            }
        });

        btn_back.setOnClickListener(view -> {
            backToDoctorSelect();
        });

    }

    private String  formatDate(long date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void getDayOfTheWeekANDgetDoctorAvailabilityToday(int day){

        switch (day){
            case 1:
                selectedDayOfWeek = "sunday";
                break;
            case 2:
                selectedDayOfWeek = "monday";
                break;
            case 3:
                selectedDayOfWeek = "tuesday";
                break;
            case 4:
                selectedDayOfWeek = "wednesday";
                break;
            case 5:
                selectedDayOfWeek = "thursday";
                break;
            case 6:
                selectedDayOfWeek = "friday";
                break;
            case 7:
                selectedDayOfWeek = "saturday";
                break;
        }

        getDoctorAvailabilityToday();
    }

    private void getDoctorAvailabilityToday() {
        collectionDoctorReference.document(doctor_document_id)
                .collection("SubCollection").document("Availability")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Note_Doctor_Availability note = documentSnapshot.toObject(Note_Doctor_Availability.class);

                        if (note == null ){
                            ll_null.setVisibility(View.VISIBLE);
                            calendarView.setVisibility(View.GONE);
                            sv_timeSlots.setVisibility(View.GONE);
                        }
                        if (note != null){
                            note_doctor_availability = note;
                            noteDaysOfTheWeek = note.getDaysOfWeek();
                            noteTimeSlots = note.getTimeSlots();
                            noteIsHalfHourSlots = note.getHalfHourSlots();

                            if(noteDaysOfTheWeek.get(selectedDayOfWeek)){
                                tv_unavailable.setVisibility(View.GONE);
                                setDoctorAvailableSlots();
                            }
                            if(!noteDaysOfTheWeek.get(selectedDayOfWeek)){
                                tv_unavailable.setVisibility(View.VISIBLE);
                                sv_timeSlots.setVisibility(View.GONE);
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

    private void setDoctorAvailableSlots(){
//        Log.d(TAG, "setDoctorAvailableSlots: " + noteIsHalfHourSlots);

        if (!noteIsHalfHourSlots){
            ll_time_slots_hour.setVisibility(View.VISIBLE);
            ll_time_slots_half_hour.setVisibility(View.GONE);
            //        link all the buttons
            btn_time01 = findViewById(R.id.B_A_PatientBookingSelect_time01);
            btn_time02 = findViewById(R.id.B_A_PatientBookingSelect_time02);
            btn_time03 = findViewById(R.id.B_A_PatientBookingSelect_time03);
            btn_time04 = findViewById(R.id.B_A_PatientBookingSelect_time04);

            btn_time05 = findViewById(R.id.B_A_PatientBookingSelect_time05);
            btn_time06 = findViewById(R.id.B_A_PatientBookingSelect_time06);
            btn_time07 = findViewById(R.id.B_A_PatientBookingSelect_time07);
            btn_time08 = findViewById(R.id.B_A_PatientBookingSelect_time08);

            btn_time09 = findViewById(R.id.B_A_PatientBookingSelect_time09);
            btn_time10 = findViewById(R.id.B_A_PatientBookingSelect_time10);
            btn_time11 = findViewById(R.id.B_A_PatientBookingSelect_time11);
            btn_time12 = findViewById(R.id.B_A_PatientBookingSelect_time12);

            btn_time13 = findViewById(R.id.B_A_PatientBookingSelect_time13);
            btn_time14 = findViewById(R.id.B_A_PatientBookingSelect_time14);
            btn_time15 = findViewById(R.id.B_A_PatientBookingSelect_time15);
            btn_time16 = findViewById(R.id.B_A_PatientBookingSelect_time16);

            btn_time17 = findViewById(R.id.B_A_PatientBookingSelect_time17);
            btn_time18 = findViewById(R.id.B_A_PatientBookingSelect_time18);
            btn_time19 = findViewById(R.id.B_A_PatientBookingSelect_time19);
            btn_time20 = findViewById(R.id.B_A_PatientBookingSelect_time20);

            btn_time21 = findViewById(R.id.B_A_PatientBookingSelect_time21);
            btn_time22 = findViewById(R.id.B_A_PatientBookingSelect_time22);
            btn_time23 = findViewById(R.id.B_A_PatientBookingSelect_time23);
            btn_time24 = findViewById(R.id.B_A_PatientBookingSelect_time24);

            //Hide the unnecessary slots
            if (!noteTimeSlots.get("time01") && !noteTimeSlots.get("time02")
                    &&!noteTimeSlots.get("time03") &&!noteTimeSlots.get("time04")){
                ll_time_hour_01.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time05") && !noteTimeSlots.get("time06")
                    &&!noteTimeSlots.get("time07") &&!noteTimeSlots.get("time08")){
                ll_time_hour_02.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time09") && !noteTimeSlots.get("time10")
                    &&!noteTimeSlots.get("time11") &&!noteTimeSlots.get("time12")){
                ll_time_hour_03.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time13") && !noteTimeSlots.get("time14")
                    &&!noteTimeSlots.get("time15") &&!noteTimeSlots.get("time16")){
                ll_time_hour_04.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time17") && !noteTimeSlots.get("time18")
                    &&!noteTimeSlots.get("time19") &&!noteTimeSlots.get("time20")){
                ll_time_hour_05.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time21") && !noteTimeSlots.get("time22")
                    &&!noteTimeSlots.get("time23") &&!noteTimeSlots.get("time24")){
                ll_time_hour_06.setVisibility(View.GONE);
            }

            //Set Onclick listener for available the buttons
            setupTimeSlotButton(btn_time01,"time01");
            setupTimeSlotButton(btn_time02,"time02");
            setupTimeSlotButton(btn_time03,"time03");
            setupTimeSlotButton(btn_time04,"time04");

            setupTimeSlotButton(btn_time05,"time05");
            setupTimeSlotButton(btn_time06,"time06");
            setupTimeSlotButton(btn_time07,"time07");
            setupTimeSlotButton(btn_time08,"time08");

            setupTimeSlotButton(btn_time09,"time09");
            setupTimeSlotButton(btn_time10,"time10");
            setupTimeSlotButton(btn_time11,"time11");
            setupTimeSlotButton(btn_time12,"time12");

            setupTimeSlotButton(btn_time13,"time13");
            setupTimeSlotButton(btn_time14,"time14");
            setupTimeSlotButton(btn_time15,"time15");
            setupTimeSlotButton(btn_time16,"time16");

            setupTimeSlotButton(btn_time17,"time17");
            setupTimeSlotButton(btn_time18,"time18");
            setupTimeSlotButton(btn_time19,"time19");
            setupTimeSlotButton(btn_time20,"time20");

            setupTimeSlotButton(btn_time21,"time21");
            setupTimeSlotButton(btn_time22,"time22");
            setupTimeSlotButton(btn_time23,"time23");
            setupTimeSlotButton(btn_time24,"time24");

            btnMap = new HashMap<>();

            btnMap.put("time01",btn_time01);
            btnMap.put("time02",btn_time02);
            btnMap.put("time03",btn_time03);
            btnMap.put("time04",btn_time04);

            btnMap.put("time05",btn_time05);
            btnMap.put("time06",btn_time06);
            btnMap.put("time07",btn_time07);
            btnMap.put("time08",btn_time08);

            btnMap.put("time09",btn_time09);
            btnMap.put("time10",btn_time10);
            btnMap.put("time11",btn_time11);
            btnMap.put("time12",btn_time12);

            btnMap.put("time13",btn_time13);
            btnMap.put("time14",btn_time14);
            btnMap.put("time15",btn_time15);
            btnMap.put("time16",btn_time16);

            btnMap.put("time17",btn_time17);
            btnMap.put("time18",btn_time18);
            btnMap.put("time19",btn_time19);
            btnMap.put("time20",btn_time20);

            btnMap.put("time21",btn_time21);
            btnMap.put("time22",btn_time22);
            btnMap.put("time23",btn_time23);
            btnMap.put("time24",btn_time24);

            getDoctorBookedSlotsToday();
        }

        if (noteIsHalfHourSlots){
            ll_time_slots_hour.setVisibility(View.GONE);
            ll_time_slots_half_hour.setVisibility(View.VISIBLE);

            //        link all the buttons
            btn_time01 = findViewById(R.id.B_A_PatientBookingSelect_timeH01);
            btn_time02 = findViewById(R.id.B_A_PatientBookingSelect_timeH02);
            btn_time03 = findViewById(R.id.B_A_PatientBookingSelect_timeH03);
            btn_time04 = findViewById(R.id.B_A_PatientBookingSelect_timeH04);

            btn_time05 = findViewById(R.id.B_A_PatientBookingSelect_timeH05);
            btn_time06 = findViewById(R.id.B_A_PatientBookingSelect_timeH06);
            btn_time07 = findViewById(R.id.B_A_PatientBookingSelect_timeH07);
            btn_time08 = findViewById(R.id.B_A_PatientBookingSelect_timeH08);

            btn_time09 = findViewById(R.id.B_A_PatientBookingSelect_timeH09);
            btn_time10 = findViewById(R.id.B_A_PatientBookingSelect_timeH10);
            btn_time11 = findViewById(R.id.B_A_PatientBookingSelect_timeH11);
            btn_time12 = findViewById(R.id.B_A_PatientBookingSelect_timeH12);

            btn_time13 = findViewById(R.id.B_A_PatientBookingSelect_timeH13);
            btn_time14 = findViewById(R.id.B_A_PatientBookingSelect_timeH14);
            btn_time15 = findViewById(R.id.B_A_PatientBookingSelect_timeH15);
            btn_time16 = findViewById(R.id.B_A_PatientBookingSelect_timeH16);

            btn_time17 = findViewById(R.id.B_A_PatientBookingSelect_timeH17);
            btn_time18 = findViewById(R.id.B_A_PatientBookingSelect_timeH18);
            btn_time19 = findViewById(R.id.B_A_PatientBookingSelect_timeH19);
            btn_time20 = findViewById(R.id.B_A_PatientBookingSelect_timeH20);

            btn_time21 = findViewById(R.id.B_A_PatientBookingSelect_timeH21);
            btn_time22 = findViewById(R.id.B_A_PatientBookingSelect_timeH22);
            btn_time23 = findViewById(R.id.B_A_PatientBookingSelect_timeH23);
            btn_time24 = findViewById(R.id.B_A_PatientBookingSelect_timeH24);

            btn_time25 = findViewById(R.id.B_A_PatientBookingSelect_timeH25);
            btn_time26 = findViewById(R.id.B_A_PatientBookingSelect_timeH26);
            btn_time27 = findViewById(R.id.B_A_PatientBookingSelect_timeH27);
            btn_time28 = findViewById(R.id.B_A_PatientBookingSelect_timeH28);

            btn_time29 = findViewById(R.id.B_A_PatientBookingSelect_timeH29);
            btn_time30 = findViewById(R.id.B_A_PatientBookingSelect_timeH30);
            btn_time31 = findViewById(R.id.B_A_PatientBookingSelect_timeH31);
            btn_time32 = findViewById(R.id.B_A_PatientBookingSelect_timeH32);

            btn_time33 = findViewById(R.id.B_A_PatientBookingSelect_timeH33);
            btn_time34 = findViewById(R.id.B_A_PatientBookingSelect_timeH34);
            btn_time35 = findViewById(R.id.B_A_PatientBookingSelect_timeH35);
            btn_time36 = findViewById(R.id.B_A_PatientBookingSelect_timeH36);

            btn_time37 = findViewById(R.id.B_A_PatientBookingSelect_timeH37);
            btn_time38 = findViewById(R.id.B_A_PatientBookingSelect_timeH38);
            btn_time39 = findViewById(R.id.B_A_PatientBookingSelect_timeH39);
            btn_time40 = findViewById(R.id.B_A_PatientBookingSelect_timeH40);

            btn_time41 = findViewById(R.id.B_A_PatientBookingSelect_timeH41);
            btn_time42 = findViewById(R.id.B_A_PatientBookingSelect_timeH42);
            btn_time43 = findViewById(R.id.B_A_PatientBookingSelect_timeH43);
            btn_time44 = findViewById(R.id.B_A_PatientBookingSelect_timeH44);

            btn_time45 = findViewById(R.id.B_A_PatientBookingSelect_timeH45);
            btn_time46 = findViewById(R.id.B_A_PatientBookingSelect_timeH46);
            btn_time47 = findViewById(R.id.B_A_PatientBookingSelect_timeH47);
            btn_time48 = findViewById(R.id.B_A_PatientBookingSelect_timeH48);

            //Hide the unnecessary slots
            if (!noteTimeSlots.get("time01") && !noteTimeSlots.get("time02")
                    &&!noteTimeSlots.get("time03") &&!noteTimeSlots.get("time04")){
                ll_time_half_01.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time05") && !noteTimeSlots.get("time06")
                    &&!noteTimeSlots.get("time07") &&!noteTimeSlots.get("time08")){
                ll_time_half_02.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time09") && !noteTimeSlots.get("time10")
                    &&!noteTimeSlots.get("time11") &&!noteTimeSlots.get("time12")){
                ll_time_half_03.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time13") && !noteTimeSlots.get("time14")
                    &&!noteTimeSlots.get("time15") &&!noteTimeSlots.get("time16")){
                ll_time_half_04.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time17") && !noteTimeSlots.get("time18")
                    &&!noteTimeSlots.get("time19") &&!noteTimeSlots.get("time20")){
                ll_time_half_05.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time21") && !noteTimeSlots.get("time22")
                    &&!noteTimeSlots.get("time23") &&!noteTimeSlots.get("time24")){
                ll_time_half_06.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time25") && !noteTimeSlots.get("time26")
                    &&!noteTimeSlots.get("time27") &&!noteTimeSlots.get("time28")){
                ll_time_half_07.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time29") && !noteTimeSlots.get("time30")
                    &&!noteTimeSlots.get("time31") &&!noteTimeSlots.get("time32")){
                ll_time_half_08.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time33") && !noteTimeSlots.get("time34")
                    &&!noteTimeSlots.get("time35") &&!noteTimeSlots.get("time36")){
                ll_time_half_09.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time37") && !noteTimeSlots.get("time38")
                    &&!noteTimeSlots.get("time39") &&!noteTimeSlots.get("time40")){
                ll_time_half_10.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time41") && !noteTimeSlots.get("time42")
                    &&!noteTimeSlots.get("time43") &&!noteTimeSlots.get("time44")){
                ll_time_half_11.setVisibility(View.GONE);
            }
            if (!noteTimeSlots.get("time45") && !noteTimeSlots.get("time46")
                    &&!noteTimeSlots.get("time47") &&!noteTimeSlots.get("time48")){
                ll_time_half_12.setVisibility(View.GONE);
            }

            //Set Onclick listener for available the buttons
            setupTimeSlotButton(btn_time01,"time01");
            setupTimeSlotButton(btn_time02,"time02");
            setupTimeSlotButton(btn_time03,"time03");
            setupTimeSlotButton(btn_time04,"time04");

            setupTimeSlotButton(btn_time05,"time05");
            setupTimeSlotButton(btn_time06,"time06");
            setupTimeSlotButton(btn_time07,"time07");
            setupTimeSlotButton(btn_time08,"time08");

            setupTimeSlotButton(btn_time09,"time09");
            setupTimeSlotButton(btn_time10,"time10");
            setupTimeSlotButton(btn_time11,"time11");
            setupTimeSlotButton(btn_time12,"time12");

            setupTimeSlotButton(btn_time13,"time13");
            setupTimeSlotButton(btn_time14,"time14");
            setupTimeSlotButton(btn_time15,"time15");
            setupTimeSlotButton(btn_time16,"time16");

            setupTimeSlotButton(btn_time17,"time17");
            setupTimeSlotButton(btn_time18,"time18");
            setupTimeSlotButton(btn_time19,"time19");
            setupTimeSlotButton(btn_time20,"time20");

            setupTimeSlotButton(btn_time21,"time21");
            setupTimeSlotButton(btn_time22,"time22");
            setupTimeSlotButton(btn_time23,"time23");
            setupTimeSlotButton(btn_time24,"time24");

            setupTimeSlotButton(btn_time25,"time25");
            setupTimeSlotButton(btn_time26,"time26");
            setupTimeSlotButton(btn_time27,"time27");
            setupTimeSlotButton(btn_time28,"time28");

            setupTimeSlotButton(btn_time29,"time29");
            setupTimeSlotButton(btn_time30,"time30");
            setupTimeSlotButton(btn_time31,"time31");
            setupTimeSlotButton(btn_time32,"time32");

            setupTimeSlotButton(btn_time33,"time33");
            setupTimeSlotButton(btn_time34,"time34");
            setupTimeSlotButton(btn_time35,"time35");
            setupTimeSlotButton(btn_time36,"time36");

            setupTimeSlotButton(btn_time37,"time37");
            setupTimeSlotButton(btn_time38,"time38");
            setupTimeSlotButton(btn_time39,"time39");
            setupTimeSlotButton(btn_time40,"time40");

            setupTimeSlotButton(btn_time41,"time41");
            setupTimeSlotButton(btn_time42,"time42");
            setupTimeSlotButton(btn_time43,"time43");
            setupTimeSlotButton(btn_time44,"time44");

            setupTimeSlotButton(btn_time45,"time45");
            setupTimeSlotButton(btn_time46,"time46");
            setupTimeSlotButton(btn_time47,"time47");
            setupTimeSlotButton(btn_time48,"time48");

            btnMap = new HashMap<>();

            btnMap.put("time01",btn_time01);
            btnMap.put("time02",btn_time02);
            btnMap.put("time03",btn_time03);
            btnMap.put("time04",btn_time04);

            btnMap.put("time05",btn_time05);
            btnMap.put("time06",btn_time06);
            btnMap.put("time07",btn_time07);
            btnMap.put("time08",btn_time08);

            btnMap.put("time09",btn_time09);
            btnMap.put("time10",btn_time10);
            btnMap.put("time11",btn_time11);
            btnMap.put("time12",btn_time12);

            btnMap.put("time13",btn_time13);
            btnMap.put("time14",btn_time14);
            btnMap.put("time15",btn_time15);
            btnMap.put("time16",btn_time16);

            btnMap.put("time17",btn_time17);
            btnMap.put("time18",btn_time18);
            btnMap.put("time19",btn_time19);
            btnMap.put("time20",btn_time20);

            btnMap.put("time21",btn_time21);
            btnMap.put("time22",btn_time22);
            btnMap.put("time23",btn_time23);
            btnMap.put("time24",btn_time24);

            btnMap.put("time25",btn_time25);
            btnMap.put("time26",btn_time26);
            btnMap.put("time27",btn_time27);
            btnMap.put("time28",btn_time28);

            btnMap.put("time29",btn_time29);
            btnMap.put("time30",btn_time30);
            btnMap.put("time31",btn_time31);
            btnMap.put("time32",btn_time32);

            btnMap.put("time33",btn_time33);
            btnMap.put("time34",btn_time34);
            btnMap.put("time35",btn_time35);
            btnMap.put("time36",btn_time36);

            btnMap.put("time37",btn_time37);
            btnMap.put("time38",btn_time38);
            btnMap.put("time39",btn_time39);
            btnMap.put("time40",btn_time40);

            btnMap.put("time41",btn_time41);
            btnMap.put("time42",btn_time42);
            btnMap.put("time43",btn_time43);
            btnMap.put("time44",btn_time44);

            btnMap.put("time45",btn_time45);
            btnMap.put("time46",btn_time46);
            btnMap.put("time47",btn_time47);
            btnMap.put("time48",btn_time48);

            getDoctorBookedSlotsToday();
        }

    }

    private void setupTimeSlotButton(Button btn,String timeSlot){
        if (noteTimeSlots.get(timeSlot)){
            btn.setOnClickListener(view -> {
                bookTimeSlot(timeSlot);
                timeSlotUnavailable(btn);
            });
            btn.setClickable(true);
            Drawable buttonDrawable = btn.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            //"#12e0a2" as Doctor Green
            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#12e0a2"));
            btn.setBackground(buttonDrawable);
        }
        if (!noteTimeSlots.get(timeSlot)){
            timeSlotUnavailable(btn);
        }
    }


    private class EmailTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
                    final String Username = "notdiscoveryemails@gmail.com";
                    final String Password = "SDgroup12";
                    String MessagetoSend = "Booking confirmed for "+selectedDate +"\n" +"Doctor name: "+doctor_document_name+"\n"+"Patient name: "+patient_document_firstName+" "+patient_document_lastName;
                    Properties props = new Properties();
                    props.put("mail.smtp.auth","true");
                    props.put("mail.smtp.starttls.enable","true");
                    props.put("mail.smtp.host","smtp.gmail.com");
                    props.put("mail.smtp.port","587");

                    Session session = Session.getInstance(props,
                            new Authenticator(){
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication(){
                                    return new PasswordAuthentication(Username,Password);
                                }
                            });

                    try{
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(Username));
                        Log.d(TAG, "Patient Email in email task"+ patient_document_email);
                        String SendList = doctor_document_email +","+patient_document_email;
                        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(SendList) );
                        message.setSubject("Not Discovery Booking Confirmation");
                        message.setText(MessagetoSend);
                        Transport.send(message);
                    }catch(MessagingException e){
                        throw new RuntimeException(e);
                    }

            return null;
        }
    }

    private void bookTimeSlot(String timeSlot){
        Note_Booking note_booking = new Note_Booking(
                selectedDate,today,timeSlot,doctor_document_id,patient_document_id,noteIsHalfHourSlots);

        collectionBookingReference.add(note_booking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        getPatientData();
                        new EmailTask().execute();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void timeSlotUnavailable(Button btn){
        btn.setClickable(false);
        Drawable buttonDrawable = btn.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, Color.parseColor("#FF3B3B"));
        btn.setBackground(buttonDrawable);
    }

    private void getDoctorBookedSlotsToday() {
        collectionBookingReference
                .whereEqualTo("doctor_documentID",doctor_document_id)
                .whereEqualTo("date",selectedDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                timeSlotUnavailable(btnMap.get(document.getData().get("timeSlot")));
                            }
                        }else {

                        }
                    }
                });

        sv_timeSlots.setVisibility(View.VISIBLE);
    }

    private void getPatientData(){
        collectionPatientReference.document(patient_document_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        patientData = documentSnapshot.getData();
                        openSuccessfulDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void backToDoctorSelect() {
        Intent intent = new Intent(this, Activity_Patient_Booking_Select_Doctor.class);
        intent.putExtra(Activity_Patient_Login.patientDataKEY, (Serializable) patientData);
        intent.putExtra(Fragment_Patient_Home.phoneKey, patient_document_id);
        startActivity(intent);
    }

    private void backToPatientHome(){
        Intent intent = new Intent(this, Activity_Patient.class);
        //passing values to user activity
        intent.putExtra(Activity_Patient_Login.patientDataKEY, (Serializable) patientData);
        intent.putExtra(Activity_Patient_Login.patientPhoneKEY,patient_document_id);
        startActivity(intent);
    }

    private void openSuccessfulDialog() {
        dialog_successful.show();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToPatientHome();
            }
        });
    }

    private void openFailDialog() {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backToDoctorSelect();
    }
}