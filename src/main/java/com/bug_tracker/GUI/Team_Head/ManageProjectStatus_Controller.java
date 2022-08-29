package com.bug_tracker.GUI.Team_Head;

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
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageProjectStatus_Controller implements Initializable {
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
    void performUpdateProjectStatus(ActionEvent event) throws IOException {

        Bug_Tracker instance = Bug_Tracker.getInstance();

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

        selectedProject = instance.getParticularProject(selectedProject.getProjectUniqueIdentifer());

        System.out.println("Selected project \n"+selectedProject);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(main.class.getResource("TeamHead_Manage_Project_Status_Dialog.fxml"));

        DialogPane teamHeadDialogPane = fxmlLoader.load();
        ManageProjectStatus_Dialog_Controller updateProjectStatusDialog = fxmlLoader.getController();
        updateProjectStatusDialog.setSelectedProject(selectedProject);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(teamHeadDialogPane);
        dialog.setTitle("Update Project Details");


        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {

            System.out.println("------------- OK -----------------------");
            System.out.println(selectedProject);

            // REFRESH THE PROJECT TABLE VIEW
            Team_Head loggedInTeamHead = (Team_Head) LoginScreen_Controller.currentLoginEmployee;
            allProjectDetails.setAll(instance.getAllProjectsOfTeamHead(loggedInTeamHead));
            tableviewProjectDetails.setItems(allProjectDetails);

        } else if (clickedButton.get() == ButtonType.CANCEL) {

            // REFRESH THE PROJECT TABLE VIEW
            Team_Head loggedInTeamHead = (Team_Head) LoginScreen_Controller.currentLoginEmployee;
            allProjectDetails.setAll(instance.getAllProjectsOfTeamHead(loggedInTeamHead));
            tableviewProjectDetails.setItems(allProjectDetails);
        }
    }

}
