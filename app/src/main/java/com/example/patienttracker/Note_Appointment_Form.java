package com.example.patienttracker;

public class Note_Appointment_Form {

    private String description;
    private String prescription;

    public Note_Appointment_Form(){
        //empty constructor
    }

    public Note_Appointment_Form(String description, String prescription){
        this.description = description;
        this.prescription = prescription;
    }

    public String getDescription() {
        return description;
    }

    public String getPrescription() { return prescription; }
}
