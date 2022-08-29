package com.bug_tracker.Business_Logic;


import com.bug_tracker.Database_Connectivity.Developer_DB_Connection_Handler;
import com.bug_tracker.Database_Connectivity.Project_DB_Connection_Handler;
import org.bson.types.ObjectId;
import java.util.ArrayList;

public class Developer extends Employee {
    ArrayList<Project> currentWorkingProjects = new ArrayList<>();
    private String designation;

    //------------------------------ CONSTRUCTOR -----------------------------------------------------------------------
    public Developer(String name, String cnic, String contactNo, String email, String userName, String userPassword) {
        super(name, cnic, contactNo, email, userName, userPassword, "Developer");
        this.designation = "Developer";


    }

    //----------------------------- Getters and Setter Methods ---------------------------------------------------------
    @Override
    public String toString() {
        return super.toString();
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public ArrayList<Project> getCurrentWorkingProjects() {
        return currentWorkingProjects;
    }

    public void setCurrentWorkingProjects(ArrayList<Project> currentWorkingProjects) {
        this.currentWorkingProjects = currentWorkingProjects;
    }


    //------------------------------ Get ALl Working Projects of the developer -----------------------------------------
    public ArrayList<Project> getAllWorkingProjects() {
        Developer_DB_Connection_Handler dbHandler = new Developer_DB_Connection_Handler();
        this.currentWorkingProjects = dbHandler.get_All_Developers_Working_Projects(getUserName());
        return currentWorkingProjects;
    }

    //------------------------------- Update Bug Status ----------------------------------------------------------------
    public void updateBugStatus(Bug updatedBug, int projectUniqueID) {
        if (updatedBug != null && updatedBug.getBugStatus().compareTo("bug-closed & waiting for tester reply") == 0) {

            //First Remove the bug from the list and replace it with the new updated one
            for (Project projectDetails : currentWorkingProjects) {
                if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                    for (int i = 0; i < projectDetails.getList_Of_Bugs_Reported().size(); i++) {
                        if (projectDetails.getList_Of_Bugs_Reported().get(i).getBugUniqueID().compareTo(updatedBug.getBugUniqueID()) == 0) {
                            projectDetails.getList_Of_Bugs_Reported().remove(i);
                            projectDetails.getList_Of_Bugs_Reported().add(updatedBug);
                        }
                    }
                }
            }

            //get the list of the updated bugs
            ArrayList<Bug> updatedBugsList = null;
            for (Project projectDetails : currentWorkingProjects) {
                if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                    updatedBugsList = projectDetails.getList_Of_Bugs_Reported();
                }
            }

            //make update in the database
            Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
            dbHandlerProject.updateBugDetailsFromDB(updatedBugsList, projectUniqueID);
        }
        else
        {
            //First Remove the bug from the list and replace it with the new updated one
            for (Project projectDetails : currentWorkingProjects) {
                if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                    for (int i = 0; i < projectDetails.getList_Of_Bugs_Reported().size(); i++) {
                        if (projectDetails.getList_Of_Bugs_Reported().get(i).getBugUniqueID().compareTo(updatedBug.getBugUniqueID()) == 0) {
                            projectDetails.getList_Of_Bugs_Reported().remove(i);
                            projectDetails.getList_Of_Bugs_Reported().add(updatedBug);
                        }
                    }
                }
            }

            //get the list of the updated bugs
            ArrayList<Bug> updatedBugsList = null;
            for (Project projectDetails : currentWorkingProjects) {
                if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                    updatedBugsList = projectDetails.getList_Of_Bugs_Reported();
                }
            }

            //make update in the database
            Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
            dbHandlerProject.updateBugDetailsFromDB(updatedBugsList, projectUniqueID);
        }
    }

    //==================================================================================================================
    //*********************** DATABASE ACTIONS RELATED TO DEVELOPER ****************************************************
    //==================================================================================================================

    //--------------------------------------- Insert new Developer in DB -----------------------------------------------
    public boolean insertDeveloperInDB() {
        Developer_DB_Connection_Handler dbHandler = new Developer_DB_Connection_Handler();
        dbHandler.addNewDeveloperInDB(this);
        return false;
    }

    //--------------------------------------- Get All Developers Records From DB ---------------------------------------
    public static ArrayList<Developer> Get_All_Developers_Records() {
        Developer_DB_Connection_Handler dbHandler = new Developer_DB_Connection_Handler();
        return dbHandler.fetchAllDevelopersRecord();
    }

    //--------------------------------------- Delete A Developer Record From DB ----------------------------------------
    public static boolean Delete_Developer_Record(ObjectId mongoID) {
        Developer_DB_Connection_Handler dbHandler = new Developer_DB_Connection_Handler();
        dbHandler.Delete_Developer(mongoID);
        return true;
    }

    //-------------------------------- Insert New Project Details ------------------------------------------------------
    public static void Insert_New_Project_Details(Project projectObject, Developer developerObject) {
        Developer_DB_Connection_Handler dbHandler = new Developer_DB_Connection_Handler();
        dbHandler.insertNewProjectInDeveloper(projectObject, developerObject);
    }

    public static Developer Check_Developer_Record(String developerUserName, String developerPassword) {
        Developer_DB_Connection_Handler dbHandler = new Developer_DB_Connection_Handler();
        return dbHandler.verifyDeveloperCredentials(developerUserName, developerPassword);
    }


}
