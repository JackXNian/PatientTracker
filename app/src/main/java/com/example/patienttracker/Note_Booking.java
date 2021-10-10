package com.example.patienttracker;

import java.util.Date;

public class Note_Booking {

    private String date;
    private String doctor_documentID;
    private String patient_documentID;
    private Boolean doctor_isHalfHourSlot;
    private String timeSlot;
    private String dateOfAction;

    public Note_Booking() {
        //Empty constructor needed
    }

    public Note_Booking(String date, String date_of_action, String time_slot , String doctor_documentID, String patient_documentID,
                        Boolean doctor_isHalfHourSlot)
    {
        this.date = date;
        this.dateOfAction = date_of_action;
        this.timeSlot = time_slot;
        this.doctor_documentID = doctor_documentID;
        this.patient_documentID = patient_documentID;
        this.doctor_isHalfHourSlot = doctor_isHalfHourSlot;
    }

    public String getDate() {
        return date;
    }

    public String getDateOfAction() {
        return dateOfAction;
    }

    public String getTimeSlot() { return timeSlot; }

    public String getDoctor_documentID() {
        return doctor_documentID;
    }

    public String getPatient_documentID() {
        return patient_documentID;
    }

    public Boolean getDoctor_isHalfHourSlot() {
        return doctor_isHalfHourSlot;
    }
}
