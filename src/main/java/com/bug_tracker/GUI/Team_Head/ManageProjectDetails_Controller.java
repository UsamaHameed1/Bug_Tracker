package com.bug_tracker.GUI.Team_Head;




import com.bug_tracker.Business_Logic.Bug;
import com.bug_tracker.Business_Logic.Bug_Tracker;
import com.bug_tracker.Business_Logic.Project;
import com.bug_tracker.Business_Logic.Team_Head;
import com.bug_tracker.GUI.LoginScreen_Controller;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageProjectDetails_Controller implements Initializable {

    @FXML
    private TableColumn<Project, String> colClientName;

    @FXML
    private TableColumn<Project, String> colGithubRepoLink;

    @FXML
    private TableColumn<Project, String> projectCurrentStatus;

    @FXML
    private TableColumn<Project, LocalDate> colEndingDate;

    @FXML
    private TableColumn<Project, LocalDate> colStartingDate;

    @FXML
    private TableColumn<Project, String> colProjectDescription;

    @FXML
    private TableColumn<Project, String> colProjectName;

    @FXML
    private TableColumn<Project, String> colProjectUniqueID;

    @FXML
    private TextField keywordToSearch;

    @FXML
    private TableView<Project> tableviewProjectDetails;


    private ObservableList<Project> allProjectDetails = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProjectName.setCellValueFactory(new PropertyValueFactory<Project, String>("projectName"));
        colClientName.setCellValueFactory(new PropertyValueFactory<Project, String>("clientName"));
        colGithubRepoLink.setCellValueFactory(new PropertyValueFactory<Project, String>("repositoryLink"));
        colProjectUniqueID.setCellValueFactory(new PropertyValueFactory<Project, String>("projectUniqueIdentifer"));
        colStartingDate.setCellValueFactory(new PropertyValueFactory<Project, LocalDate>("projectStartingDate"));
        colEndingDate.setCellValueFactory(new PropertyValueFactory<Project, LocalDate>("projectEndingDate"));
        colProjectDescription.setCellValueFactory(new PropertyValueFactory<Project, String>("projectDescription"));
        projectCurrentStatus.setCellValueFactory(new PropertyValueFactory<Project, String>("projectCurrentStatus"));

        Team_Head loggedInTeamHead = (Team_Head) LoginScreen_Controller.currentLoginEmployee;
        Bug_Tracker instance = Bug_Tracker.getInstance();
        allProjectDetails.setAll(instance.getAllProjectsOfTeamHead(loggedInTeamHead));

        //update table view
        tableviewProjectDetails.setItems(allProjectDetails);
    }


    @FXML
    void performRemoveProjectDetails(ActionEvent event) {
        Project selectedProject = tableviewProjectDetails.getSelectionModel().getSelectedItem();

        Bug_Tracker instance =Bug_Tracker.getInstance();


        if(selectedProject ==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Project is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Project From the table that you want to delete");
            alert.showAndWait();
            return;
        }
        else
        {
            selectedProject.setProjectCurrentStatus("Project Removed By the Team Head " + LoginScreen_Controller.currentLoginEmployee.getName());

            Team_Head teamHead =(Team_Head) LoginScreen_Controller.currentLoginEmployee;
            selectedProject.setProjectTeamHead(teamHead);
            //----------- Creating an alert for the confirmation of a delete of the admin ----------------------------------
            Alert alertDeleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertDeleteConfirm.setTitle("Delete A Project From Record");
            alertDeleteConfirm.setHeaderText("Please Confirm the Delete of Project");
            alertDeleteConfirm.setContentText("Project Details which is to be deleted :\n" + selectedProject);

            Optional<ButtonType> result = alertDeleteConfirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                Team_Head currentLoginInTeamHeads =(Team_Head) LoginScreen_Controller.currentLoginEmployee;
                instance.removeProjectDetails(selectedProject,currentLoginInTeamHeads);

                // Refresh the table view after the remove operations
                Team_Head loggedInTeamHead = (Team_Head) LoginScreen_Controller.currentLoginEmployee;
                allProjectDetails.setAll(loggedInTeamHead.getAllWorkingProjectList());
                tableviewProjectDetails.setItems(allProjectDetails);
            }
        }
    }

    @FXML
    void performUpdateProjectDetails(ActionEvent event) throws IOException {

        Project selectedProject = tableviewProjectDetails.getSelectionModel().getSelectedItem();

        if(selectedProject ==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Project is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Project From the table that you want to delete");
            alert.showAndWait();
            return;
        }


        selectedProject = Project.getProjectDetails(selectedProject.getProjectUniqueIdentifer());

        System.out.println("Selected project \n"+selectedProject);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(main.class.getResource("TeamHead_Manage_Sub_UpdateProjectDetails_Dialog.fxml"));

        DialogPane teamHeadDialogPane = fxmlLoader.load();
        UpdateProjectDetails_Dialog_Controller updateProjectDetailsDialogController = fxmlLoader.getController();
        updateProjectDetailsDialogController.setSelectedProject(selectedProject);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(teamHeadDialogPane);
        dialog.setTitle("Update Project Details");




        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {

            System.out.println("------------- OK -----------------------");
            System.out.println(selectedProject);
            // REFRESH THE PROJECT TABLE VIEW
            Team_Head loggedInTeamHead = (Team_Head) LoginScreen_Controller.currentLoginEmployee;
            allProjectDetails.setAll(loggedInTeamHead.getAllWorkingProjectList());
            tableviewProjectDetails.setItems(allProjectDetails);

        } else if (clickedButton.get() == ButtonType.CANCEL) {

            // REFRESH THE PROJECT TABLE VIEW
            Team_Head loggedInTeamHead = (Team_Head) LoginScreen_Controller.currentLoginEmployee;
            allProjectDetails.setAll(loggedInTeamHead.getAllWorkingProjectList());
            tableviewProjectDetails.setItems(allProjectDetails);
        }
    }


}

