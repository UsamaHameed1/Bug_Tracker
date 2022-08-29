package com.bug_tracker.GUI.Admin;

import com.bug_tracker.Business_Logic.Bug_Tracker;
import com.bug_tracker.Business_Logic.Developer;
import com.bug_tracker.Business_Logic.Team_Head;
import com.bug_tracker.Business_Logic.Tester;
import com.bug_tracker.main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterEmployee_Controller implements Initializable {

    @FXML
    private TextField empCNIC;

    @FXML
    private TextField empContactNo;

    @FXML
    private TextField empEmail;

    @FXML
    private TextField empName;

    @FXML
    private TextField empPassword;

    @FXML
    private TextField empUserName;

    @FXML
    private Button registerEmp_BTN;

    @FXML
    private Label registerEmployee_Status;


    //---------- Radio Buttons ----------------------
    @FXML
    private RadioButton asDeveloper;

    @FXML
    private RadioButton asTeamHead;

    @FXML
    private RadioButton asTester;

    @FXML
    private ToggleGroup registerAsChoice;

    private int registerOptionSelected;

    @FXML
    void perform_EmployeeRegistration(ActionEvent event) {
        String empNameStr = empName.getText();
        String empCNICStr = empCNIC.getText();
        String empContactStr = empContactNo.getText();
        String empUserNameStr = empUserName.getText();
        String empUserPasswordStr = empPassword.getText();
        String empEmailStr = empEmail.getText();

        Bug_Tracker instance=Bug_Tracker.getInstance();

        if (registerOptionSelected == 1)      //AS Team Head
        {
            registerEmployee_Status.setText("New Team Head Added");
            Team_Head newTeamHead = (Team_Head) instance.registerEmployee(empNameStr, empCNICStr, empContactStr, empUserNameStr, empUserPasswordStr,empEmailStr,registerOptionSelected,AdminMainScreen_Controller.currentAdminObject);
            System.out.println(newTeamHead);
        } else if (registerOptionSelected == 2)        //AS Developer
        {
            Developer newDeveloper =(Developer) instance.registerEmployee(empNameStr, empCNICStr, empContactStr, empUserNameStr, empUserPasswordStr,empEmailStr,registerOptionSelected,AdminMainScreen_Controller.currentAdminObject);
            System.out.println(newDeveloper);
            registerEmployee_Status.setText("New Developer Added");
        } else if (registerOptionSelected == 3)        //Tester
        {
            System.out.println(registerOptionSelected);
            registerEmployee_Status.setText("New Tester Added");
            Tester newTester =(Tester) instance.registerEmployee(empNameStr, empCNICStr, empContactStr, empUserNameStr, empUserPasswordStr,empEmailStr,registerOptionSelected,AdminMainScreen_Controller.currentAdminObject);
            System.out.println(newTester);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Employee Has been added to the database");
        alert.setHeaderText(null);
        alert.setContentText("New Employee has been successfully added in the database");
        alert.showAndWait();


        // reset all the fields in the form
        empName.clear();
        empCNIC.clear();
        empContactNo.clear();
        empUserName.clear();
        empPassword.clear();
        empEmail.clear();
        registerEmployee_Status.setText("");
        return;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        asDeveloper.setToggleGroup(registerAsChoice);
        asTeamHead.setToggleGroup(registerAsChoice);
        asTester.setToggleGroup(registerAsChoice);
    }


    @FXML
    void getRole(ActionEvent event) {
        if (asTester.isSelected())
            registerOptionSelected = 3;
        else if (asTeamHead.isSelected())
            registerOptionSelected = 1;
        else if (asDeveloper.isSelected())
            registerOptionSelected = 2;
    }

    private void clearAllField() {
        empCNIC.clear();
        empContactNo.clear();
        empEmail.clear();
        empName.clear();
        empPassword.clear();
        empUserName.clear();
        registerEmployee_Status.setText("");
    }
}
