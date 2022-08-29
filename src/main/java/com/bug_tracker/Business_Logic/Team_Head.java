package com.bug_tracker.Business_Logic;


import com.bug_tracker.Database_Connectivity.Developer_DB_Connection_Handler;
import com.bug_tracker.Database_Connectivity.Project_DB_Connection_Handler;
import com.bug_tracker.Database_Connectivity.TeamHead_DB_Connection_Handler;
import com.bug_tracker.Database_Connectivity.Tester_DB_Connection_Handler;
import com.bug_tracker.GUI.LoginScreen_Controller;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Random;

public class Team_Head extends Employee {

    private ArrayList<Project> currentWorkingProjects = new ArrayList<>();
    private String designation;
    private String id;

    //------------------------------ CONSTRUCTOR -----------------------------------------------------------------------
    public Team_Head(String name, String cnic, String contactNo, String email, String userName, String userPassword) {
        super(name, cnic, contactNo, email, userName, userPassword, "Team Head");
        this.designation = "Team Head";

        Random rand = new Random(); //instance of random class
        this.id = "TH-" + (int) rand.nextInt(9999999);
    }

    //------------------------------ Convert Details -------------------------------------------------------------------
    public void insertNewWorkingProject(Project newProject) {
        currentWorkingProjects.add(newProject);
    }

    //----------------------------- Getters and Setters ----------------------------------------------------------------
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean changeProjectCurrentStatus(Project updatedProjectStatus) {
        if (updatedProjectStatus.getProjectCurrentStatus().compareTo("close-project") == 0) {
            //first update
            Project_DB_Connection_Handler dbHandlerProject = new Project_DB_Connection_Handler();

            dbHandlerProject.updateProjectDetails(updatedProjectStatus);
            updateProjectDetails(updatedProjectStatus);

            //the delete and move to the closed projects table
            dbHandlerProject.removeProjectDetails(updatedProjectStatus);
            return true;
        } else {
            // only update the current status
            Project_DB_Connection_Handler dbHandlerProject = new Project_DB_Connection_Handler();
            dbHandlerProject.updateProjectDetails(updatedProjectStatus);
            updateProjectDetails(updatedProjectStatus);
            return true;
        }
    }


    //--------------------------------------- Team Heads Functionality -------------------------------------------------
    public ArrayList<Project> getAllWorkingProjectList() {
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
        this.currentWorkingProjects = teamHeadDbConnectionHandler.getAllTeam_HeadWorkingProjectDetails(this.getUserName());
        return this.currentWorkingProjects;
    }

    public boolean removeProjectDetails(Project projectObject) {
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
        return teamHeadDbConnectionHandler.removeParticularProjectDetails(projectObject);
    }

    public void updateProjectDetails(Project projectDetails) {
        //First get userName of Testers and Developers Who are Working on this prokect

        Project_DB_Connection_Handler dbHandlerProject = new Project_DB_Connection_Handler();

        // Developers Usernames List
        ArrayList<String> allDevelopersUserNameWorkingOnProject = dbHandlerProject.getDevelopersTeamList(projectDetails);

        // Testers Usernames Working on this project
        ArrayList<String> allTestersUserNameWorkingOnProject = dbHandlerProject.getTestersTeamList(projectDetails);

        // update in projects collection
        dbHandlerProject.updateProjectDetails(projectDetails);

        //update in team head collection
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
        teamHeadDbConnectionHandler.updateProjectDetails_TeamHeadsCollection(projectDetails);

        //update in the tester collection
        Tester_DB_Connection_Handler dbHandlerTester =new Tester_DB_Connection_Handler();
        dbHandlerTester.updateProjectDetails_TesterCollection(projectDetails, allTestersUserNameWorkingOnProject);

        //update in the developer collection
        Developer_DB_Connection_Handler dbHandlerDev = new Developer_DB_Connection_Handler();
        dbHandlerDev.updateProjectDetails_DeveloperCollection(projectDetails, allDevelopersUserNameWorkingOnProject);
    }


    public void addNewProject(Project newProject)
    {
        Project.Add_New_Project_In_DB(newProject);

        //-------------------------- Add the project to the Team Head Object Also --------------------------------------
        Team_Head objectTeamHead = (Team_Head) LoginScreen_Controller.currentLoginEmployee;
        objectTeamHead.insertNewWorkingProject(newProject);
        Team_Head.Insert_New_Project_Details(newProject, objectTeamHead);

        //-------------------------- Add the project to the Developer Head Object Also ---------------------------------
        for (Developer iteratorDeveloper : newProject.getDevelopersTeam()) {
            Developer.Insert_New_Project_Details(newProject, iteratorDeveloper);
        }

        //-------------------------- Add the project to the Tester Object Also -----------------------------------------
        for (Tester iteratorTester : newProject.getTestersTeam()) {
            Tester.Insert_New_Project_Details(newProject, iteratorTester);
        }
    }

    //==================================================================================================================
    //*********************** DATABASE ACTIONS RELATED TO DEVELOPER ****************************************************
    //==================================================================================================================

    //----------------------------------- Insert New Team Head in DB ---------------------------------------------------
    public void insertTeamHeadInDB() {
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
        teamHeadDbConnectionHandler.addNewTeamHead(this);
    }

    //----------------------------------- Get All Team Heads Records From DB -------------------------------------------
    public static ArrayList<Team_Head> Get_All_TeamHeads_Records() {
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
        return teamHeadDbConnectionHandler.fetchAll_TeamHeads_Record();
    }

    //---------------------------------- Delete Team Head Records From DB ----------------------------------------------
    public static boolean Delete_TeamHead_Record(ObjectId mongoID) {
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
        teamHeadDbConnectionHandler.Delete_TeamHead(mongoID);
        return true;
    }

    //-------------------------------- Find Team Head Credentials ------------------------------------------------------
    public static Team_Head Check_TeamHead_Credentials(String userName, String password) {
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
        return teamHeadDbConnectionHandler.findTeamHeadDetails(userName, password);
    }

    //-------------------------------- Insert New Project Details ------------------------------------------------------
    public static void Insert_New_Project_Details(Project projectObject, Team_Head teamHeadObject) {
        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
        teamHeadDbConnectionHandler.insertNewProjectInTeamHead(projectObject, teamHeadObject);
    }


}
