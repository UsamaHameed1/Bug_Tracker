package com.bug_tracker.GUI;

import com.bug_tracker.Business_Logic.*;
import com.bug_tracker.main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreen_Controller implements Initializable {

    public static Employee currentLoginEmployee;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private RadioButton asAdmin;

    @FXML
    private RadioButton asDeveloper;

    @FXML
    private RadioButton asTeamHead;

    @FXML
    private RadioButton asTester;

    @FXML
    private ToggleGroup loginRadio;

    @FXML
    private Button login_BTN;

    @FXML
    private TextField password;

    @FXML
    private TextField username;

    int loginRoleAs;

    @FXML
    void performLogin(ActionEvent event) throws IOException {
        //--------------------------------- Login As Admin -------------------------------------------------------------
        if (loginRoleAs == 1) {
            currentLoginEmployee = Admin.Check_Admin_Credentials(username.getText(), password.getText());

            if (currentLoginEmployee != null) {
                root = FXMLLoader.load(main.class.getResource("Admin_Main_Screen.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else if (currentLoginEmployee == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Admin Record Found");
                alert.setHeaderText(null);
                alert.setContentText("Please Check Your Login Username and Password");
                alert.showAndWait();
                return;
            }
        }
        //-------------------------------- Login As Team Head ----------------------------------------------------------
        else if (loginRoleAs == 4) {
            currentLoginEmployee = Team_Head.Check_TeamHead_Credentials(username.getText(), password.getText());
            if (currentLoginEmployee != null) {
                root = FXMLLoader.load(main.class.getResource("TeamHead_Main_Screen.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Team Head Record Found");
                alert.setHeaderText(null);
                alert.setContentText("Please Check Your Login Username and Password");
                alert.showAndWait();
                return;
            }
        }
        //--------------------------------------- As A Tester ----------------------------------------------------------
        else if (loginRoleAs ==2)
        {
            currentLoginEmployee = Tester.Check_Tester_Record(username.getText(), password.getText());
            if (currentLoginEmployee != null) {
                root = FXMLLoader.load(main.class.getResource("Tester_Main_Screen.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Tester Record Found");
                alert.setHeaderText(null);
                alert.setContentText("Please Check Your Login Username and Password");
                alert.showAndWait();
                return;
            }
        }

        //------------------------------------- As A Developer ---------------------------------------------------------
        else if(loginRoleAs ==3)
        {
            currentLoginEmployee = Developer.Check_Developer_Record(username.getText(),password.getText());
            if(currentLoginEmployee !=null)
            {
                root = FXMLLoader.load(main.class.getResource("Developer_Main_Screen.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Developer Record Found");
                alert.setHeaderText(null);
                alert.setContentText("Please Check Your Login Username and Password");
                alert.showAndWait();
                return;
            }
        }
    }


    @FXML
    void getLoginRole(ActionEvent event) {
        if (asAdmin.isSelected())
            loginRoleAs = 1;
        else if (asTester.isSelected())
            loginRoleAs = 2;
        else if (asDeveloper.isSelected())
            loginRoleAs = 3;
        else if (asTeamHead.isSelected())
            loginRoleAs = 4;

        System.out.println("login choice\t" + loginRoleAs);
    }

    @FXML
    private ImageView imageView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("Assests/main.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }
}