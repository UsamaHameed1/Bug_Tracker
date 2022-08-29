package com.bug_tracker.Business_Logic;

import com.bug_tracker.Database_Connectivity.Project_DB_Connection_Handler;
import com.bug_tracker.Database_Connectivity.Tester_DB_Connection_Handler;
import org.bson.types.ObjectId;
import java.util.ArrayList;

public class Tester extends Employee {

    ArrayList<Project> currentWorkingProjects = new ArrayList<>();

    private String designation;

    public Tester(String name, String cnic, String contactNo, String email, String userName, String userPassword) {
        super(name, cnic, contactNo, email, userName, userPassword, "Tester");
        this.designation = "Tester";
    }


    @Override
    public String toString() {
        return super.toString();
    }

    //----------------------------- Getters and Setters ----------------------------------------------------------------

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

    public ArrayList<Project> getAllWorkingProjects() {
        Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
        this.currentWorkingProjects = testerDbConnectionHandler.get_All_Testers_Working_Projects(this.getUserName());
        return this.currentWorkingProjects;
    }

    public void reportABug(Bug reportedBug, int projectUniqueID) {
        Project qureyProject = null;
        for (Project projectDetails : currentWorkingProjects) {
            if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                qureyProject = projectDetails;
            }
        }

        if (qureyProject != null) {
            qureyProject.getList_Of_Bugs_Reported().add(reportedBug);

            //--- Insert A New Bug In the Project DB ----------------------------------------------------------------------
            Project_DB_Connection_Handler dbHandlerProject = new Project_DB_Connection_Handler();
            dbHandlerProject.insertNewBug(reportedBug, projectUniqueID);
        }
    }

    public void markBugAsClosed(Bug updatedBug, int projectUniqueID) {
        //First Remove the bug from the list and replace it with the new updated one
        for (Project projectDetails : currentWorkingProjects) {
            if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                for (int i = 0; i < projectDetails.getList_Of_Bugs_Reported().size(); i++) {
                    if (projectDetails.getList_Of_Bugs_Reported().get(i).getBugUniqueID().compareTo(updatedBug.getBugUniqueID()) == 0) {
                        projectDetails.getList_Of_Bugs_Reported().remove(i);
                        projectDetails.getList_Of_Bugs_Reported().add(i, updatedBug);
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

        //Update In the Database
        Project_DB_Connection_Handler dbHandlerProject = new Project_DB_Connection_Handler();
        dbHandlerProject.updateBugDetailsFromDB(updatedBugsList, projectUniqueID);
    }

    //--------------------------------- Update The Bugs Details --------------------------------------------------------
    public void updateReportedBug(Bug updatedBug, int projectUniqueID) {
        //First Remove the bug from the list and replace it with the new updated one
        for (Project projectDetails : currentWorkingProjects) {
            if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                for (int i = 0; i < projectDetails.getList_Of_Bugs_Reported().size(); i++) {
                    if (projectDetails.getList_Of_Bugs_Reported().get(i).getBugUniqueID().compareTo(updatedBug.getBugUniqueID()) == 0) {
                        projectDetails.getList_Of_Bugs_Reported().remove(i);
                        projectDetails.getList_Of_Bugs_Reported().add(i, updatedBug);
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

        //Update In the Database
        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
        dbHandlerProject.updateBugDetailsFromDB(updatedBugsList, projectUniqueID);
    }

    public void removeBug(String bugID, int projectUniqueID) {
        for (Project projectDetails : currentWorkingProjects) {
            if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                for (int i = 0; i < projectDetails.getList_Of_Bugs_Reported().size(); i++) {
                    if (projectDetails.getList_Of_Bugs_Reported().get(i).getBugUniqueID().compareTo(bugID) == 0) {
                        projectDetails.getList_Of_Bugs_Reported().remove(i);
                    }
                }
            }
        }

        ArrayList<Bug> updatedBugsList = null;
        for (Project projectDetails : currentWorkingProjects) {
            if (projectDetails.getProjectUniqueIdentifer() == projectUniqueID) {
                updatedBugsList = projectDetails.getList_Of_Bugs_Reported();
            }
        }

        //Update In the Database
        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
        dbHandlerProject.updateBugDetailsFromDB(updatedBugsList, projectUniqueID);
    }

    //==================================================================================================================
    //*********************** DATABASE ACTIONS RELATED TO DEVELOPER ****************************************************
    //==================================================================================================================


    //----------------------------------------- Insert New Tester in DB  -----------------------------------------------
    public boolean insertNewTesterInDB() {
        Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
        testerDbConnectionHandler.addNewTesterInDB(this);
        return true;
    }

    //----------------------------------------- Find the Tester Record in DB -------------------------------------------
    public static Tester Check_Tester_Record(String userName, String userPassword) {
        Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
        return testerDbConnectionHandler.findTesterDetails(userName, userPassword);
    }

    public static ArrayList<Tester> Get_All_Testers_Records_DB() {
        Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
        return testerDbConnectionHandler.fetchAll_Testers_Record();
    }

    //---------------------------------------- Delete A Tester Record --------------------------------------------------
    public static boolean Delete_Tester_Record(ObjectId mongoID) {
        Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
        testerDbConnectionHandler.Delete_Tester(mongoID);
        return true;
    }

    //-------------------------------- Insert New Project Details ------------------------------------------------------
    public static void Insert_New_Project_Details(Project projectObject, Tester testerObject) {
        Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
        testerDbConnectionHandler.insertNewProjectInTester(projectObject, testerObject);
    }
}
