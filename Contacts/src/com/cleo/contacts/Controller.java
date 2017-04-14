package com.cleo.contacts;

import com.cleo.contacts.datamodel.Contact;
import com.cleo.contacts.datamodel.ContactData;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private BorderPane mainBorderPane;
    private ContactData contactData;
    @FXML
    private TableView<Contact> contactsTable;
    private ContextMenu contextMenu;

    public void initialize() {
        //create a instance of contact dota
        contactData = new ContactData();
        contactData.loadContacts();
        System.out.println("successfully load data");
        contactsTable.setItems(contactData.getContacts());

        //handle the double clicked action
        contactsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    showEditDialog();
                }
            }
        });

    }


    @FXML
    public void showAddDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add a New Contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println("couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            ContactController controller = fxmlLoader.getController();
            Contact newContact = controller.processResult();
            contactData.addContact(newContact);
            contactData.saveContacts();
            System.out.println("Ok pressed");
        }else {
            System.out.println("Cancel pressed");
        }
    }
    @FXML
    public void showEditDialog() {
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you want to edit");
            alert.showAndWait();
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Contact");
        dialog.setHeaderText("fill in the content to edit");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println("couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //fill in the dialog with origin data
        ContactController contactController = fxmlLoader.getController();
        contactController.showEditContact(selectedContact);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            contactController.updateContact(selectedContact);
            contactData.saveContacts();
            System.out.println("Ok pressed");
        }else {
            System.out.println("Cancel pressed");
        }

    }
    public void handleDelete() {
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you want to delete");
            alert.showAndWait();
            return;
        }
        deleteContact(selectedContact);
    }
    @FXML
    public void handleKeyPressed (KeyEvent keyEvent){
        if (keyEvent.getCode().equals(KeyCode.DELETE) && contactsTable.getSelectionModel().getSelectedItem() != null){
            handleDelete();
        }
    }


    public void deleteContact(Contact selectedContact) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText("Delete "+selectedContact.getFirstName()+"'s contact information");
        alert.setContentText("Are you sure? Press Ok to confirm, or cancel to Back out.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contactData.deleteContact(selectedContact);
            contactData.saveContacts();
        }
    }
    public void handleExit() {
        Platform.exit();
    }
}
