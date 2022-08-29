package com.bug_tracker.Database_Connectivity;

import com.bug_tracker.Business_Logic.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class Developer_DB_Connection_Handler {

    // -------------------------------- Insert New Developer in to the mongo DB ----------------------------------------
    public void addNewDeveloperInDB(Developer object) {

//        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
//        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Developers");


        Object_To_Document_Conversions  object1 = new Object_To_Document_Conversions();
        Document newDeveloperDoc = object1.convertDeveloper(object);

        //4. Insert the new Object
        collection.insertOne(newDeveloperDoc);
        System.out.println("New Developer Inserted");

        mongoClient.close();
    }

    // ------------------------------- Fetch All Developers Record -----------------------------------------------------
    public ArrayList<Developer> fetchAllDevelopersRecord() {

        ArrayList<Developer> developers = new ArrayList<>();
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");

        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Developers");

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

                Developer developer = new Developer(name, cnic, contactNo, email, userName, userPassword);
                developer.setMongoId(mongoId);
                developers.add(developer);
            }
        }
        mongoClient.close();
        return developers;
    }

    // --------------------------------------- Delete Developer Record -------------------------------------------------
    public boolean Delete_Developer(ObjectId mongoID) {
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Developers");

        // Delete the document
        collection.deleteOne(Filters.eq("_id", mongoID));
        System.out.println("Developer Delete Successful");

        return true;
    }

    //-------------------------------------- Add new project in the Developer document --------------------------------
    public void insertNewProjectInDeveloper(Project projectObject, Developer testerObject) {

        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

        Document newProjectDocument = obj1.convertProject(projectObject);
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Developers");

        //4. Push New Project in to the team head record
        collection.updateOne(Filters.eq("_id", testerObject.getMongoId()), Updates.push("currentWorkingProjects", newProjectDocument));
    }

    //-------------------------------------- Get all Developers Working Project -------------------------------------------
    public ArrayList<Project> get_All_Developers_Working_Projects(String testerUsername) {

        ArrayList<Project> currentWorkingProjects = null;

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Developers");

        try (MongoCursor<Document> cur = collection.find().iterator()) {
            while (cur.hasNext()) {
                var doc = cur.next();
                if (doc.getString("userName").compareTo(testerUsername) == 0) {
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

                        Document teamheadDocument = (Document) currentProject.get("teamHead");
                        String teamheadName = teamheadDocument.getString("name");
                        Object teamheadMongoID = teamheadDocument.getObjectId("_id");
                        String teamheadEmail = teamheadDocument.getString("Email");
                        String teamheadID = teamheadDocument.getString("Teamhead_ID");

                        Team_Head projectTeamHead = new Team_Head(teamheadName, null, null, teamheadEmail, null, null);
                        projectTeamHead.setId(teamheadID);

                        projectDetails.setProjectTeamHead(projectTeamHead);

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

                        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
                        projectDetails.setList_Of_Bugs_Reported(dbHandlerProject.getAllReportedBugs(projectDetails.getProjectUniqueIdentifer()));
                        currentWorkingProjects.add(projectDetails);
                    }
                }
            }
        }
        mongoClient.close();
        return currentWorkingProjects;
    }

    //------------------------------------- Update the Developer Project Details ------------------------------------------
    public void updateProjectDetails_DeveloperCollection(Project project, ArrayList<String> oldDevelopersUsernameWorkingOnThisProject) {

        boolean first_check_fail=true;
        //get all the developers' username working on the project
        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();
        ArrayList<String> updatedDevelopersUsernameWorkingOnThisProject = dbHandlerProject.getDevelopersTeamList(project);

        //---------------------------------------------- First Delete Which Are Not Part of the New Project Object ----------------------------------------------------------------------------
        // Traverse the projects developers username and match with the old developers username
        for (String oldUserName : oldDevelopersUsernameWorkingOnThisProject) {
            boolean flag = true;
            for (String updatedDeveloper : updatedDevelopersUsernameWorkingOnThisProject) {
                if (oldUserName.compareTo(updatedDeveloper) == 0) {
                    flag = false;
                }
            }

            //chenage maded noww !!!!! 16-5-2022
            if (flag == true) {

                first_check_fail=false;
                // get the delete developer all working projects
                ArrayList<Project> developerWorkingProjects = get_All_Developers_Working_Projects(oldUserName);

                // pop the current project on which this function is called : project will contain the developers list which will have not this current developer
                int size_of_array_working_projects = developerWorkingProjects.size();

                // Removing the project from the developer list

                for ( int i = 0; i < developerWorkingProjects.size(); i++) {
                    if (developerWorkingProjects.get(i).getProjectUniqueIdentifer() == project.getProjectUniqueIdentifer()) {
                        developerWorkingProjects.remove(i);
                        break;
                    }
                }

//                1. Establish Connection
                try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {


                    //2. Connect to the Database
                    MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


                    //3. Connect to the collection
                    MongoCollection<Document> collection = db.getCollection("Developers");

                    System.out.println(project.getProjectTeamHead().getId());

                    // First Remove The Current Working Projects
                    for (int i = 0; i < size_of_array_working_projects; i++) {
                        collection.updateOne(Filters.eq("userName", oldUserName), Updates.popFirst("currentWorkingProjects"));
                    }

                    // Update a document
                    for (Project projectDetailsObj : developerWorkingProjects) {
                        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                        Document projectDocumentDoc = obj1.convertProject(projectDetailsObj);
                        collection.updateOne(Filters.eq("userName", oldUserName), Updates.push("currentWorkingProjects", projectDocumentDoc));
                    }
                }
            }
            else if (flag == false) {

                first_check_fail = false;
                // get the delete developer all working projects
                ArrayList<Project> developerWorkingProjects = get_All_Developers_Working_Projects(oldUserName);

                // pop the current project on which this function is called : project will contain the developers list which will have not this current developer
                int size_of_array_working_projects = developerWorkingProjects.size();

                // Removing the project from the developer list

                for (int i = 0; i < developerWorkingProjects.size(); i++) {
                    if (developerWorkingProjects.get(i).getProjectUniqueIdentifer() == project.getProjectUniqueIdentifer()) {
                        developerWorkingProjects.remove(i);
                        developerWorkingProjects.add(project);
                        break;
                    }
                }

//                1. Establish Connection
                try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {


                    //2. Connect to the Database
                    MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


                    //3. Connect to the collection
                    MongoCollection<Document> collection = db.getCollection("Developers");

                    System.out.println(project.getProjectTeamHead().getId());

                    // First Remove The Current Working Projects
                    for (int i = 0; i < size_of_array_working_projects; i++) {
                        collection.updateOne(Filters.eq("userName", oldUserName), Updates.popFirst("currentWorkingProjects"));
                    }

                    // Update a document
                    for (Project projectDetailsObj : developerWorkingProjects) {
                        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                        Document projectDocumentDoc = obj1.convertProject(projectDetailsObj);
                        collection.updateOne(Filters.eq("userName", oldUserName), Updates.push("currentWorkingProjects", projectDocumentDoc));
                    }
                }
            }
        }

        // Now check for the new developers that came in
        boolean second_check_fail=true;
        for(String newUsername : updatedDevelopersUsernameWorkingOnThisProject)
        {
            boolean flag=true;
            for(String oldUserName : oldDevelopersUsernameWorkingOnThisProject)
            {
                if(newUsername.compareTo(oldUserName) ==0)
                {
                    flag=false;
                }
            }

            if(flag)
            {
                second_check_fail=false;
                //1. Establish Connection
                try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {


                    //2. Connect to the Database
                    MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


                    //3. Connect to the collection
                    MongoCollection<Document> collection = db.getCollection("Developers");

                    Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                    Document projectDocumentDoc = obj1.convertProject(project);
                    collection.updateOne(Filters.eq("userName", newUsername), Updates.push("currentWorkingProjects", projectDocumentDoc));
                }
            }
        }

        // if developer is same but tester object has some changes

        if(first_check_fail ==true && second_check_fail == true)
        {
            for(String oldDeveloper : oldDevelopersUsernameWorkingOnThisProject)
            {
                // get the delete developer all working projects
                ArrayList<Project> developerWorkingProjects = get_All_Developers_Working_Projects(oldDeveloper);

                // pop the current project on which this function is called : project will contain the developers list which will have not this current developer
                int size_of_array_working_projects = developerWorkingProjects.size();

                // Removing the project from the developer list

                for ( int i = 0; i < developerWorkingProjects.size(); i++) {
                    if (developerWorkingProjects.get(i).getProjectUniqueIdentifer() == project.getProjectUniqueIdentifer()) {
                        developerWorkingProjects.remove(i);

                        //add the updated version in the list
                        developerWorkingProjects.add(project);
                        break;
                    }
                }

//                1. Establish Connection
                try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {


                    //2. Connect to the Database
                    MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


                    //3. Connect to the collection
                    MongoCollection<Document> collection = db.getCollection("Developers");

                    System.out.println(project.getProjectTeamHead().getId());

                    // First Remove The Current Working Projects
                    for (int i = 0; i < size_of_array_working_projects; i++) {
                        collection.updateOne(Filters.eq("userName", oldDeveloper), Updates.popFirst("currentWorkingProjects"));
                    }

                    // Update a document
                    for (Project projectDetailsObj : developerWorkingProjects) {

                        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                        Document projectDocumentDoc = obj1.convertProject(projectDetailsObj);
                        collection.updateOne(Filters.eq("userName", oldDeveloper), Updates.push("currentWorkingProjects", projectDocumentDoc));
                    }
                }
            }

        }

    }


    //------------------------------------- Get Developer Details from Username ----------------------------------------
    public Developer getDeveloperRecord(String developerUsername)
    {
        Developer queryDeveloper = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Developers");

        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("userName", developerUsername);
        MongoCursor<Document> cursor = collection.find(whereQuery).iterator();


        while (cursor.hasNext()) {
            var doc = cursor.next();

            //----------- Mapping of Document to Java Object --------------------------
            ObjectId mongoId = doc.getObjectId("_id");
            String name = doc.getString("name");
            String cnic = doc.getString("cnic");
            String contactNo = doc.getString("contactNo");
            String email = doc.getString("email");
            String userName1 = doc.getString("userName");
            String userPassword = doc.getString("userPassword");
            queryDeveloper = new Developer(name, cnic, contactNo, email, userName1, userPassword);
        }
        return queryDeveloper;
    }

    public Developer verifyDeveloperCredentials(String developerUsername, String developerPassword)
    {
        Developer queryDeveloper = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Developers");

        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("userName", developerUsername);
        whereQuery.put("userPassword", developerPassword);
        MongoCursor<Document> cursor = collection.find(whereQuery).iterator();


        while (cursor.hasNext()) {
            var doc = cursor.next();

            //----------- Mapping of Document to Java Object --------------------------
            String name = doc.getString("name");
            String cnic = doc.getString("cnic");
            String contactNo = doc.getString("contactNo");
            String email = doc.getString("email");
            String userName1 = doc.getString("userName");
            String userPassword = doc.getString("userPassword");
            queryDeveloper = new Developer(name, cnic, contactNo, email, userName1, userPassword);
        }
        return queryDeveloper;
    }
}


