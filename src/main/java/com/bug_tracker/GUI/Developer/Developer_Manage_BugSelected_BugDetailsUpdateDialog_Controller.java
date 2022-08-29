package com.bug_tracker.GUI.Developer;

import com.bug_tracker.Business_Logic.*;
import com.bug_tracker.GUI.Tester.Manage_Bugs_Controller;
import com.bug_tracker.GUI.Tester.Tester_MainScreen_Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Developer_Manage_BugSelected_BugDetailsUpdateDialog_Controller implements Initializable {


    private Project selectedProject;
    private Bug selectedBug;

    //------------------ List that contains the Team Members --------------------
    private ObservableList<Tester> allTesterList = FXCollections.observableArrayList();
    private ObservableList<Developer> allDevelopersList = FXCollections.observableArrayList();
    private ObservableList<Employee> allTeamMembersList = FXCollections.observableArrayList();
    //------------------------------------------------------------------------------------------------------------------


    @FXML
    private ComboBox<String> assignToDeveloper;

    @FXML
    private TextField authorName;

    @FXML
    private DatePicker bugAssignmentDate;

    @FXML
    private TextArea bugDescription;

    @FXML
    private TextField bugRelatedCommitHasg;

    @FXML
    private TextField bugRelvantCodeLine;

    @FXML
    private TextField bugStatus;

    @FXML
    private TextField bugTitle;

    @FXML
    private ComboBox<String> bugCategory;

    @FXML
    private TableColumn<Developer, String> developerContactNo;

    @FXML
    private TableColumn<Developer, String> developerDesignation;

    @FXML
    private TableColumn<Developer, String> developerEmail;

    @FXML
    private TableColumn<Developer, String> developerName;

    @FXML
    private TableView<Developer> developer_TableView;

    @FXML
    private TextArea messageNote;

    @FXML
    private Button performReportBug;

    @FXML
    private TableColumn<Tester, String> testerContactNo;

    @FXML
    private TableColumn<Tester, String> testerDesignation;

    @FXML
    private TableColumn<Tester, String> testerEmail;

    @FXML
    private TableColumn<Tester, String> testerName;

    @FXML
    private TableView<Tester> tester_TableView;


    @FXML
    private TextArea describeSolution;

    @FXML
    private DatePicker bugClosingDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //------------------------ Ready The Combo Box For Bug Types ---------------------------------------------------
        ArrayList<String> bugTypes =new ArrayList<>();
        bugTypes.add("Critical");
        bugTypes.add("Highest");
        bugTypes.add("Medium");
        bugTypes.add("Low");
        bugTypes.add("Improvement Ticket");
        bugCategory.getItems().setAll(bugTypes);
        //--------------------------------------------------------------------------------------------------------------

        //------------------------ Ready the combo box for Developers List ---------------------------------------------
        ArrayList<String> developersListStr=new ArrayList<>();
        for(Developer dev : allDevelopersList)
        {
            developersListStr.add(dev.getName());
        }
        assignToDeveloper.getItems().setAll(developersListStr);
        //--------------------------------------------------------------------------------------------------------------

        //---------------------- Initialize The Project Developers and Testers Tables ----------------------------------
        // Developers Table
        //--------------------------------- For Developer Table --------------------------------------------------------
        developerName.setCellValueFactory(new PropertyValueFactory<Developer, String>("name"));
        developerContactNo.setCellValueFactory(new PropertyValueFactory<Developer, String>("contactNo"));
        developerEmail.setCellValueFactory(new PropertyValueFactory<Developer, String>("email"));
        developerDesignation.setCellValueFactory(new PropertyValueFactory<Developer, String>("designation"));
        developer_TableView.setItems(allDevelopersList);
        //--------------------------------------------------------------------------------------------------------------

        // Testers Table
        //--------------------------------- For Tester Table ----------------------------------------------------------
        testerName.setCellValueFactory(new PropertyValueFactory<Tester, String>("name"));
        testerContactNo.setCellValueFactory(new PropertyValueFactory<Tester, String>("contactNo"));
        testerEmail.setCellValueFactory(new PropertyValueFactory<Tester, String>("email"));
        testerDesignation.setCellValueFactory(new PropertyValueFactory<Tester, String>("designation"));
        tester_TableView.setItems(allTesterList);
        //--------------------------------------------------------------------------------------------------------------
    }

    public void setSelectedBug(Bug selectedBug) {
        this.selectedBug = selectedBug;


        authorName.setText(selectedBug.getAuthorTesterUsername());
        bugTitle.setText(selectedBug.getBugTitle());
        bugDescription.setText(selectedBug.getBugDescription());
        bugAssignmentDate.setValue(selectedBug.getBugAssignmentDate());
        bugStatus.setText(selectedBug.getBugStatus());
        bugRelvantCodeLine.setText(selectedBug.getBugStatus());
        bugRelvantCodeLine.setText(selectedBug.getCodeLineNumber());
        bugRelatedCommitHasg.setText(selectedBug.getRepositoryCommitHash());
        messageNote.setText(selectedBug.getMessage());
        bugCategory.setValue(selectedBug.getBugCategory());
        assignToDeveloper.setValue(selectedBug.getAssignedDeveloper().getUserName());

        //---- Get Project Details To Update The Tables ----------------------------------------------------------------

        this.selectedProject = Manage_Bug_Status_Dialog_OnlyReportedToDeveloper_Controller.getSelectedProject();
        allDevelopersList.setAll(this.selectedProject.getDevelopersTeam());
        allTesterList.setAll(this.selectedProject.getTestersTeam());

        authorName.setText(selectedBug.getAuthorTesterUsername());

        //--------------------------------- For Tester Table ----------------------------------------------------------
        testerName.setCellValueFactory(new PropertyValueFactory<Tester, String>("name"));
        testerContactNo.setCellValueFactory(new PropertyValueFactory<Tester, String>("contactNo"));
        testerEmail.setCellValueFactory(new PropertyValueFactory<Tester, String>("email"));
        testerDesignation.setCellValueFactory(new PropertyValueFactory<Tester, String>("designation"));
        tester_TableView.setItems(allTesterList);
        //--------------------------------------------------------------------------------------------------------------


        //--------------------------------- For Developer Table --------------------------------------------------------
        developerName.setCellValueFactory(new PropertyValueFactory<Developer, String>("name"));
        developerContactNo.setCellValueFactory(new PropertyValueFactory<Developer, String>("contactNo"));
        developerEmail.setCellValueFactory(new PropertyValueFactory<Developer, String>("email"));
        developerDesignation.setCellValueFactory(new PropertyValueFactory<Developer, String>("designation"));
        developer_TableView.setItems(allDevelopersList);
        //--------------------------------------------------------------------------------------------------------------

        //------------------------ Ready the combo box for Developers List ---------------------------------------------
        ArrayList<String> developersListStr=new ArrayList<>();
        for(Developer dev : allDevelopersList)
        {
            developersListStr.add(dev.getName());
        }
        assignToDeveloper.getItems().setAll(developersListStr);
        //--------------------------------------------------------------------------------------------------------------
    }


    @FXML
    void performUpdateBugStatus(ActionEvent event) {
        this.selectedBug.setClosingDate(bugClosingDate.getValue());
        this.selectedBug.setDescribeSolution(describeSolution.getText());

        if(bugStatus.getText().compareTo("bug-closed") ==0)
            this.selectedBug.setBugStatus(bugStatus.getText().toLowerCase()+" & waiting for tester reply");
        else
            this.selectedBug.setBugStatus(bugStatus.getText());


        Developer_Main_Screen_Controller.getCurrentDeveloperObject().updateBugStatus(this.selectedBug,Manage_Bugs_Main_Screen_Controller.selectedProject.getProjectUniqueIdentifer());
    }

    public Bug getSelectedBug() {
        return selectedBug;
    }
}
