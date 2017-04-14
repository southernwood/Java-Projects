package com.cleo.contacts.datamodel;

/**
 * Created by sensen on 2017-04-14.
 */

import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty firstName = new SimpleStringProperty("");
    private SimpleStringProperty lastName = new SimpleStringProperty("");
    private SimpleStringProperty phoneNumber = new SimpleStringProperty("");
    private SimpleStringProperty emailAddress = new SimpleStringProperty("");
    private SimpleStringProperty notes = new SimpleStringProperty("");


    public Contact() {
    }

    public Contact(String firstName, String lastName, String phoneNumber, String emailAddress, String notes) {
        firstName = replaceEmpty(firstName);
        lastName = replaceEmpty(lastName);
        phoneNumber = replaceEmpty(phoneNumber);
        emailAddress = replaceEmpty(emailAddress);
        notes = replaceEmpty(notes);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.phoneNumber.set(phoneNumber);
        this.notes.set(notes);
        this.emailAddress.set(emailAddress);
    }
    public String replaceEmpty(String input) {
        if (input.length() == 0) return " ";
        return input;
    }
    public String getEmailAddress() { return emailAddress.get();}
    public void setEmailAddress(String emailAddress) {
        emailAddress = replaceEmpty(emailAddress);
        this.emailAddress.set(emailAddress);
    }
    public SimpleStringProperty emailAddressProperty() { return emailAddress; }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        firstName = replaceEmpty(firstName);
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        lastName = replaceEmpty(lastName);
        this.lastName.set(lastName);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        phoneNumber = replaceEmpty(phoneNumber);
        this.phoneNumber.set(phoneNumber);
    }

    public String getNotes() {
        return notes.get();
    }

    public SimpleStringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        notes = replaceEmpty(notes);
        this.notes.set(notes);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName=" + firstName +
                ", lastName=" + lastName +
                ", phoneNumber=" + phoneNumber +
                ", notes=" + notes +
                '}';
    }
}
