package com.bug_tracker;

import com.bug_tracker.Business_Logic.Developer;

import com.bug_tracker.Database_Connectivity.Developer_DB_Connection_Handler;

import com.bug_tracker.Database_Connectivity.TeamHead_DB_Connection_Handler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class main extends Application {

    private static String masterPassword="123";
    @Override
    public void start(Stage stage) throws IOException {

        File file = new File("Assests/taskbar-img.png");
        Image taskBarIcon = new Image(file.toURI().toString());


        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("Login_Screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bug Tracker");
        stage.getIcons().add(taskBarIcon);
        stage.setScene(scene);
        stage.show();
    }

    public static String getMasterPassword()
    {
        return masterPassword;
    }
    public static void main(String[] args) {
        Developer_DB_Connection_Handler dbHandler = new Developer_DB_Connection_Handler();

       ArrayList<Developer>devs= dbHandler.fetchAllDevelopersRecord();
        for (Developer dev : devs)
            System.out.println(dev);
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();

        teamHeadDbConnectionHandler.getAllTeam_HeadWorkingProjectDetails("B");

        launch();
    }
}