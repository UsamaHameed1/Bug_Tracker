package com.bug_tracker.Business_Logic;


import com.bug_tracker.Database_Connectivity.Project_DB_Connection_Handler;
import java.util.Random;
import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private int projectUniqueIdentifer;
    private String projectName;
    private String clientName;
    private LocalDate projectStartingDate;
    private LocalDate projectEndingDate;
    private String projectDescription;
    private Team_Head projectTeamHead;
    private String projectCurrentStatus;
    private String repositoryLink;

    private ArrayList<Developer> developersTeam;
    private ArrayList<Tester> testersTeam;
    private ArrayList<Bug> List_Of_Bugs_Reported;

    ///------------------------------- Mongo DB IDS --------------------------------------------------------------------
//    private ArrayList<ObjectId> developersIDS;
//    private ArrayList<ObjectId> testersIDS;

    //---------------------------------------------------- CONSTRUCTOR -------------------------------------------------
    public Project(String projectName, String clientName, LocalDate projectStartingDate, LocalDate projectEndingDate, String projectDescription,String repositoryLink) {
        this.projectName = projectName;
        this.clientName = clientName;
        this.projectStartingDate = projectStartingDate;
        this.projectEndingDate = projectEndingDate;
        this.projectDescription = projectDescription;
        this.repositoryLink =repositoryLink;

        Random rand = new Random(); //instance of random class
        this.projectUniqueIdentifer =rand.nextInt(99999);
        this.projectCurrentStatus ="First Time Entered Details";

        List_Of_Bugs_Reported=new ArrayList<>();
    }

    //--------------------------------------------------- Getters & Setters --------------------------------------------



    public void setList_Of_Bugs_Reported(ArrayList<Bug> list_Of_Bugs_Reported) {
        List_Of_Bugs_Reported = list_Of_Bugs_Reported;
    }

    public ArrayList<Bug> getList_Of_Bugs_Reported() {
        return List_Of_Bugs_Reported;
    }

    public String getRepositoryLink() {
        return repositoryLink;
    }

    public void setRepositoryLink(String repositoryLink) {
        this.repositoryLink = repositoryLink;
    }

    public int getProjectUniqueIdentifer() {
        return projectUniqueIdentifer;
    }

    public void setProjectUniqueIdentifer(int projectUniqueIdentifer) {
        this.projectUniqueIdentifer = projectUniqueIdentifer;
    }

    public String getProjectCurrentStatus() {
        return projectCurrentStatus;
    }

    public void setProjectCurrentStatus(String projectCurrentStatus) {
        this.projectCurrentStatus = projectCurrentStatus;
    }

    public Team_Head getProjectTeamHead() {
        return projectTeamHead;
    }

    public void setProjectTeamHead(Team_Head projectTeamHead) {
        this.projectTeamHead = projectTeamHead;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getProjectStartingDate() {
        return projectStartingDate;
    }

    public void setProjectStartingDate(LocalDate projectStartingDate) {
        this.projectStartingDate = projectStartingDate;
    }

    public LocalDate getProjectEndingDate() {
        return projectEndingDate;
    }

    public void setProjectEndingDate(LocalDate projectEndingDate) {
        this.projectEndingDate = projectEndingDate;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public ArrayList<Developer> getDevelopersTeam() {
        return developersTeam;
    }

    public void setDevelopersTeam(ArrayList<Developer> developersTeam) {
        this.developersTeam = developersTeam;
    }

    public ArrayList<Tester> getTestersTeam() {
        return testersTeam;
    }

    public void setTestersTeam(ArrayList<Tester> testersTeam) {
        this.testersTeam = testersTeam;
    }


    public static Project getProjectDetails(int projectUniqueIdentifer)
    {
        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
        return dbHandlerProject.findProjectDetails(projectUniqueIdentifer);
    }


    @Override
    public String toString() {
        return "Project{" +
                "projectUniqueIdentifer=" + projectUniqueIdentifer +
                ", projectName='" + projectName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", projectStartingDate=" + projectStartingDate +
                ", projectEndingDate=" + projectEndingDate +
                ", projectDescription='" + projectDescription + '\'' +
                ", projectTeamHead=" + projectTeamHead +
                ", projectCurrentStatus='" + projectCurrentStatus + '\'' +
                ", repositoryLink='" + repositoryLink + '\'' +
                ", developersTeam=" + developersTeam +
                ", testersTeam=" + testersTeam +
                '}';
    }

    //==================================================================================================================
    //*********************** DATABASE ACTIONS RELATED TO Projects *****************************************************
    //==================================================================================================================
    public static void Add_New_Project_In_DB(Project projectObject)
    {
        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
        dbHandlerProject.addNewProject(projectObject);
    }

    public static Project Get_Closed_Project_Details(int projectUniqueID)
    {
        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
        return dbHandlerProject.getClosedProjectDetails(projectUniqueID);
    }
}
