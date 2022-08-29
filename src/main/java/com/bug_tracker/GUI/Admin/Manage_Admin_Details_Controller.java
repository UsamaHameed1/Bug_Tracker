package com.bug_tracker.GUI.Admin;

import com.bug_tracker.Business_Logic.*;
import com.bug_tracker.main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Manage_Admin_Details_Controller implements Initializable {
    @FXML
    private TableColumn<Admin, String> colDesignation;

    @FXML
    private TableColumn<Admin, String> colPassword;

    @FXML
    private TableColumn<Admin, String> colUserName;

    @FXML
    private TableColumn<Admin, String> col_CNIC;

    @FXML
    private TableColumn<Admin, String> col_ContactNo;

    @FXML
    private TableColumn<Admin, String> col_Email;

    @FXML
    private TableColumn<Admin, String> col_Name;

    @FXML
    private TableView<Admin> Admins_Details_Table;

    @FXML
    private TextField keywordField;

    private ObservableList<Admin> allAdmins = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_Name.setCellValueFactory(new PropertyValueFactory<Admin, String>("name"));
        col_CNIC.setCellValueFactory(new PropertyValueFactory<Admin, String>("cnic"));
        col_ContactNo.setCellValueFactory(new PropertyValueFactory<Admin, String>("contactNo"));
        col_Email.setCellValueFactory(new PropertyValueFactory<Admin, String>("email"));
        colUserName.setCellValueFactory(new PropertyValueFactory<Admin, String>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Admin, String>("userPassword"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Admin, String>("userPassword"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<Admin, String>("designation"));
//

        Bug_Tracker instance = Bug_Tracker.getInstance();

        allAdmins.setAll(instance.getAllAdminRecords());
        Admins_Details_Table.setItems(allAdmins);
    }

    @FXML
    void performRemoveAdmin(ActionEvent event) {
        Admin selectedAdmin = Admins_Details_Table.getSelectionModel().getSelectedItem();

        if (selectedAdmin == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Admin is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Admin From the table that you want to delete");
            alert.showAndWait();
            return;
        }

        //----------- Creating an alert for the confirmation of a delete of the admin ----------------------------------
        Alert alertDeleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertDeleteConfirm.setTitle("Delete A Admin From Record");
        alertDeleteConfirm.setHeaderText("Please Confirm the Delete of Admin");
        alertDeleteConfirm.setContentText("Admin Details which is to be deleted :\n" + selectedAdmin);

        Optional<ButtonType> result = alertDeleteConfirm.showAndWait();

        Bug_Tracker instance = Bug_Tracker.getInstance();
        selectedAdmin= (Admin)instance.getParticularAdmin(selectedAdmin);

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (instance.Delete_Admin(selectedAdmin)) {
                System.out.println("Admin Deleted....");
                try {
                    allAdmins.setAll(Admin.Get_All_Admins_Records());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Some Database Issue");
                    alert.setHeaderText(null);
                    alert.setContentText("There is an issue occurred while fetching from Database. Kindly check if there is a Negative value in quantity/price. Kindly see Database");
                    alert.showAndWait();
                    return;
                }
                allAdmins.setAll(instance.getAllAdminRecords());

                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Delete A Admin From Record");
                alertSuccess.setHeaderText("Your Selected Admin Is Delete From The System");
                alertSuccess.setContentText("Following Admin Is Deleted :\n" + selectedAdmin);
                alertSuccess.showAndWait();
            }

        }
    }

        @FXML
        void performUpdateDetails(ActionEvent event) throws IOException {
            Admin selectedAdmin = Admins_Details_Table.getSelectionModel().getSelectedItem();

            if(selectedAdmin ==null)
            {
                Alert alert= new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Admin is Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please Select A Admin From the table that you want to edit");
                alert.showAndWait();
                return;
            }

            Bug_Tracker instance= Bug_Tracker.getInstance();
            selectedAdmin = (Admin) instance.getParticularAdmin((Employee) selectedAdmin);


            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader.setLocation(main.class.getResource("Admin_Edit_Admin_Dialog.fxml"));

            DialogPane adminEditor=fxmlLoader.load();
            EditAdminDialog_Controller editAdminDialog_controller=fxmlLoader.getController();
            editAdminDialog_controller.setAdmin(selectedAdmin);

            Dialog<ButtonType> dialog =new Dialog<>();
            dialog.setDialogPane(adminEditor);
            dialog.setTitle("Edit A Admin Details");


            Optional<ButtonType> clickedButton =dialog.showAndWait();

            if(clickedButton.get()==ButtonType.OK)
            {
                try {
                    allAdmins.setAll(Admin.Get_All_Admins_Records());

                } catch (Exception e) {
                    Alert alert= new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Some Database Issue");
                    alert.setHeaderText(null);
                    alert.setContentText("There is an issue occurend while fetching from Database. Kindly check if there is a Negative value in quantity/price. Kindly see Database");
                    alert.showAndWait();
                    return;
                }
                allAdmins.setAll(Admin.Get_All_Admins_Records());
            }

        }

    }
