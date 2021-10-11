package com.example.patienttracker;

public class Note_Appointment {
    private String AppointmentDateTime;
    private String DocumentID;
    private String Name;
    private String Number;

    public Note_Appointment(){
        //empty needed for FireStore
    }

    public Note_Appointment(String appointmentDate, String documentID, String name, String number){
        AppointmentDateTime = appointmentDate;
        DocumentID = documentID;
        Name = name;
        Number = number;
    }

    public String getAppointmentDateTime() {
        return AppointmentDateTime;
    }

    public String getDocumentID() { return DocumentID; }

    public String getName() {
        return Name;
    }

    public String getNumber() {
        return Number;
    }
}
