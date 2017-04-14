package com.cleo.contacts;

import com.cleo.contacts.datamodel.Contact;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created by sensen on 2017-04-14.
 */
public class ContactController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField notesField;
    @FXML
    private TextField emailAddressField;

    public Contact processResult() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesField.getText().trim();
        String emailAddress = emailAddressField.getText().trim();
        Contact newContact = new Contact(firstName, lastName, phoneNumber,emailAddress, notes);
        return newContact;
    }
    public void showEditContact(Contact selectedContact) {
        firstNameField.setText(selectedContact.getFirstName());
        lastNameField.setText(selectedContact.getLastName());
        phoneNumberField.setText(selectedContact.getPhoneNumber());
        emailAddressField.setText(selectedContact.getEmailAddress());
        notesField.setText(selectedContact.getNotes());
    }
    public void updateContact(Contact selectedContact) {
        selectedContact.setFirstName(firstNameField.getText().trim());
        selectedContact.setLastName(lastNameField.getText().trim());
        selectedContact.setPhoneNumber(phoneNumberField.getText().trim());
        selectedContact.setEmailAddress(emailAddressField.getText().trim());
        selectedContact.setNotes(notesField.getText().trim());
    }
}
