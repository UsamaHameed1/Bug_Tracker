package com.bug_tracker.GUI.Tester;

import com.bug_tracker.Business_Logic.Developer;
import com.bug_tracker.Business_Logic.Employee;
import com.bug_tracker.Business_Logic.Project;
import com.bug_tracker.Business_Logic.Tester;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class View_All_Working_Project_Expanded_Details_Dialog_Controller {
    //------------------ List that contains the Team Members --------------------
    private ObservableList<Tester> allTesterList = FXCollections.observableArrayList();
    private ObservableList<Developer> allDevelopersList = FXCollections.observableArrayList();
    private ObservableList<Employee> allTeamMembersList = FXCollections.observableArrayList();


    private Project selectedProject;

    @FXML
    private TextField clientName;

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
    private TableColumn<Tester, String> testerContactNo;

    @FXML
    private TableColumn<Tester, String> testerDesignation;

    @FXML
    private TableColumn<Tester, String> testerEmail;

    @FXML
    private TableColumn<Tester, String>testerName;

    @FXML
    private TableView<Tester> tester_TableView;

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
        allDevelopersList.setAll(this.selectedProject.getDevelopersTeam());
        allTesterList.setAll(this.selectedProject.getTestersTeam());


        //----------------- Filling the view with the respective values ------------------------------------------------
        projectName.setText(this.selectedProject.getProjectName());
        clientName.setText(this.selectedProject.getClientName());
        projectStartingDate.setValue(this.selectedProject.getProjectStartingDate());
        projectEndingDate.setValue(this.selectedProject.getProjectEndingDate());
        repositoryLink.setText(this.selectedProject.getRepositoryLink());
        projectDescription.setText(this.selectedProject.getProjectDescription());


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

    }
}
