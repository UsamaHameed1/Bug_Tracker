package com.bug_tracker.GUI.Developer;

import com.bug_tracker.Business_Logic.Bug_Tracker;
import com.bug_tracker.Business_Logic.Project;
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

public class Manage_Bugs_Main_Screen_Controller implements Initializable {

    public static Project selectedProject;

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


    private ObservableList<Project> workingProjectDetails = FXCollections.observableArrayList();


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
        workingProjectDetails.setAll(instance.getAllProjectsofDeveloper(Developer_Main_Screen_Controller.getCurrentDeveloperObject()));
        tableviewProjectDetails.setItems(workingProjectDetails);
    }


    @FXML
    void performManageBug(ActionEvent event) throws IOException {

        Bug_Tracker instance =Bug_Tracker.getInstance();
        selectedProject = tableviewProjectDetails.getSelectionModel().getSelectedItem();

        if (selectedProject == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Project is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Project From the table that you want to delete");
            alert.showAndWait();
            return;
        }


        selectedProject = instance.getParticularProject(selectedProject.getProjectUniqueIdentifer());

        System.out.println("Selected project \n" + selectedProject);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(main.class.getResource("Developer_Manage_Bugs_List_of_Bugs_Current_Dev.fxml"));

        DialogPane reportBugDialogWindow = fxmlLoader.load();
        Manage_Bug_Status_Dialog_OnlyReportedToDeveloper_Controller manage_bug_dialog_controller = fxmlLoader.getController();
        manage_bug_dialog_controller.setSelectedProject(selectedProject);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(reportBugDialogWindow);
        dialog.setTitle("Expanded Project Details");

        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {
            System.out.println("------------- OK -----------------------");
        } else if (clickedButton.get() == ButtonType.CANCEL) {
        }
    }
}
