package com.example.patienttracker;

public class Note_Appointment {
    private String appointmentDateTime;
    private String documentID;
    private String name;
    private String number;

    public Note_Appointment(){
        //empty constructor
    }

    public Note_Appointment(String appointmentDate, String documentID, String name, String number){
        this.appointmentDateTime = appointmentDate;
        this.documentID = documentID;
        this.name = name;
        this.number = number;
    }

    public String getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public String getDocumentID() { return documentID; }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
