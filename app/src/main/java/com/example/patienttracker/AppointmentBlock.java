package com.example.patienttracker;

public class AppointmentBlock {
    private String AppointmentDate;
    private String Name;
    private String Number;

    public AppointmentBlock(){
        //empty needed for FireStore
    }

    public AppointmentBlock(String appointmentDate, String name, String number){
        AppointmentDate = appointmentDate;
        Name = name;
        Number = number;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public String getName() {
        return Name;
    }

    public String getNumber() {
        return Number;
    }
}
