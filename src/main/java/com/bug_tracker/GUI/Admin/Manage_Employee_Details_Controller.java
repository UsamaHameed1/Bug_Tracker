package com.bug_tracker.GUI.Admin;

import com.bug_tracker.Business_Logic.*;
import com.bug_tracker.Business_Logic.Team_Head;
import com.bug_tracker.main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Manage_Employee_Details_Controller implements Initializable {
    @FXML
    private TableColumn<Employee, String> colDesignation;

    @FXML
    private TableColumn<Employee, String> colPassword;

    @FXML
    private TableColumn<Employee, String> colUserName;

    @FXML
    private TableColumn<Employee, String> col_CNIC;

    @FXML
    private TableColumn<Employee, String> col_ContactNo;

    @FXML
    private TableColumn<Employee, String> col_Email;

    @FXML
    private TableColumn<Employee, String> col_Name;

    @FXML
    private TableView<Employee> All_Employee_Table_Details;

    @FXML
    private TextField keywordField;

    private ObservableList<Employee> allEmployeesList = FXCollections.observableArrayList();

    public ArrayList<Employee> getAllEmployeeRecords() {

        Bug_Tracker instance = Bug_Tracker.getInstance();

        ArrayList<Team_Head> allTeamHeads = instance.getAllTeamHeadsRecords();
        ArrayList<Developer> allDevelopers = instance.getAllDevelopersRecords();
        ArrayList<Tester> allTesters = instance.getAllTestersRecords();

        ArrayList<Employee> allEmployees = new ArrayList<>();
        allEmployees.addAll(allTeamHeads);
        allEmployees.addAll(allDevelopers);
        allEmployees.addAll(allTesters);

        return allEmployees;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_Name.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
        col_CNIC.setCellValueFactory(new PropertyValueFactory<Employee, String>("cnic"));
        col_ContactNo.setCellValueFactory(new PropertyValueFactory<Employee, String>("contactNo"));
        col_Email.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        colUserName.setCellValueFactory(new PropertyValueFactory<Employee, String>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Employee, String>("userPassword"));
        colPassword.setCellValueFactory(new PropertyValueFactory<Employee, String>("userPassword"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<Employee, String>("designation"));

        allEmployeesList.setAll(getAllEmployeeRecords());
        All_Employee_Table_Details.setItems(allEmployeesList);
    }

    @FXML
    void performRemoveDetails(ActionEvent event) {
        Employee selectedEmployee = All_Employee_Table_Details.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Employee is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Employee From the table that you want to delete");
            alert.showAndWait();
            return;
        }

        //----------- Creating an alert for the confirmation of a delete of the Employee ----------------------------------
        Alert alertDeleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertDeleteConfirm.setTitle("Delete A Employee From Record");
        alertDeleteConfirm.setHeaderText("Please Confirm the Delete of Employee");
        alertDeleteConfirm.setContentText("Employee Details which is to be deleted :\n" + selectedEmployee);

        Bug_Tracker instance = Bug_Tracker.getInstance();

        Optional<ButtonType> result = alertDeleteConfirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (selectedEmployee.getDesignation().compareTo("Team Head") ==0)
            {

                instance.deleteTeamHead(selectedEmployee.getMongoId());

            } else if (selectedEmployee.getDesignation().compareTo("Tester") ==0) {

                instance.deleteTester(selectedEmployee.getMongoId());

            } else if (selectedEmployee.getDesignation().compareTo("Developer") ==0) {
                instance.deleteDeveloper( selectedEmployee.getMongoId());
            }
        }

        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
        alertSuccess.setTitle("Delete A Employee From Record");
        alertSuccess.setHeaderText("Your Selected Employee Is Delete From The System");
        alertSuccess.setContentText("Following Employee Is Deleted :\n" + selectedEmployee);
        alertSuccess.showAndWait();

        allEmployeesList.setAll(getAllEmployeeRecords());
        All_Employee_Table_Details.setItems(allEmployeesList);
    }

    @FXML
    void performUpdateDetails(ActionEvent event) throws IOException {
        Employee selectedEmployee = All_Employee_Table_Details.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Admin is Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please Select A Employee Record From the table that you want to edit");
            alert.showAndWait();
            return;
        }

        Bug_Tracker instance = Bug_Tracker.getInstance();

        // check the employee rank and get all the details from the data base
        if(selectedEmployee.getDesignation().compareTo("Team Head") ==0)
        {
            selectedEmployee = instance.getParticularTeamHeadRecord(selectedEmployee);
        }
        else if (selectedEmployee.getDesignation().compareTo("Developer") ==0)
        {
            selectedEmployee =instance.getParticularDeveloperRecord(selectedEmployee);
        }
        else if (selectedEmployee.getDesignation().compareTo("Tester") ==0)
        {
            selectedEmployee=instance.getParticularTesterRecord(selectedEmployee);
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(main.class.getResource("Admin_Edit_Employee_Dialog.fxml"));

        DialogPane employeeEditor = fxmlLoader.load();
        EditEmployeeDialog_Controller editEmployeeDialog_controller = fxmlLoader.getController();
        editEmployeeDialog_controller.setSelectedEmployee(selectedEmployee);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(employeeEditor);
        dialog.setTitle("Edit A Employee Details");

        Optional<ButtonType> clickedButton = dialog.showAndWait();


        if (clickedButton.get() == ButtonType.OK) {
            try {
                allEmployeesList.setAll(getAllEmployeeRecords());
                All_Employee_Table_Details.setItems(allEmployeesList);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Some Database Issue");
                alert.setHeaderText(null);
                alert.setContentText("There is an issue occurend while fetching from Database. Kindly check if there is a Negative value in quantity/price. Kindly see Database");
                alert.showAndWait();
                return;
            }

        }

        allEmployeesList.setAll(getAllEmployeeRecords());
        All_Employee_Table_Details.setItems(allEmployeesList);
    }

}
