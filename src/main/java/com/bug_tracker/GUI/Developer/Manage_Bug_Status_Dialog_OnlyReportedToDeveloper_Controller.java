package com.bug_tracker.GUI.Developer;

import com.bug_tracker.Business_Logic.Bug;
import com.bug_tracker.Business_Logic.Bug_Tracker;
import com.bug_tracker.Business_Logic.Developer;
import com.bug_tracker.Business_Logic.Project;
import com.bug_tracker.GUI.Tester.Manage_Bug_Details_Perform_Manage_BTN_Dialog;
import com.bug_tracker.GUI.Tester.Tester_MainScreen_Controller;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Manage_Bug_Status_Dialog_OnlyReportedToDeveloper_Controller implements Initializable {
    private static Project selectedProject;

    @FXML
    private TableColumn<Bug, LocalDate> colAssignementDate;

    @FXML
    private TableColumn<Bug, String> colAuthorName;

    @FXML
    private TableColumn<Bug, String> colBugCurrentStatus;

    @FXML
    private TableColumn<Bug, String> colBugDescription;

    @FXML
    private TableColumn<Bug, String> colBugID;

    @FXML
    private TableColumn<Bug, String> colBugTitle;

    @FXML
    private TableColumn<Bug, String> colCategory;

    @FXML
    private TableColumn<Bug, LocalDate> colClosingDate;

    @FXML
    private TableColumn<Bug, String> colCodeLineNumber;

    @FXML
    private TableColumn<Bug, String> colNoteMsg;

    @FXML
    private TableColumn<Bug, String> colRepoCommitHash;

    @FXML
    private TextField keywordToSearch;

    @FXML
    private TableView<Bug> tableviewBugLog;

    private ArrayList<Bug> bugsReportedByLoggedInTesterOnly;


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

        // listOfBugs.setAll(selectedProject.getList_Of_Bugs_Reported());
        tableviewBugLog.setItems(listOfBugs);

    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;

        //--- we will only display the bugs reported by the tester who has reported them only---------------------------
        bugsReportedByLoggedInTesterOnly = new ArrayList<>();
        for (Bug currentBug : selectedProject.getList_Of_Bugs_Reported()) {
            //remove all the bugs which are closed from the table view
            if (currentBug.getAssignedDeveloper().getUserName().compareTo(Developer_Main_Screen_Controller.getCurrentDeveloperObject().getUserName()) == 0 && currentBug.getBugStatus().compareTo("bug-closed")!=0  && currentBug.isBugFixFlag()==false) {
                bugsReportedByLoggedInTesterOnly.add(currentBug);
            }
        }
        listOfBugs.setAll(bugsReportedByLoggedInTesterOnly);
        tableviewBugLog.setItems(listOfBugs);
    }

    @FXML
    void performManageBugDetails(ActionEvent event) throws IOException {

        Bug_Tracker instance =Bug_Tracker.getInstance();

        Bug selectedBug = tableviewBugLog.getSelectionModel().getSelectedItem();
        if (selectedBug == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Bug Record is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Bug Record From the table that you want to delete");
            alert.showAndWait();
            return;
        }


        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(main.class.getResource("Developer_Manage_Bug_Selected_Bug_Details_Update_Dialog.fxml"));

        DialogPane expandBugDetails = fxmlLoader.load();
        Developer_Manage_BugSelected_BugDetailsUpdateDialog_Controller expandedBugDetailsDialogController = fxmlLoader.getController();
        expandedBugDetailsDialogController.setSelectedBug(selectedBug);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(expandBugDetails);
        dialog.setTitle("Expanded Project Details");

        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {

            System.out.println("------------- OK -----------------------");
            Bug updatedBug = expandedBugDetailsDialogController.getSelectedBug();

            //*************************************************************************************************************************************************
            instance.manageStatusOfBug(Developer_Main_Screen_Controller.getCurrentDeveloperObject(),updatedBug,selectedProject.getProjectUniqueIdentifer());

            tableviewBugLog.setItems(listOfBugs);

            //--- we will only display the bugs reported by the tester who has reported them only---------------------------
            bugsReportedByLoggedInTesterOnly = new ArrayList<>();
            for (Bug currentBug : selectedProject.getList_Of_Bugs_Reported()) {
                //remove all the bugs which are closed from the table view
                if (currentBug.getAssignedDeveloper().getUserName().compareTo(Developer_Main_Screen_Controller.getCurrentDeveloperObject().getUserName()) == 0 && currentBug.getBugStatus().compareTo("bug-closed")!=0 && !currentBug.isBugFixFlag()) {
                    bugsReportedByLoggedInTesterOnly.add(currentBug);
                }
            }
            listOfBugs.setAll(bugsReportedByLoggedInTesterOnly);
            tableviewBugLog.setItems(listOfBugs);
        } else if (clickedButton.get() == ButtonType.CANCEL)
        {

        }
    }

    @FXML
    void performRemoveBugRecord(ActionEvent event) {
        Bug selectedBug = tableviewBugLog.getSelectionModel().getSelectedItem();

        if (selectedBug == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Bug Record is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Bug Record From the table that you want to delete");
            alert.showAndWait();
            return;
        }


        //Update From the Database
        Tester_MainScreen_Controller.getCurrentTesterObject().removeBug(selectedBug.getBugUniqueID(), selectedProject.getProjectUniqueIdentifer());

        for (int i = 0; i < bugsReportedByLoggedInTesterOnly.size(); i++) {
            if (bugsReportedByLoggedInTesterOnly.get(i).getBugUniqueID().compareTo(selectedBug.getBugUniqueID()) == 0) {
                bugsReportedByLoggedInTesterOnly.remove(i);
                break;
            }
        }

        //update the table view
        listOfBugs.setAll(bugsReportedByLoggedInTesterOnly);
        tableviewBugLog.setItems(listOfBugs);

    }

    static public Project getSelectedProject() {
        return selectedProject;
    }
}
