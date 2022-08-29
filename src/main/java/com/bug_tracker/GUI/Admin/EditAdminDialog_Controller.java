package com.bug_tracker.GUI.Admin;

import com.bug_tracker.Business_Logic.Admin;
import com.bug_tracker.Business_Logic.Bug_Tracker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Optional;

public class EditAdminDialog_Controller {

    Admin selectedAdmin;

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
    private Label adminUpdateStatus;

    @FXML
    void perform_UpdateDetails(ActionEvent event) {

        selectedAdmin.setEmail(adminEmail.getText());
        selectedAdmin.setCnic(adminCNIC.getText());
        selectedAdmin.setContactNo(adminContactNo.getText());
        selectedAdmin.setUserName(adminUsername.getText());
        selectedAdmin.setName(adminName.getText());
        selectedAdmin.setUserPassword(adminPassword.getText());

        Admin.Update_Admin(selectedAdmin);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(adminName.getText() + " is going to be updated");
        alert.setHeaderText("Admin Record Update");
        alert.setContentText("Admin Record Update Pop Up Confirmation\n" +
                adminName.getText() + "\n" + adminEmail.getText() + "\n");

        Bug_Tracker instance= Bug_Tracker.getInstance();
        instance.updateAdmin(selectedAdmin);


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            adminUpdateStatus.setText("Update Successful");
        } else
        {
            adminUpdateStatus.setText("Update Unsuccessful");
        }
    }


    public void setAdmin(Admin selectedAdmin) {
        this.selectedAdmin  =selectedAdmin;
        adminCNIC.setText(selectedAdmin.getCnic());
        adminContactNo.setText(selectedAdmin.getContactNo());
        adminEmail.setText(selectedAdmin.getEmail());
        adminName.setText(selectedAdmin.getName());
        adminPassword.setText(selectedAdmin.getUserPassword());
        adminUsername.setText(selectedAdmin.getUserName());
    }

}
