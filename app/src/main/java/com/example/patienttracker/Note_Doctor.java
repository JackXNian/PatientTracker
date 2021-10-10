package com.example.patienttracker;

public class Note_Doctor {

    private String Phone;
    private String Email;
    private String Password;
    private String FirstName;
    private String LastName;
    private String ID;
    private String Fields;
    private String Qualifications;
    private String Years;

    public Note_Doctor(){

        //Empty constructor needed

    }

    public Note_Doctor(String phone, String email, String password, String firstName,
                       String lastName, String id, String fields, String qualifications,
                       String years)
    {
        this.Phone = phone;
        this.Email = email;
        this.Password = password;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.ID = id;
        this.Fields = fields;
        this.Qualifications = qualifications;
        this.Years = years;
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

    public String getFields() {
        return Fields;
    }

    public String getQualifications() {
        return Qualifications;
    }

    public String getYears() {
        return Years;
    }
}
