package com.bug_tracker.GUI.Developer;

import com.bug_tracker.Business_Logic.Bug;
import com.bug_tracker.Business_Logic.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Developer_BugLog_Dialog_Controller implements Initializable {
    private Project selectedProject;

    @FXML
    private TableColumn<Bug, LocalDate> colAssignementDate;

    @FXML
    private TableColumn<Bug, String>  colAuthorName;

    @FXML
    private TableColumn<Bug, String>  colBugCurrentStatus;

    @FXML
    private TableColumn<Bug, String>  colBugDescription;

    @FXML
    private TableColumn<Bug, String> colBugID;

    @FXML
    private TableColumn<Bug, String>  colBugTitle;

    @FXML
    private TableColumn<Bug, String> colCategory;

    @FXML
    private TableColumn<Bug, LocalDate>  colClosingDate;

    @FXML
    private TableColumn<Bug, String>  colCodeLineNumber;

    @FXML
    private TableColumn<Bug, String>  colNoteMsg;

    @FXML
    private TableColumn<Bug, String>  colRepoCommitHash;

    @FXML
    private TableColumn<Bug, String> bugSolution;

    @FXML
    private TextField keywordToSearch;

    @FXML
    private TableView<Bug> tableviewBugLog;

    public void setSelectedProject(Project selectedProject) {
        System.out.println("xxxxxxxxxxxxxxx");
        this.selectedProject = selectedProject;

        listOfBugs.setAll(this.selectedProject.getList_Of_Bugs_Reported());
        tableviewBugLog.setItems(listOfBugs);

    }

    private ObservableList<Bug> listOfBugs = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colRepoCommitHash.setCellValueFactory(new PropertyValueFactory<Bug, String>("repositoryCommitHash"));
        colNoteMsg.setCellValueFactory(new PropertyValueFactory<Bug, String>("message"));
        colCodeLineNumber.setCellValueFactory(new PropertyValueFactory<Bug, String>("codeLineNumber"));
        colClosingDate.setCellValueFactory(new PropertyValueFactory<Bug, LocalDate>("closingDate"));
        colCategory.setCellValueFactory(new PropertyValueFactory<Bug, String>("bugCategory"));
        colBugTitle.setCellValueFactory(new PropertyValueFactory<Bug, String>("bugTitle"));
        colBugID.setCellValueFactory(new PropertyValueFactory<Bug, String>("bugUniqueID"));
        colBugDescription.setCellValueFactory(new PropertyValueFactory<Bug, String>("bugDescription"));
        colBugCurrentStatus.setCellValueFactory(new PropertyValueFactory<Bug, String>("bugStatus"));
        colAssignementDate.setCellValueFactory(new PropertyValueFactory<Bug, LocalDate>("bugAssignmentDate"));
        colAuthorName.setCellValueFactory(new PropertyValueFactory<Bug, String>("authorTesterUsername"));
        bugSolution.setCellValueFactory(new PropertyValueFactory<Bug, String>("describeSolution"));


        // listOfBugs.setAll(selectedProject.getList_Of_Bugs_Reported());
        tableviewBugLog.setItems(listOfBugs);

    }
}
