package com.bug_tracker.GUI.Admin;

import com.bug_tracker.Business_Logic.Bug_Tracker;
import com.bug_tracker.Business_Logic.Project;

import com.bug_tracker.GUI.Tester.View_All_Working_Project_Expanded_Details_Dialog_Controller;
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

public class Admin_View_Project_Details_Controller  implements Initializable {
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


    private final ObservableList<Project> allProjectsDetails = FXCollections.observableArrayList();


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


        Bug_Tracker instance =Bug_Tracker.getInstance();

        allProjectsDetails.setAll(instance.getAllProjectRecords(AdminMainScreen_Controller.currentAdminObject));
        tableviewProjectDetails.setItems(allProjectsDetails);
    }

    @FXML
    void performExpandProjectDetails(ActionEvent event) throws IOException {
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

        int projectUniqueID =selectedProject.getProjectUniqueIdentifer();
        //first check in closed projects collection
        selectedProject = Project.Get_Closed_Project_Details(selectedProject.getProjectUniqueIdentifer());
        if(selectedProject ==null)
        {
            //*
            // if the project is not in the closed projects collection then it must be in the opened working projects
            // */
            selectedProject =selectedProject.getProjectDetails(projectUniqueID);
        }

        System.out.println("Selected project \n"+selectedProject);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(main.class.getResource("Tester_View_All_Working_Projects_Expanded_Details.fxml"));

        DialogPane expandedProjectDetails = fxmlLoader.load();
        View_All_Working_Project_Expanded_Details_Dialog_Controller expandedProjectDetailsDialogController = fxmlLoader.getController();
        expandedProjectDetailsDialogController.setSelectedProject(selectedProject);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(expandedProjectDetails);
        dialog.setTitle("Expanded Project Details");

        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {

            System.out.println("------------- OK -----------------------");


        } else if (clickedButton.get() == ButtonType.CANCEL) {
        }

    }
}
