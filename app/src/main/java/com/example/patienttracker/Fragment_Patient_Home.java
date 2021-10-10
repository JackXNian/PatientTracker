package com.example.patienttracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Fragment_Patient_Home extends Fragment {

    //variables
    private static final String TAG                 = "Fragment_Patient_Home";

    public static final String firstNameKey        = "firstname";
    public static final String lastNameKey         = "lastname";
    public static final String phoneKey            = "phonenumber";
    public static final String emailKey            = "emailaddress";

    public static final String dateFormatPatten = "yyyy-MM-dd";

    private String
            patient_first_name, patient_last_name, patient_phone, patient_email,
            date_today;
    private final Date dateToday = new Date();
    private QueryDocumentSnapshot up_coming_appointment;

    //widgets
    private TextView
            tv_userFirstName, tv_userLastName, tv_userPhone, tv_userEmail, tv_userDocumentID,
            tv_upComingDateTime, tv_upComingDoctor, tv_upComingDoctorNumber, tv_upComingDateOfAction;
    private Button btn_booking;
    private RelativeLayout rl_upComing;
    private LinearLayout ll_upComingNo;

    //databse
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionDoctorReference = db.collection("Doctor");
    private CollectionReference collectionBookingReference = db.collection("Booking");

    public Fragment_Patient_Home() {
        // Required empty public constructor
    }

    public static Fragment_Patient_Home newInstance(
            String FName,String  LName,String  Phone,String Email) {
        Fragment_Patient_Home fragment = new Fragment_Patient_Home();
        Bundle bundle = new Bundle();

        bundle.putString(firstNameKey,FName);
        bundle.putString(lastNameKey,LName);
        bundle.putString(phoneKey,Phone);
        bundle.putString(emailKey,Email);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patient_first_name = getArguments().getString(firstNameKey);
            patient_last_name = getArguments().getString(lastNameKey);
            patient_phone = getArguments().getString(phoneKey);
            patient_email = getArguments().getString(emailKey);
        }
        date_today = formatDate(dateToday.getTime());

    }//End of onCrete


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override  //Cycle After onCrete
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient, container, false);

        getUpcomingBooking();

        tv_userFirstName = view.findViewById(R.id.TV_F_Patient_First_Name);
        tv_userLastName = view.findViewById(R.id.TV_F_Patient_Last_Name);
        tv_userPhone = view.findViewById(R.id.TV_F_Patient_Phone);
        tv_userEmail = view.findViewById(R.id.TV_F_Patient_Email);
        tv_userDocumentID = view.findViewById(R.id.TV_F_Patient_userID);

        tv_upComingDateTime = view.findViewById(R.id.TV_F_patient_upComing_dateTime);
        tv_upComingDoctor = view.findViewById(R.id.TV_F_patient_upComing_doctor);
        tv_upComingDoctorNumber = view.findViewById(R.id.TV_F_patient_upComing_doctor_phone);
        tv_upComingDateOfAction = view.findViewById(R.id.TV_F_patient_upComing_dateOfAction);

        rl_upComing = view.findViewById(R.id.RL_F_patient_upComing);
        ll_upComingNo = view.findViewById(R.id.LL_patient_upComingNo);

        tv_userFirstName.setText(patient_first_name);
        tv_userLastName.setText(patient_last_name);
        tv_userPhone.setText(patient_phone);
        tv_userEmail.setText(patient_email);
        tv_userDocumentID.setText("Login ID : " + patient_phone);

        btn_booking = view.findViewById(R.id.B_F_Patient_Book);

        return view;
    }//end of onCreteView

    @SuppressLint("SetTextI18n")
    @Override  //Cycle After onCreteView
    public void onStart() {
        super.onStart();

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Patient_Booking_Select_Doctor.class);
                intent.putExtra(phoneKey, patient_phone);
                intent.putExtra(emailKey, patient_email);
                startActivity(intent);
            }
        });

    }//end of onStart

    private String  formatDate(long date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(dateFormatPatten);
        return format.format(date);
    }

    private void getUpcomingBooking() {
         collectionBookingReference
                .whereEqualTo("patient_documentID",patient_phone)
                .orderBy("date", Query.Direction.ASCENDING)
                .orderBy("timeSlot", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            ll_upComingNo.setVisibility(View.VISIBLE);
                            rl_upComing.setVisibility(View.GONE);
                        }else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                try {
                                    findUpcomingDocumnet(document);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                });
    }

    private void findUpcomingDocumnet(QueryDocumentSnapshot document) throws ParseException {
        String document_date = (String) document.get("date");

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPatten);
        Date documentDate = sdf.parse(document_date);
        Date today = sdf.parse(date_today);

        if (today.equals(documentDate) || today.before(documentDate)) {
            if (up_coming_appointment == null){
                up_coming_appointment = document;

                setupUpomingCard(up_coming_appointment);
            }else {
                Date currDocumentDate =  sdf.parse( (String) up_coming_appointment.get("date"));
                if (documentDate.before(currDocumentDate)){
                    up_coming_appointment = document;

                    setupUpomingCard(up_coming_appointment);
                }
            }
        }else {

        }
    }

    @SuppressLint("SetTextI18n")
    private void setupUpomingCard(QueryDocumentSnapshot document) {
        ll_upComingNo.setVisibility(View.GONE);
        rl_upComing.setVisibility(View.VISIBLE);
        tv_upComingDateTime.setText(
                (String) document.getData().get("date") + " "
                        + getTime((String) document.getData().get("timeSlot")
                        , (Boolean) document.getData().get("doctor_isHalfHourSlot")));
        getDoctorName((String) document.getData().get("doctor_documentID"));
        tv_upComingDoctorNumber.setText( "Doctor Number : " + (String) document.getData().get("doctor_documentID"));
        tv_upComingDateOfAction.setText( "Appointment was booked on : " + (String) document.getData().get("dateOfAction"));
    }

    private String getTime(String time,Boolean isHalfHourSlot) {
        String temp_time_string = "Error Getting Time";

        if (isHalfHourSlot){
            switch (time){
                case "time01":
                    temp_time_string = "00:00";
                    break;
                case "time02":
                    temp_time_string = "00:30";
                    break;
                case "time03":
                    temp_time_string = "01:00";
                    break;
                case "time04":
                    temp_time_string = "01:30";
                    break;

                case "time05":
                    temp_time_string = "02:00";
                    break;
                case "time06":
                    temp_time_string = "02:30";
                    break;
                case "time07":
                    temp_time_string = "03:00";
                    break;
                case "time08":
                    temp_time_string = "03:30";
                    break;

                case "time09":
                    temp_time_string = "04:00";
                    break;
                case "time10":
                    temp_time_string = "04:30";
                    break;
                case "time11":
                    temp_time_string = "05:00";
                    break;
                case "time12":
                    temp_time_string = "05:30";
                    break;

                case "time13":
                    temp_time_string = "06:00";
                    break;
                case "time14":
                    temp_time_string = "06:30";
                    break;
                case "time15":
                    temp_time_string = "07:00";
                    break;
                case "time16":
                    temp_time_string = "07:30";
                    break;

                case "time17":
                    temp_time_string = "08:00";
                    break;
                case "time18":
                    temp_time_string = "08:30";
                    break;
                case "time19":
                    temp_time_string = "09:00";
                    break;
                case "time20":
                    temp_time_string = "09:30";
                    break;

                case "time21":
                    temp_time_string = "10:00";
                    break;
                case "time22":
                    temp_time_string = "10:30";
                    break;
                case "time23":
                    temp_time_string = "11:00";
                    break;
                case "time24":
                    temp_time_string = "11:30";
                    break;

                case "time25":
                    temp_time_string = "12:00";
                    break;
                case "time26":
                    temp_time_string = "12:30";
                    break;
                case "time27":
                    temp_time_string = "13:00";
                    break;
                case "time28":
                    temp_time_string = "13:30";
                    break;

                case "time29":
                    temp_time_string = "14:00";
                    break;
                case "time30":
                    temp_time_string = "14:30";
                    break;
                case "time31":
                    temp_time_string = "15:00";
                    break;
                case "time32":
                    temp_time_string = "15:30";
                    break;

                case "time33":
                    temp_time_string = "16:00";
                    break;
                case "time34":
                    temp_time_string = "16:30";
                    break;
                case "time35":
                    temp_time_string = "17:00";
                    break;
                case "time36":
                    temp_time_string = "17:30";
                    break;

                case "time37":
                    temp_time_string = "18:00";
                    break;
                case "time38":
                    temp_time_string = "18:30";
                    break;
                case "time39":
                    temp_time_string = "19:00";
                    break;
                case "time40":
                    temp_time_string = "19:30";
                    break;

                case "time41":
                    temp_time_string = "20:00";
                    break;
                case "time42":
                    temp_time_string = "20:30";
                    break;
                case "time43":
                    temp_time_string = "21:00";
                    break;
                case "time44":
                    temp_time_string = "21:30";
                    break;

                case "time45":
                    temp_time_string = "22:00";
                    break;
                case "time46":
                    temp_time_string = "22:30";
                    break;
                case "time47":
                    temp_time_string = "23:00";
                    break;
                case "time48":
                    temp_time_string = "23:30";
                    break;
            }

        }else {
            switch (time){
                case "time01":
                    temp_time_string = "00:00";
                    break;
                case "time02":
                    temp_time_string = "01:00";
                    break;
                case "time03":
                    temp_time_string = "02:00";
                    break;
                case "time04":
                    temp_time_string = "03:00";
                    break;

                case "time05":
                    temp_time_string = "04:00";
                    break;
                case "time06":
                    temp_time_string = "05:00";
                    break;
                case "time07":
                    temp_time_string = "06:00";
                    break;
                case "time08":
                    temp_time_string = "07:00";
                    break;

                case "time09":
                    temp_time_string = "08:00";
                    break;
                case "time10":
                    temp_time_string = "09:00";
                    break;
                case "time11":
                    temp_time_string = "10:00";
                    break;
                case "time12":
                    temp_time_string = "11:00";
                    break;

                case "time13":
                    temp_time_string = "12:00";
                    break;
                case "time14":
                    temp_time_string = "13:00";
                    break;
                case "time15":
                    temp_time_string = "14:00";
                    break;
                case "time16":
                    temp_time_string = "15:00";
                    break;

                case "time17":
                    temp_time_string = "16:00";
                    break;
                case "time18":
                    temp_time_string = "17:00";
                    break;
                case "time19":
                    temp_time_string = "18:00";
                    break;
                case "time20":
                    temp_time_string = "19:00";
                    break;

                case "time21":
                    temp_time_string = "20:00";
                    break;
                case "time22":
                    temp_time_string = "21:00";
                    break;
                case "time23":
                    temp_time_string = "22:00";
                    break;
                case "time24":
                    temp_time_string = "23:00";
                    break;
            }
        }

        return temp_time_string;
    }

    private void getDoctorName(String doctor_document_id){
        collectionDoctorReference.document(doctor_document_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String temp_name1 = (String) documentSnapshot.get("FirstName");
                        String temp_name2 = (String) documentSnapshot.get("LastName");
                        tv_upComingDoctor.setText(temp_name1 + " " + temp_name2);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tv_upComingDoctor.setText("Error Getting Doctor Name");
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}