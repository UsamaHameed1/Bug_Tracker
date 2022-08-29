package com.bug_tracker.GUI.Tester;

import com.bug_tracker.Business_Logic.Tester;
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


public class Tester_MainScreen_Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public static Tester currentTesterObject;

    public static Tester getCurrentTesterObject() {
        return currentTesterObject;
    }

    @FXML
    private StackPane contentArea;

    @FXML
    private Label loggedInAs;


    @FXML
    void performBugLogProject(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Tester_Bugs_Log_Project.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performGoToDashboard(ActionEvent event) throws IOException {
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
    void performManageBugs(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Tester_Manage_Bugs.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performReportBug(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Tester_Report_Bug.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performViewAllWorkingProjects(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Tester_View_All_Working_Projects.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performGenerateProjectReport(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInAs.setText(LoginScreen_Controller.currentLoginEmployee.getName());
        currentTesterObject =(Tester) LoginScreen_Controller.currentLoginEmployee;
        currentTesterObject.getAllWorkingProjects();
    }
}
