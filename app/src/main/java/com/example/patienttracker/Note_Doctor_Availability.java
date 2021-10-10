package com.example.patienttracker;

import java.util.Map;

public class Note_Doctor_Availability {
    private Map<String,Boolean> daysOfWeek;
    private Map<String,Boolean> timeSlots;
    private Boolean halfHourSlots;

    public Note_Doctor_Availability(){

    }

    public Note_Doctor_Availability(Map<String,Boolean> days,Map<String,Boolean> timeSlots,Boolean halfHourSlots){
        this.daysOfWeek = days;
        this.timeSlots = timeSlots;
        this.halfHourSlots = halfHourSlots;
    }

    public Map<String, Boolean> getDaysOfWeek() {
        return daysOfWeek;
    }

    public Map<String, Boolean> getTimeSlots() { return timeSlots; }

    public Boolean getHalfHourSlots() { return halfHourSlots; }
}

