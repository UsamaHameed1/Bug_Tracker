package com.bug_tracker.GUI.Admin;

import com.bug_tracker.Business_Logic.Admin;
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

public class AdminMainScreen_Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static Admin currentAdminObject;
    @FXML
    private Label loggedInAs;

    @FXML
    private StackPane contentArea;

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
    void performRegisterEmployee(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Admin_Register_Employee.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performRegisterNewAdmin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Admin_Register_New_Admin.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void performViewProjectDetails(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Admin_View_All_Projects.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    void perrform_ManageEmployees(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Admin_Manage_Employee_Details.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }


    @FXML
    void perform_ManageAdmins(ActionEvent event) throws IOException {
        root = FXMLLoader.load(main.class.getResource("Admin_Manage_Admins_Detail.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedInAs.setText(LoginScreen_Controller.currentLoginEmployee.getName());
        currentAdminObject=(Admin) LoginScreen_Controller.currentLoginEmployee;
    }
}
