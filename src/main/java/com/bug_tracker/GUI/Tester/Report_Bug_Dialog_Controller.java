package com.bug_tracker.GUI.Tester;

import com.bug_tracker.Business_Logic.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Report_Bug_Dialog_Controller implements Initializable {

    private Project selectedProject;

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

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
        allDevelopersList.setAll(this.selectedProject.getDevelopersTeam());
        allTesterList.setAll(this.selectedProject.getTestersTeam());

        authorName.setText(Tester_MainScreen_Controller.getCurrentTesterObject().getName());

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
    void performReportBug(ActionEvent event) {
        System.out.println("perform report bug called");
        Developer developerDetails =null;
        for(Developer developer:allDevelopersList)
        {
            if(developer.getName().compareTo(assignToDeveloper.getValue())==0)
            {
                developerDetails=developer;
            }
        }

        Bug_Tracker instance = Bug_Tracker.getInstance();
        instance.reportANewBug(Tester_MainScreen_Controller.getCurrentTesterObject(), selectedProject.getProjectUniqueIdentifer() ,
                Tester_MainScreen_Controller.currentTesterObject.getUserName(), bugTitle.getText(),
                bugDescription.getText(),bugAssignmentDate.getValue(),bugStatus.getText(),bugRelvantCodeLine.getText(), bugRelatedCommitHasg.getText(),
                messageNote.getText(),bugCategory.getValue(),developerDetails);


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New Bug is Reported");
        alert.setHeaderText(null);
        alert.setContentText("The New Reported Bug is added in the database");
        alert.showAndWait();
    }
}

