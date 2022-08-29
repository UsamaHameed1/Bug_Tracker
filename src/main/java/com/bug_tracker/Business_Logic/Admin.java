package com.bug_tracker.Business_Logic;

import com.bug_tracker.Database_Connectivity.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/*
    Admin Class :
        Can Add Employees
        Can Add Other Admins
        Can Manage Details Related To Employees
        Can Manage Details Related To Admins
 */


public class Admin extends Employee{

    private String designation;
    //--------------------------- CONSTRUCTOR --------------------------------------------------------------------------
    public Admin(String name, String cnic, String contactNo, String email, String userName, String userPassword) {
        super(name, cnic, contactNo, email, userName, userPassword,"Admin");
        this.designation="Admin";
    }


    //--------------------------- To String Method ---------------------------------------------------------------------
    @Override
    public String toString()
    {
        return super.toString();
    }

    //---------------------------- Getter & Setters --------------------------------------------------------------------
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    //--------------------------- View All Projects With Details -------------------------------------------------------
    public ArrayList<Project> getAllProjectsDetails()
    {
        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
        return dbHandlerProject.getAllProjectsInDatabase();
    }


    private Developer addNewDeveloper(String empNameStr, String empCNICStr, String empContactStr, String empUserNameStr, String empUserPasswordStr, String empEmailStr)
    {
        Developer newDeveloper = new Developer(empNameStr, empCNICStr, empContactStr, empEmailStr, empUserNameStr, empUserPasswordStr);
        System.out.println(newDeveloper);
        newDeveloper.insertDeveloperInDB();
        return newDeveloper;
    }

    private Tester addNewTester(String empNameStr, String empCNICStr, String empContactStr, String empUserNameStr, String empUserPasswordStr, String empEmailStr)
    {
        Tester newTester = new Tester(empNameStr, empCNICStr, empContactStr, empEmailStr, empUserNameStr, empUserPasswordStr);
        System.out.println(newTester);
        newTester.insertNewTesterInDB();
        return newTester;
    }

    private Team_Head addNewTeamHead(String empNameStr, String empCNICStr, String empContactStr, String empUserNameStr, String empUserPasswordStr, String empEmailStr)
    {
        Team_Head newTeamHead = new Team_Head(empNameStr, empCNICStr, empContactStr, empEmailStr, empUserNameStr, empUserPasswordStr);
        System.out.println(newTeamHead);
        newTeamHead.insertTeamHeadInDB();
        return newTeamHead;
    }

    public Employee registerNewEmployee(String empNameStr, String empCNICStr, String empContactStr, String empUserNameStr, String empUserPasswordStr, String empEmailStr, int registerOptionSelected)
    {
        if (registerOptionSelected == 1)      //AS Team Head
        {
            return this.addNewTeamHead(empNameStr, empCNICStr, empContactStr, empUserNameStr, empUserPasswordStr,empEmailStr);

        } else if (registerOptionSelected == 2)        //AS Developer
        {
            return this.addNewDeveloper(empNameStr, empCNICStr, empContactStr, empUserNameStr, empUserPasswordStr,empEmailStr);

        } else if (registerOptionSelected == 3)        //Tester
        {
            return this.addNewTester(empNameStr, empCNICStr, empContactStr,  empUserNameStr, empUserPasswordStr,empEmailStr);
        }
        return null;
    }


    //==================================================================================================================
    //*********************** DATABASE ACTIONS RELATED TO Admin ********************************************************
    //==================================================================================================================

    //------------------------------- Insert New Admin in DB -----------------------------------------------------------
    public void insertAdminInDB()
    {
        Admin_DB_Connection_Handler dbConnectionHandler=new Admin_DB_Connection_Handler();
        dbConnectionHandler.addNewAdminInDB(this);
    }

    //------------------------------- Get All Admins Records From DB ---------------------------------------------------
    public static ArrayList<Admin> Get_All_Admins_Records() {
        Admin_DB_Connection_Handler dbConnectionHandler=new Admin_DB_Connection_Handler();
        return dbConnectionHandler.fetchAllAdminsRecords();
    }

    //------------------------------- Delete an Admin From the DB ------------------------------------------------------
    public static boolean Delete_Admin(ObjectId mongoID)
    {
        Admin_DB_Connection_Handler dbConnectionHandler=new Admin_DB_Connection_Handler();
        return dbConnectionHandler.Delete_Admin(mongoID);
    }

    //------------------------------ Update and Admin in the DB ------------------------------------------------------
    public static void Update_Admin(Admin adminObject)
    {
        Admin_DB_Connection_Handler dbConnectionHandler=new Admin_DB_Connection_Handler();
        dbConnectionHandler.Update_Admin_Details(adminObject);
    }

    //------------------------------ Find Admin Credentials From DB ----------------------------------------------------
    public static Admin Check_Admin_Credentials(String userName, String password)
    {
        Admin_DB_Connection_Handler dbConnectionHandler=new Admin_DB_Connection_Handler();
        return  dbConnectionHandler.findAdminCredentials(userName,password);
    }

}
