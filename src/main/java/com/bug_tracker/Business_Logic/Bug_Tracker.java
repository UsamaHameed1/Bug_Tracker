package com.bug_tracker.Business_Logic;

import com.bug_tracker.GUI.LoginScreen_Controller;
import com.bug_tracker.GUI.Tester.Tester_MainScreen_Controller;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;

public class Bug_Tracker {

    final private String masterPassword = "123";
    // ======================================== Singleton Pattern Implementation =======================================
    private static Bug_Tracker instance = new Bug_Tracker();

    // private constructor
    private Bug_Tracker() {
    }

    //get the only object available
    public static Bug_Tracker getInstance() {
        return instance;
    }
    // ==============================================================================================================================================================================================================

    //--------------------------------------------------------------------------- REGISTRATION OF EMPLOYEEE --------------------------------------------------------------------------------------------------------
    // Register Employee Function
    public Employee registerEmployee(String empNameStr, String empCNICStr, String empContactStr, String empUserNameStr, String empUserPasswordStr, String empEmailStr, int registerOptionSelected, Admin loggedInAdmin) {
        return loggedInAdmin.registerNewEmployee(empNameStr,empCNICStr,empContactStr,empUserNameStr,empUserPasswordStr,empEmailStr,registerOptionSelected);
    }

    public Admin registerAdmin(String empNameStr, String empCNICStr, String empContactStr, String empUserNameStr, String empUserPasswordStr, String empEmailStr, String masterPassword) {
        if (masterPassword.compareTo(this.masterPassword) == 0) {
            Admin newAdmin = new Admin(empNameStr, empCNICStr, empContactStr, empEmailStr, empUserNameStr, empUserPasswordStr);
            System.out.println("New Admin Added Successfully");
            newAdmin.insertAdminInDB();
            return newAdmin;
        } else {
            return null;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------- FETCH ALL EMPLOYEE TYPE RECORDS FROM DATABASE --------------------------------------------------------------------------------------------------------
    // Get all Team Head Records From Database
    public ArrayList<Team_Head> getAllTeamHeadsRecords() {
        return Team_Head.Get_All_TeamHeads_Records();
    }

    // Get All Testers Record From Database
    public ArrayList<Tester> getAllTestersRecords() {
        return Tester.Get_All_Testers_Records_DB();
    }

    // Get All Developers Record From Database
    public ArrayList<Developer> getAllDevelopersRecords() {
        return Developer.Get_All_Developers_Records();
    }

    // get all admins records
    public ArrayList<Admin> getAllAdminRecords() {
        return Admin.Get_All_Admins_Records();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    //---------------------------------------------------- DELETE EMPLOYEE RECORDS -------------------------------------------------------------------------------------------------------------------------------

    //delete a teamhead record
    public void deleteTeamHead(ObjectId mongoID) {
        Team_Head.Delete_TeamHead_Record(mongoID);
    }

    //delete a tester record
    public void deleteTester(ObjectId mongoID) {
        Tester.Delete_Tester_Record(mongoID);
    }

    //delete a developer record
    public void deleteDeveloper(ObjectId mongoID) {
        Developer.Delete_Developer_Record(mongoID);
    }

    //delete a admin record
    public boolean Delete_Admin(Admin selectedAdmin) {
        return Admin.Delete_Admin(selectedAdmin.getMongoId());
    }


    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void updateDeveloer(Developer updatedDeveloper)
    {
    }


    public void updateTester(Tester updateTester)
    {

    }

    public void updateTeamHead(Team_Head updateTeamhead)
    {

    }


    public void updateAdmin(Admin updatedAdmin)
    {
            Admin.Update_Admin(updatedAdmin);
    }

    //------------------------------------------------------------------ GET PARTICULAR EMPLOYEE RECORD ONLY ----------------------------------------------------------------------------------------------------
    public Employee getParticularDeveloperRecord(Employee selectedDeveloper) {
        return (Employee) Developer.Check_Developer_Record(selectedDeveloper.getUserName(), selectedDeveloper.getUserPassword());
    }

    public Employee getParticularTesterRecord(Employee selectedDeveloper) {
        return (Employee) Tester.Check_Tester_Record(selectedDeveloper.getUserName(), selectedDeveloper.getUserPassword());
    }

    public Employee getParticularTeamHeadRecord(Employee selectedTeamHead) {
        return (Employee) Team_Head.Check_TeamHead_Credentials(selectedTeamHead.getUserName(), selectedTeamHead.getUserPassword());
    }

    public Employee getParticularAdmin(Employee selectedAdmin) {
        return (Employee) Admin.Check_Admin_Credentials(selectedAdmin.getUserName(), selectedAdmin.getUserPassword());
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    //---------------------------------------------------------------- GET ALL PROJECTS OF A EMPLOYEE ON WHICH HE IS WORKING ------------------------------------------------------------------------------------
    // GET ALL THE PROJECTS OF A PARTICULAR TEAMHEAD WORKING ON ONLY
    public ArrayList<Project> getAllProjectsOfTeamHead(Team_Head loggedInTeamHead) {
        return loggedInTeamHead.getAllWorkingProjectList();
    }

    // GET ALL THE PROJECTS OF A PARTICULAR DEVELOPER WORKING ON ONLY
    public ArrayList<Project> getAllProjectsofDeveloper(Developer loggedInDeveloper)
    {
        return loggedInDeveloper.getAllWorkingProjects();
    }

    // GET ALL THE PROJECTS OF A PARTICULAR TESTER WORKING ON ONLY
    public  ArrayList<Project> getAllProjectsOfTester(Tester loggedInTester)
    {
        return loggedInTester.getAllWorkingProjects();
    }

    //---------------------------------------------------------------- RELATED TO THE PROJECT OPERATIONS ---------------------------------------------------------------------------------------------------------

    // get all projects from the database
    public ArrayList<Project> getAllProjectRecords(Admin object) {
        return object.getAllProjectsDetails();
    }

    // add a new project by teamhead
    public void addNewProject(Project newProject, Team_Head loggedInTeamHead) {
        loggedInTeamHead.addNewProject(newProject);
    }

    // remove a project details
    public void removeProjectDetails(Project selectedProject, Team_Head loggedInTeamHead) {
        loggedInTeamHead.removeProjectDetails(selectedProject);
    }

    //update project details
    public void updateProjectDetails(Project selectedProject, Team_Head loggedInTeamHead) {
        loggedInTeamHead.updateProjectDetails(selectedProject);
    }

    //manage project status
    public void manageProjectStatus(Project selectedProject, Team_Head loggedInTeamHead) {
        loggedInTeamHead.changeProjectCurrentStatus(selectedProject);
    }


    // GET PARTICULAR PROJECT DETAILS
    public Project getParticularProject(int projectUniqueID) {
        return Project.getProjectDetails(projectUniqueID);
    }


    //---------------------------------------------------------------------- ACTIONS RELATED TO THE BUG OF PROJECT -------------------------------------------------------------------------------------------------------
    // GET LIST OF ALL THE BUGS REPORTED IN THE PROJECT
    public ArrayList<Bug> getListOfAllBugsOfAParticularProject(Project selectedProject)
    {
         return selectedProject.getList_Of_Bugs_Reported();
    }

    // REPORT A NEW BUG
    public void reportANewBug(Tester loggedInTester,int uniqueProjectID,String testerUserName, String bugTitle, String bugDescription, LocalDate bugAssignmentDate, String bugStatus, String bugRelvantCodeLine, String bugRelatedCommitHash,String messageNote, String bugCategory, Developer developerDetails)
    {
        Bug newReportedBug =new Bug(Tester_MainScreen_Controller.currentTesterObject.getUserName(), bugTitle,
                bugDescription,bugAssignmentDate,bugStatus,bugRelvantCodeLine, bugRelatedCommitHash,
                messageNote,bugCategory,developerDetails);

        loggedInTester.reportABug(newReportedBug,uniqueProjectID);
    }

    // UPDATE DETAILS OF REPORTED BUG
    public void updateReportedBugDetail(Tester loggedInTester, Bug updatedBug, int projectUniqueIdentifier)
    {
        loggedInTester.updateReportedBug(updatedBug, projectUniqueIdentifier);
    }


    // REMOVE THE REPORT BUG DETAIL
    public void removeReportedBugDetails(Tester loggedInTester, String bugID, int projectUniqueID)
    {
        loggedInTester.removeBug(bugID, projectUniqueID);
    }

    //CLOSE THE BUG
    public void closeBug(Tester loggedInTester,Bug selectedBug, int projectUniqueId )
    {
        loggedInTester.markBugAsClosed(selectedBug,projectUniqueId);
    }


    public void manageStatusOfBug(Developer loggedInDeveloper,Bug updatedBug, int projectUniqueIdentifier)
    {
        loggedInDeveloper.updateBugStatus(updatedBug,projectUniqueIdentifier);
    }
}
