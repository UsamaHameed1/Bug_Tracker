package com.bug_tracker.Database_Connectivity;

import com.bug_tracker.Business_Logic.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TeamHead_DB_Connection_Handler {


    // Insert New Developer in to the mongo DB
    public  void addNewTeamHead(Team_Head object) {

//        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
//        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        Object_To_Document_Conversions objectCon =new Object_To_Document_Conversions();
        Document newDeveloperDoc = objectCon.convertTeamHead(object);

        //4. Insert the new Object
        collection.insertOne(newDeveloperDoc);
        System.out.println("New Team Head Inserted");

        mongoClient.close();
    }

    // Fetch All Team Heads Record
    public  ArrayList<Team_Head> fetchAll_TeamHeads_Record() {

        ArrayList<Team_Head> team_heads = new ArrayList<>();
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");

        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        try (MongoCursor<Document> cur = collection.find().iterator()) {
            while (cur.hasNext()) {
                var doc = cur.next();

                //----------- Mapping of Document to Java Object --------------------------
                ObjectId mongoId = doc.getObjectId("_id");
                String name = doc.getString("name");
                String cnic = doc.getString("cnic");
                String contactNo = doc.getString("contactNo");
                String email = doc.getString("email");
                String userName = doc.getString("userName");
                String userPassword = doc.getString("userPassword");

                Team_Head team_head = new Team_Head(name, cnic, contactNo, email, userName, userPassword);
                team_head.setMongoId(mongoId);
                team_heads.add(team_head);
            }
        }
        mongoClient.close();
        return team_heads;
    }

    // Delete Team Head Record
    public  boolean Delete_TeamHead(ObjectId mongoID) {
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        // Delete the document
        collection.deleteOne(Filters.eq("_id", mongoID));
        System.out.println("Team Head Delete Successful");

        return true;
    }

    //------------------------------------ Find Team Head Credentials --------------------------------------------------
    //Find Team Head Credentials
    public  Team_Head findTeamHeadDetails(String userName, String password) {
        Team_Head queryTeamHead = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("userName", userName);
        whereQuery.put("userPassword", password);
        MongoCursor<Document> cursor = collection.find(whereQuery).iterator();


        while (cursor.hasNext()) {
            System.out.println("team head founded !!!!");

            var doc = cursor.next();

            //----------- Mapping of Document to Java Object --------------------------
            ObjectId mongoId = doc.getObjectId("_id");
            String name = doc.getString("name");
            String cnic = doc.getString("cnic");
            String contactNo = doc.getString("contactNo");
            String email = doc.getString("email");
            String userName1 = doc.getString("userName");
            String userPassword = doc.getString("userPassword");
            String teamHeadID = doc.getString("Teamhead_ID");

            queryTeamHead = new Team_Head(name, cnic, contactNo, email, userName1, userPassword);
            queryTeamHead.setId(teamHeadID);
            queryTeamHead.setMongoId(mongoId);
        }
        return queryTeamHead;
    }

    //------------------------------------ Find Team Head Credentials --------------------------------------------------
    //Find Team Head Credentials
    public  Team_Head findTeamHead(String teamHeadID) {
        Team_Head queryTeamHead = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("userName", teamHeadID);
        MongoCursor<Document> cursor = collection.find(whereQuery).iterator();

        while (cursor.hasNext()) {
            System.out.println("team head founded !!!!");

            var doc = cursor.next();

            //----------- Mapping of Document to Java Object --------------------------
            ObjectId mongoId = doc.getObjectId("_id");
            String name = doc.getString("name");
            String cnic = doc.getString("cnic");
            String contactNo = doc.getString("contactNo");
            String email = doc.getString("email");
            String userName1 = doc.getString("userName");
            String userPassword = doc.getString("userPassword");

            queryTeamHead = new Team_Head(name, cnic, contactNo, email, userName1, userPassword);
            queryTeamHead.setMongoId(mongoId);
        }
        return queryTeamHead;
    }

    //-------------------------------------- Add new project in the team heads document --------------------------------
    public  void insertNewProjectInTeamHead(Project projectObject, Team_Head teamHead_Object) {

        Object_To_Document_Conversions obj=new Object_To_Document_Conversions();
        Document newProjectDocument = obj.convertProject(projectObject);


        Team_Head queryTeamHead = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        //4. Push New Project in to the team head record
        collection.updateOne(Filters.eq("_id", teamHead_Object.getMongoId()), Updates.push("currentWorkingProjects", newProjectDocument));
    }


    //------------------------------------- Get All Working Project Details of Team Head -------------------------------
    public  ArrayList<Project> getAllTeam_HeadWorkingProjectDetails(String object) {

        ArrayList<Project> currentWorkingProjects = null;


        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        try (MongoCursor<Document> cur = collection.find().iterator()) {
            while (cur.hasNext()) {
                var doc = cur.next();
                if (doc.getString("userName").compareTo(object) == 0) {
                    //---------- LIST OF ALL WORKING PROJECTS BY TEAM HEAD -------------------------------------------------
                    currentWorkingProjects = new ArrayList<>();

                    for (Document currentProject : (ArrayList<Document>) doc.get("currentWorkingProjects")) {
                        System.out.println(currentProject);

                        String projectName = currentProject.getString("projectName");
                        String clientName = currentProject.getString("clientName");
                        String description = currentProject.getString("description");

                        Date startingDate = currentProject.getDate("startingDate");
                        Date endingDate = currentProject.getDate("endingDate");

                        int projectID = currentProject.getInteger("projectUniqueID");
                        String repositoryLink = currentProject.getString("repositoryLink");
                        String currentProjectStatus = currentProject.getString("projectCurrentStatus");

                        Project projectDetails = new Project(projectName, clientName, Helper_Functions.convertToLocalDateViaInstant(startingDate), Helper_Functions.convertToLocalDateViaInstant(endingDate), description, repositoryLink);
                        projectDetails.setProjectUniqueIdentifer(projectID);
                        projectDetails.setProjectCurrentStatus(currentProjectStatus);

                        //------------- All Testers Details ----------------------------------------------------------------
                        ArrayList<Tester> testersTeam = new ArrayList<>();
                        for (Document currentTester : (ArrayList<Document>) currentProject.get("testersTeam")) {
                            String testerName = currentTester.getString("name");
                            String testerCNIC = currentTester.getString("cnic");
                            String testerEmail = currentTester.getString("email");
                            String testerContact = currentTester.getString("contact");
                            String testerUserName = currentProject.getString("userName");
                            String testerPassword = currentProject.getString("userPassword");

                            //------- Get All Working Projects of the Tester -----------------------------------------------
                            Tester currentTesterObject = new Tester(testerName, testerCNIC, testerContact, testerEmail, testerUserName, testerPassword);
                            currentTesterObject.setCurrentWorkingProjects(null);

                            //append in the list
                            testersTeam.add(currentTesterObject);
                        }

                        // Set The Testers Team of the project
                        projectDetails.setTestersTeam(testersTeam);

                        //------------- All Developers Details -------------------------------------------------------------
                        ArrayList<Developer> developersTeam = new ArrayList<>();
                        for (Document currentTester : (ArrayList<Document>) currentProject.get("developersTeam")) {
                            String developerName = currentTester.getString("name");
                            String developerCNIC = currentTester.getString("cnic");
                            String developerEmail = currentTester.getString("email");
                            String developerContact = currentTester.getString("contact");
                            String developerUserName = currentProject.getString("userName");


                            //------- Get All Working Projects of the Tester -----------------------------------------------
                            Developer currentDeveloper = new Developer(developerName, developerCNIC, developerEmail, developerContact, developerUserName, null);
                            currentDeveloper.setCurrentWorkingProjects(null);

                            //append in the list
                            developersTeam.add(currentDeveloper);
                        }

                        // Set The Developers Team of the project
                        projectDetails.setDevelopersTeam(developersTeam);

                        currentWorkingProjects.add(projectDetails);
                    }
                }
            }
        }
        mongoClient.close();
        return currentWorkingProjects;
    }


    //------------------------------------ Remove Particular Instance of the Project From Database ---------------------
    public  boolean removeParticularProjectDetails(Project projectDetails) {
        Project_DB_Connection_Handler dbHandlerProject = new Project_DB_Connection_Handler();
        return dbHandlerProject.removeProjectDetails(projectDetails);
    }

    //------------------------------- Update Project Details in Team Head ----------------------------------------------
    public  void updateProjectDetails_TeamHeadsCollection(Project project) {
        //----------------------------------- Get All Team Heads Working Projects --------------------------------------
        ArrayList<Project> allWorkingProjects = getAllTeam_HeadWorkingProjectDetails(project.getProjectTeamHead().getUserName());

        for (int i = 0; i < allWorkingProjects.size(); i++) {
            if (allWorkingProjects.get(i).getProjectUniqueIdentifer() == project.getProjectUniqueIdentifer()) {
                allWorkingProjects.remove(i);
                break;
            }
        }
        allWorkingProjects.add(project);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        System.out.println(project.getProjectTeamHead().getId());

        // First Remove The Current Working Projects
        for (int i = 0; i < allWorkingProjects.size(); i++) {
            collection.updateOne(Filters.eq("Teamhead_ID", project.getProjectTeamHead().getId()), Updates.popFirst("currentWorkingProjects"));
        }

        // Update a document
        for (Project projectDetailsObj : allWorkingProjects) {
            Object_To_Document_Conversions object =new Object_To_Document_Conversions();
            Document projectDocumentDoc = object.convertProject(projectDetailsObj);
            collection.updateOne(Filters.eq("Teamhead_ID", project.getProjectTeamHead().getId()), Updates.push("currentWorkingProjects", projectDocumentDoc));
        }

        mongoClient.close();
    }
}
