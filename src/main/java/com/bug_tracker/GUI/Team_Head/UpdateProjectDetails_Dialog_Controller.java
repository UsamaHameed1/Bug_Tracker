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
import java.util.Optional;
import java.util.ResourceBundle;


public class UpdateProjectDetails_Dialog_Controller implements Initializable {

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
        makeAllTeamMembersList();
    }


    @FXML
    void performFinalizeProjectDetails(ActionEvent event) {

        Bug_Tracker instance =Bug_Tracker.getInstance();

        Project newProject = new Project(projectName.getText(), clientName.getText(),projectStartingDate.getValue(), projectEndingDate.getValue(), projectDescription.getText(), repositoryLink.getText());
        newProject.setProjectUniqueIdentifer(this.selectedProject.getProjectUniqueIdentifer());

        //--------------------------- Setting the Stake Holders of A project -------------------------------------------
        newProject.setDevelopersTeam(new ArrayList<Developer>(developer_TableView.getItems()));
        newProject.setTestersTeam(new ArrayList<Tester>(tester_TableView.getItems()));
        newProject.setProjectTeamHead((Team_Head) LoginScreen_Controller.currentLoginEmployee);

        System.out.println(newProject);

        //--------------------------- Update the Records with new Update Records ---------------------------------------
        //In Project
        Team_Head currentLoginTeamHead =(Team_Head) LoginScreen_Controller.currentLoginEmployee;
        instance.updateProjectDetails(newProject,currentLoginTeamHead);
    }

    @FXML
    void performUpdateTeamMembers(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(main.class.getResource("TeamHead_Manage_Sub_SubUpdate_Manage_TeamMembers_Dialog.fxml"));

        DialogPane teamHeadDialogPane = fxmlLoader.load();
        UpdateProjectDetails_UpdateTeamMembers_Dialog_Controller updateProjectDetails_updateTeamMembers_dialog_controller = fxmlLoader.getController();
        updateProjectDetails_updateTeamMembers_dialog_controller.setAllTeamMembersList(this.allTeamMembersList);


        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(teamHeadDialogPane);
        dialog.setTitle("Add Team Members Details");


        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.get() == ButtonType.OK) {

            // First clear the old team members list and fill the list with the new members of the team
            allTeamMembersList.clear();
            allDevelopersList.clear();
            allTesterList.clear();

            // clear all table views
            tester_TableView.setItems(allTesterList);
            developer_TableView.setItems(allDevelopersList);

            this.allTeamMembersList = updateProjectDetails_updateTeamMembers_dialog_controller.getAllTeamMembersList();

            System.out.println("Update Team Members list\n"+allTeamMembersList);

        } else if (clickedButton.get() == ButtonType.CANCEL) {
                return;
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

    private void makeAllTeamMembersList()
    {
        //first iterate over the testers team list
        for(Employee iteratorTester : allTesterList)
        {
            this.allTeamMembersList.add(iteratorTester);
        }

        //then iterate over the developers team list
        for(Employee iteratorDeveloper : allDevelopersList)
        {
            this.allTeamMembersList.add(iteratorDeveloper);
        }
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
}

