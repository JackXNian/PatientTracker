package com.example.patienttracker;

public class Note_Appointment_Detail {
    private String Description;
    private String Prescription;

    public Note_Appointment_Detail(){
        //empty constructor
    }

    public Note_Appointment_Detail(String description,String prescription){
        Description = description;
        Prescription = prescription;
    }

    public String getDescription() {
        return Description;
    }

    public String getPrescription() {
        return Prescription;
    }
}
