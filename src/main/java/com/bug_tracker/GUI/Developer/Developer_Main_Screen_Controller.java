package com.bug_tracker.GUI.Developer;

import com.bug_tracker.Business_Logic.Developer;
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

public class Developer_Main_Screen_Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static Developer currentDeveloperObject;

    @FXML
    private StackPane contentArea;

    @FXML
    private Label loggedInAs;

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
    void performManageStatusOfBug(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Developer_Manage_Bug_Main_Screen.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performViewAllWorkingProjects(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Developer_View_All_Working_Projects.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performBugLogOfAProject(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Developer_Bug_Log.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentDeveloperObject= (Developer) LoginScreen_Controller.currentLoginEmployee;
        loggedInAs.setText(currentDeveloperObject.getName());
        currentDeveloperObject.getAllWorkingProjects();
    }

    public static Developer getCurrentDeveloperObject() {
        return currentDeveloperObject;
    }

    public static void setCurrentDeveloperObject(Developer currentDeveloperObject) {
        Developer_Main_Screen_Controller.currentDeveloperObject = currentDeveloperObject;
    }
}
