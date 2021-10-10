package com.example.patienttracker;

public class AppointmentBlock {
    private String AppointmentDate;
    private String DoctorName;
    private String DoctorNum;

    public AppointmentBlock(){
        //empty needed for FireStore
    }

    public AppointmentBlock(String AppDate, String DocName, String DocNum){
        AppointmentDate = AppDate;
        DoctorName = DocName;
        DoctorNum = DocNum;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public String getDoctorNum() {
        return DoctorNum;
    }
}
