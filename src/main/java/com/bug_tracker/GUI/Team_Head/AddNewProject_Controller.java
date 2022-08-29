package com.bug_tracker.GUI.Team_Head;

import com.bug_tracker.Business_Logic.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddNewProject_Controller implements Initializable {

    //------------------ List that contains the Team Members --------------------
    private ObservableList<Tester> allTesterList = FXCollections.observableArrayList();
    private ObservableList<Developer> allDevelopersList = FXCollections.observableArrayList();
    private ObservableList<Employee> allTeamMembersList = FXCollections.observableArrayList();


    @FXML
    private TextArea projectDescription;

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
    private DatePicker projectEndingDate;

    @FXML
    private TextField projectName;

    @FXML
    private DatePicker projectStartingDate;

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
    private TextField repositoryLink;

    @FXML
    void performAddTeamMembers(ActionEvent event) throws IOException {

        //------------------ First Clear the Table View of Tester and Developer ----------------------------------------
//        allTesterList.clear();
//        allDevelopersList.clear();
//        tester_TableView.setItems(allTesterList);
//        developer_TableView.setItems(allDevelopersList);
        //--------------------------------------------------------------------------------------------------------------

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(main.class.getResource("TeamHead_AddTeamMembers_Dialog.fxml"));

        DialogPane teamHeadDialogPane = fxmlLoader.load();
        AddTeamMembers_Dialog_Controller addTeamMembers_dialog_controller = fxmlLoader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(teamHeadDialogPane);
        dialog.setTitle("Add Team Members Details");


        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.get() == ButtonType.OK) {
            allTeamMembersList = addTeamMembers_dialog_controller.getAllTeamMembersList();

        } else if (clickedButton.get() == ButtonType.CANCEL) {

        }

        for (Employee iteratorEmployee : allTeamMembersList) {
            if (iteratorEmployee.getDesignation() == "Developer") {
                Developer newDev = new Developer(iteratorEmployee.getName(), iteratorEmployee.getCnic(), iteratorEmployee.getContactNo(), iteratorEmployee.getEmail(), iteratorEmployee.getUserName(), iteratorEmployee.getUserPassword());
                newDev.setMongoId(iteratorEmployee.getMongoId());
                allDevelopersList.add(newDev);
            } else if (iteratorEmployee.getDesignation() == "Tester") {
                Tester newTester = new Tester(iteratorEmployee.getName(), iteratorEmployee.getCnic(), iteratorEmployee.getContactNo(), iteratorEmployee.getEmail(), iteratorEmployee.getUserName(), iteratorEmployee.getUserPassword());
                newTester.setMongoId(iteratorEmployee.getMongoId());
                allTesterList.add(newTester);
            }
        }
        //-------------- UPDATE THE TESTER TABLE VIEW ------------------------------------------------------------------
        tester_TableView.setItems(allTesterList);

        //--------------- UPDATE THE DEVELOPER TABLE VIEW -------------------------------------------------------------=
        developer_TableView.setItems(allDevelopersList);
    }

    @FXML
    void performFinalizeProjectDetails(ActionEvent event) {

        Project newProject = new Project(projectName.getText(), clientName.getText(),projectStartingDate.getValue(), projectEndingDate.getValue(), projectDescription.getText(), repositoryLink.getText());

        //--------------------------- Setting the Stake Holders of A project -------------------------------------------
        newProject.setDevelopersTeam(new ArrayList<Developer>(developer_TableView.getItems()));
        newProject.setTestersTeam(new ArrayList<Tester>(tester_TableView.getItems()));
        newProject.setProjectTeamHead((Team_Head) LoginScreen_Controller.currentLoginEmployee);

        System.out.println(newProject);

        Bug_Tracker instance =Bug_Tracker.getInstance();
        instance.addNewProject(newProject,TeamHead_MainScreen_Controller.currentTeamHead_Object);

        clearAllField();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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


    private void clearAllField() {
        projectName.clear();
        clientName.clear();
        projectStartingDate.getEditor().clear();
        projectEndingDate.getEditor().clear();
        projectDescription.clear();
        repositoryLink.clear();


        allTesterList.clear();
        allDevelopersList.clear();

        tester_TableView.setItems(allTesterList);
        developer_TableView.setItems(allDevelopersList);
    }
}
