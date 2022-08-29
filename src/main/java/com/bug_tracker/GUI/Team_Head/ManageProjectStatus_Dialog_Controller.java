package com.bug_tracker.GUI.Team_Head;

import com.bug_tracker.Business_Logic.Bug_Tracker;
import com.bug_tracker.Business_Logic.Developer;
import com.bug_tracker.Business_Logic.Project;
import com.bug_tracker.Business_Logic.Tester;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageProjectStatus_Dialog_Controller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private TextField clientName;

    @FXML
    private TextArea projectDescription;

    @FXML
    private DatePicker projectEndingDate;

    @FXML
    private TextField projectName;

    @FXML
    private DatePicker projectStartingDate;

    @FXML
    private TextField repositoryLink;

    @FXML
    private TextField currentProjectStatus;

    private Project selectedProject;

    @FXML
    void performManageProjectStatus(ActionEvent event) {
        Bug_Tracker instance = Bug_Tracker.getInstance();

        selectedProject.setProjectCurrentStatus(currentProjectStatus.getText());
        if(selectedProject !=null)
        {
            instance.manageProjectStatus(selectedProject,TeamHead_MainScreen_Controller.currentTeamHead_Object);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Project Current Status");
            alert.setHeaderText(null);
            alert.setContentText("Are You sure to change the project current status to following \n"+selectedProject.getProjectCurrentStatus());
            alert.showAndWait();
            return;
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Project Current Status");
            alert.setHeaderText(null);
            alert.setContentText("Please First Fill the update project status text field");
            alert.showAndWait();
            return;
        }
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject=selectedProject;
        this.selectedProject.setProjectTeamHead(TeamHead_MainScreen_Controller.currentTeamHead_Object);

        //----------------- Filling the view with the respective values ------------------------------------------------
        projectName.setText(this.selectedProject.getProjectName());
        clientName.setText(this.selectedProject.getClientName());
        projectStartingDate.setValue(this.selectedProject.getProjectStartingDate());
        projectEndingDate.setValue(this.selectedProject.getProjectEndingDate());
        repositoryLink.setText(this.selectedProject.getRepositoryLink());
        projectDescription.setText(this.selectedProject.getProjectDescription());
        currentProjectStatus.setText(this.selectedProject.getProjectCurrentStatus());

        projectName.setDisable(true);
        clientName.setDisable(true);
        projectEndingDate.setDisable(true);
        repositoryLink.setDisable(true);
        projectDescription.setDisable(true);
        projectStartingDate.setDisable(true);


    }
}
