package com.bug_tracker.GUI.Team_Head;

import com.bug_tracker.Business_Logic.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTeamMembers_Dialog_Controller implements Initializable {

    private ObservableList<Tester> allTesterList = FXCollections.observableArrayList();
    private ObservableList<Developer> allDevelopersList = FXCollections.observableArrayList();
    private ObservableList<Employee> allTeamMembersList = FXCollections.observableArrayList();


    //------------------------------------ Getters to Fetch all the lists ----------------------------------------------
    public ObservableList<Tester> getAllTesterList() {
        return allTesterList;
    }

    public ObservableList<Developer> getAllDevelopersList() {
        return allDevelopersList;
    }

    public ObservableList<Employee> getAllTeamMembersList() {
        return allTeamMembersList;
    }

    //------------------------------------------- Developer Table ------------------------------------------------------
    @FXML
    private TableView<Developer> Developers_TableView;

    @FXML
    private TableColumn<Developer, String> col_developerContactNo;

    @FXML
    private TableColumn<Developer, String> col_developerDesignation;

    @FXML
    private TableColumn<Developer, String> col_developerEmail;

    @FXML
    private TableColumn<Developer, String> col_developerName;
    //------------------------------------------- Developer Table ------------------------------------------------------


    //----------------------------------------- Team Members Table -----------------------------------------------------
    @FXML
    private TableView<Employee> TeamMembers_TableView;
    @FXML
    private TableColumn<Employee, String > col_teamMembersContactNo;

    @FXML
    private TableColumn<Employee, String > col_teamMembersDesignation;

    @FXML
    private TableColumn<Employee, String > col_teamMembersEmail;

    @FXML
    private TableColumn<Employee, String > col_teamMembersName;
    //----------------------------------------- Team Members Table -----------------------------------------------------


    //---------------------------------------- Tester Table View -------------------------------------------------------
    @FXML
    private TableView<Tester> Tester_TableView;

    @FXML
    private TableColumn<Tester, String> col_testerContact;

    @FXML
    private TableColumn<Tester, String> col_testerDesignation;

    @FXML
    private TableColumn<Tester, String> col_testerEmail;

    @FXML
    private TableColumn<Tester, String> col_testerName;
    //---------------------------------------- Tester Table View -------------------------------------------------------

    @FXML
    private Label insertStatus;

    @FXML
    private TextField teamMembersKeyword;

    @FXML
    void insertSelectedDeveloper(ActionEvent event) {
        Developer selectedDeveloper =Developers_TableView.getSelectionModel().getSelectedItem();

        if (selectedDeveloper == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Developer is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Developer From the table that you want to delete");
            alert.showAndWait();
            return;
        }

        //CHECK IF DEVELOPER IS ALREADY SELECTED
        boolean insertDeveloperFlag=true;
        for(Employee iteratorDeveloper : allTeamMembersList)
        {
            if(iteratorDeveloper.getMongoId() == selectedDeveloper.getMongoId())
                insertDeveloperFlag=false;

        }
        if(insertDeveloperFlag==true)
        {
            allTeamMembersList.add(selectedDeveloper);
            TeamMembers_TableView.setItems(allTeamMembersList);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Developer is already selected");
            alert.setHeaderText(null);
            alert.setContentText("Developer is already selected from the table.. Kindly selected another developer or review your choice..");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    void insertSelectedTester(ActionEvent event) {
        Tester selectedTester =Tester_TableView.getSelectionModel().getSelectedItem();

        if (selectedTester == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Tester is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Tester From the table that you want to delete");
            alert.showAndWait();
            return;
        }


        //CHECK IF TESTER IS ALREADY SELECTED
        boolean insertDeveloperFlag=true;
        for(Employee iteratorTester : allTeamMembersList)
        {
            if(iteratorTester.getMongoId() == selectedTester.getMongoId())
                insertDeveloperFlag=false;

        }
        if(insertDeveloperFlag==true)
        {
            allTeamMembersList.add(selectedTester);
            TeamMembers_TableView.setItems(allTeamMembersList);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Developer is already selected");
            alert.setHeaderText(null);
            alert.setContentText("Developer is already selected from the table.. Kindly selected another developer or review your choice..");
            alert.showAndWait();
            return;
        }

    }

    @FXML
    void perform_UpdateDetails(ActionEvent event) {
        TeamMembers_TableView.setItems(allTeamMembersList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //--------------------------------- For Tester Table ----------------------------------------------------------
        col_testerName.setCellValueFactory(new PropertyValueFactory<Tester, String>("name"));
        col_testerContact.setCellValueFactory(new PropertyValueFactory<Tester,String>("contactNo"));
        col_testerEmail.setCellValueFactory(new PropertyValueFactory<Tester, String>("email"));
        col_testerDesignation.setCellValueFactory(new PropertyValueFactory<Tester, String>("designation"));

        allTesterList.setAll(Tester.Get_All_Testers_Records_DB());
        Tester_TableView.setItems(allTesterList);
        //--------------------------------------------------------------------------------------------------------------


        //--------------------------------- For Developer Table --------------------------------------------------------
        col_developerName.setCellValueFactory(new PropertyValueFactory<Developer, String>("name"));
        col_developerContactNo.setCellValueFactory(new PropertyValueFactory<Developer,String>("contactNo"));
        col_developerEmail.setCellValueFactory(new PropertyValueFactory<Developer, String>("email"));
        col_developerDesignation.setCellValueFactory(new PropertyValueFactory<Developer, String>("designation"));

        allDevelopersList.setAll(Developer.Get_All_Developers_Records());
        Developers_TableView.setItems(allDevelopersList);
        //--------------------------------------------------------------------------------------------------------------

        //--------------------------------- For Team Head Table --------------------------------------------------------
        col_teamMembersName.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
        col_teamMembersContactNo.setCellValueFactory(new PropertyValueFactory<Employee,String>("contactNo"));
        col_teamMembersEmail.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        col_teamMembersDesignation.setCellValueFactory(new PropertyValueFactory<Employee, String>("designation"));

    }

    @FXML
    void performRemoveTeamMember(ActionEvent event) {
        Employee selectedTeamMember =TeamMembers_TableView.getSelectionModel().getSelectedItem();
        if(selectedTeamMember ==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Team Member is selected");
            alert.setHeaderText(null);
            alert.setContentText("Please First Select a Team Member From the table that you want to delete");
            alert.showAndWait();
            return;
        }

        //REMOVE THE SELECTED TEAM HEAD FROM THE LIST AND ALSO UPDATE THE TABLE VIEW
        int counter=0;
        for(Employee iteratorTeamHead : allTeamMembersList)
        {
            if(iteratorTeamHead.getMongoId() == selectedTeamMember.getMongoId())
            {
                allTeamMembersList.remove(counter);
                break;
            }
            counter++;
        }

        //NOW UPDATE THE TABLE VIEW OF TEAM MEMBERS
        TeamMembers_TableView.setItems(allTeamMembersList);
    }
}
