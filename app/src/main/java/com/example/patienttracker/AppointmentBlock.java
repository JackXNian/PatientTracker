package com.example.patienttracker;

public class AppointmentBlock {
    private String AppointmentDateTime;
    private String DocumentID;
    private String Name;
    private String Number;

    public AppointmentBlock(){
        //empty needed for FireStore
    }

    public AppointmentBlock(String appointmentDate, String documentID, String name, String number){
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
