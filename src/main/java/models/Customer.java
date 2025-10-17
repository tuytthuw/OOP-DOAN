package models;

import enums.Tier;

import java.time.LocalDate;

public class Customer extends Person {
    private Tier memberTier;
    private String notes;
    private int points;
    private LocalDate lastValidDate;
    //Constructor
    public Customer(String id, String fullName, String phoneNumber, String email, String address, LocalDate dateOfBirth, String gender, LocalDate createdDate, )
}
