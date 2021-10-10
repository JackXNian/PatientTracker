package com.example.patienttracker;

public class Note_Patient {

    private String Phone;
    private String Email;
    private String Password;
    private String FirstName;
    private String LastName;
    private String ID;

    public Note_Patient(){
        //Empty constructor needed
    }

    public Note_Patient(String phone, String email, String password, String firstName,
                        String lastName, String id)
    {
        this.Phone = phone;
        this.Email = email;
        this.Password = password;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.ID = id;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getID() {
        return ID;
    }
}
