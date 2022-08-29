package com.bug_tracker.GUI.Admin;

import com.bug_tracker.Business_Logic.Developer;
import com.bug_tracker.Business_Logic.Employee;
import com.bug_tracker.Business_Logic.Team_Head;
import com.bug_tracker.Business_Logic.Tester;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class EditEmployeeDialog_Controller implements Initializable {

    Employee selectedEmployee;


    @FXML
    private RadioButton changeToDeveloper;

    @FXML
    private RadioButton changeToTeamHead;

    @FXML
    private RadioButton changeToTester;

    @FXML
    private TextField employeeCNIC;

    @FXML
    private TextField employeeContactNo;

    @FXML
    private TextField employeeEmail;

    @FXML
    private TextField employeeName;

    @FXML
    private TextField employeePassword;

    @FXML
    private Label employeeUpdateStatus;

    @FXML
    private TextField employeeUserName;

    @FXML
    private ToggleGroup updateDesignation;

    int selectedRadioBTN = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeToDeveloper.setToggleGroup(updateDesignation);
        changeToTeamHead.setToggleGroup(updateDesignation);
        changeToTester.setToggleGroup(updateDesignation);
    }

    @FXML
    void perform_UpdateDetails(ActionEvent event) {
        String prevDesignation = selectedEmployee.getDesignation();

        // getting radio button feedback
        if (changeToDeveloper.isSelected())
            selectedRadioBTN = 2;
        else if (changeToTeamHead.isSelected())
            selectedRadioBTN = 1;
        else if (changeToTester.isSelected())
            selectedRadioBTN = 3;

        // Getting Data From the Form First
        if (selectedRadioBTN == 1) //upgrade To Team Head
        {
            Team_Head newTeamHead = new Team_Head(employeeName.getText(), employeeCNIC.getText(), employeeContactNo.getText(), employeeEmail.getText(), employeeUserName.getText(), employeePassword.getText());



            // remove the previous object to from the data base
            if (prevDesignation == "Team Head") {
                Team_Head.Delete_TeamHead_Record(selectedEmployee.getMongoId());
            } else if (prevDesignation == "Developer") {
                Developer.Delete_Developer_Record(selectedEmployee.getMongoId());
            } else if (prevDesignation == "Tester") {
                Tester.Delete_Tester_Record(selectedEmployee.getMongoId());
            }

            //add the new team head to DB
            newTeamHead.insertTeamHeadInDB();

        } else if (selectedRadioBTN == 2) //Upgrade to Developer
        {
            Developer newDeveloper = new Developer(employeeName.getText(), employeeCNIC.getText(), employeeContactNo.getText(), employeeEmail.getText(), employeeUserName.getText(), employeePassword.getText());



            // remove the previous object to from the data base
            if (prevDesignation == "Team Head") {
                Team_Head.Delete_TeamHead_Record(selectedEmployee.getMongoId());
            } else if (prevDesignation == "Developer") {
                Developer.Delete_Developer_Record(selectedEmployee.getMongoId());
            } else if (prevDesignation == "Tester") {
                Tester.Delete_Tester_Record(selectedEmployee.getMongoId());
            }

            //add the new team head to DB
            newDeveloper.insertDeveloperInDB();
        } else if (selectedRadioBTN == 3) //upgrade to Tester
        {
            Tester newTester = new Tester(employeeName.getText(), employeeCNIC.getText(), employeeContactNo.getText(), employeeEmail.getText(), employeeUserName.getText(), employeePassword.getText());



            // remove the previous object to from the data base
            if (prevDesignation == "Team Head") {
                Team_Head.Delete_TeamHead_Record(selectedEmployee.getMongoId());
            } else if (prevDesignation == "Developer") {
                Developer.Delete_Developer_Record(selectedEmployee.getMongoId());
            } else if (prevDesignation == "Tester") {
                Tester.Delete_Tester_Record(selectedEmployee.getMongoId());
            }

            //add the new team head to DB
            newTester.insertNewTesterInDB();
        }

    }

    void setSelectedEmployee(Employee object) {
        this.selectedEmployee = object;
        employeeCNIC.setText(object.getCnic());
        employeeContactNo.setText(object.getContactNo());
        employeeName.setText(object.getName());
        employeePassword.setText(object.getUserPassword());
        employeeUserName.setText(object.getUserName());
        employeeEmail.setText(object.getEmail());

        String designation = object.getDesignation();

        if (designation == "Team Head") {
            changeToTeamHead.setSelected(true);
        } else if (designation == "Developer") {
            changeToDeveloper.setSelected(true);
        } else if (designation == "Tester") {
            changeToTester.setSelected(true);
        }

    }

    @FXML
    void getChoice(ActionEvent event) {
        if (changeToDeveloper.isSelected())
            selectedRadioBTN = 2;
        else if (changeToTeamHead.isSelected())
            selectedRadioBTN = 1;
        else if (changeToTester.isSelected())
            selectedRadioBTN = 3;

    }
}
