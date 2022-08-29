package com.bug_tracker.GUI.Team_Head;

import com.bug_tracker.Business_Logic.Team_Head;
import com.bug_tracker.GUI.LoginScreen_Controller;
import com.bug_tracker.main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeamHead_MainScreen_Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public static Team_Head currentTeamHead_Object;

    @FXML
    private StackPane contentArea;

    @FXML
    private Label loggedInAs;


    @FXML
    void performAddNewProject(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("TeamHead_Add_New_Project.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performGenerateProjectReport(ActionEvent event) {

    }

    @FXML
    void performGoToDashboard(ActionEvent event) {

    }

    @FXML
    void performLogout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Login_Screen.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void performProjectBugLog(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("TeamHead_Project_Bug_Log.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performManageProjectDetails(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("TeamHead_Manage_ProjectDetails.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performManageStatusofProject(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("TeamHead_Manage_Project_Status.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInAs.setText(LoginScreen_Controller.currentLoginEmployee.getName());
        currentTeamHead_Object=(Team_Head) LoginScreen_Controller.currentLoginEmployee;

    }
}
