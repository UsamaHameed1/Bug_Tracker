package com.bug_tracker.GUI.Admin;

import com.bug_tracker.Business_Logic.Admin;
import com.bug_tracker.Business_Logic.Bug_Tracker;
import com.bug_tracker.main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class RegisterNewAdmin_Controller {
    @FXML
    private TextField adminCNIC;

    @FXML
    private TextField adminContactNo;

    @FXML
    private TextField adminEmail;

    @FXML
    private TextField adminName;

    @FXML
    private TextField adminPassword;

    @FXML
    private TextField adminUsername;

    @FXML
    private TextField masterPassword;

    @FXML
    private Button registerNewAdmin;

    @FXML
    private Label registrationStatus;

    @FXML
    void perform_addNewAdmin(ActionEvent event) {

        String masterPasswordFromForm = masterPassword.getText();

        Bug_Tracker instance=Bug_Tracker.getInstance();

        Admin newAdmin =instance.registerAdmin(adminName.getText(),adminCNIC.getText(),adminContactNo.getText(), adminUsername.getText(), adminPassword.getText(),adminEmail.getText(),masterPasswordFromForm);
        registrationStatus.setText("New Admin Added Successfully");
        if(newAdmin ==null)
        {
            Alert alertDeleteConfirm = new Alert(Alert.AlertType.ERROR);
            alertDeleteConfirm.setTitle("Invalid Master Password");
            alertDeleteConfirm.setHeaderText("Cannot Add The New Admin Because You Don't Know The Master Password");
            alertDeleteConfirm.setContentText("Kindly Find The Master Owner To Add The New Admin");
            Optional<ButtonType> result = alertDeleteConfirm.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Admin Has been added to the database");
            alert.setHeaderText(null);
            alert.setContentText("New Admin has been successfully added in the database");
            alert.showAndWait();


            adminCNIC.clear();
            adminName.clear();
            adminContactNo.clear();
            adminEmail.clear();
            adminUsername.clear();
            adminPassword.clear();
            masterPassword.clear();
        }



    }
}
