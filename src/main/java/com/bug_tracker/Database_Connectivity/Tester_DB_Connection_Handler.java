package com.bug_tracker.Database_Connectivity;

import com.bug_tracker.Business_Logic.Developer;
import com.bug_tracker.Business_Logic.Project;
import com.bug_tracker.Business_Logic.Team_Head;
import com.bug_tracker.Business_Logic.Tester;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.beans.binding.ObjectExpression;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;


public class Tester_DB_Connection_Handler {

    // Insert New Developer in to the mongo DB
    public  void addNewTesterInDB(Tester object) {

//        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
//        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Testers");


        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

        Document newDeveloperDoc = obj1.convertTester(object);

        //4. Insert the new Object
        collection.insertOne(newDeveloperDoc);
        System.out.println("New Tester Inserted");

        mongoClient.close();
    }

    // Fetch All Testers Record
    public  ArrayList<Tester> fetchAll_Testers_Record() {

        ArrayList<Tester> team_heads = new ArrayList<>();
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");

        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Testers");

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

                Tester tester = new Tester(name, cnic, contactNo, email, userName, userPassword);
                tester.setMongoId(mongoId);
                team_heads.add(tester);
            }
        }
        mongoClient.close();
        return team_heads;
    }

    // Delete Team Head Record
    public  boolean Delete_Tester(ObjectId mongoID) {
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Testers");

        // Delete the document
        collection.deleteOne(Filters.eq("_id", mongoID));
        System.out.println("Tester Record Delete Successful");

        return true;
    }

    //-------------------------------------- Get all Testers Working Project -------------------------------------------
    public  ArrayList<Project> get_All_Testers_Working_Projects(String testerUsername) {

        ArrayList<Project> currentWorkingProjects = null;

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Testers");

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

    //-------------------------------------- Add new project in the tester document ------------------------------------
    public  void insertNewProjectInTester(Project projectObject, Tester testerObject) {
        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

        Document newProjectDocument = obj1.convertProject(projectObject);


        Team_Head queryTeamHead = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Testers");

        //4. Push New Project in to the team head record
        collection.updateOne(Filters.eq("_id", testerObject.getMongoId()), Updates.push("currentWorkingProjects", newProjectDocument));
    }


    //------------------------------------- Update the Developer Project Details ------------------------------------------
    public  void updateProjectDetails_TesterCollection(Project project, ArrayList<String> oldTestersUsernameWorkingOnThisProject) {

        Project_DB_Connection_Handler dbHandlerProject=new Project_DB_Connection_Handler();

        //get all the developers' username working on the project
        ArrayList<String> updatedTestersUsernameWorkingOnThisProject = dbHandlerProject.getTestersTeamList(project);

        // Traverse the projects developers username and match with the old developers username
        for (String oldUserName : oldTestersUsernameWorkingOnThisProject) {
            boolean flag = true;
            for (String updatedDeveloper : updatedTestersUsernameWorkingOnThisProject) {
                if (oldUserName.compareTo(updatedDeveloper) == 0) {
                    flag = false;
                    continue;
                }
            }

            if (flag == true) {

                // get the delete developer all working projects
                ArrayList<Project> testers_working_projects = get_All_Testers_Working_Projects(oldUserName);

                // pop the current project on which this function is called : project will contain the developers list which will have not this current developer
                int size_of_array_working_projects =testers_working_projects.size();

                // Removing the project from the developer list
                for(int i=0; i<testers_working_projects.size();i++)
                {
                    if(testers_working_projects.get(i).getProjectUniqueIdentifer() == project.getProjectUniqueIdentifer())
                    {
                        testers_working_projects.remove(i);
                        break;
                    }
                }
//                1. Establish Connection
                try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {


                    //2. Connect to the Database
                    MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


                    //3. Connect to the collection
                    MongoCollection<Document> collection = db.getCollection("Testers");

                    System.out.println(project.getProjectTeamHead().getId());

                    // First Remove The Current Working Projects
                    for (int i = 0; i < size_of_array_working_projects; i++) {
                        collection.updateOne(Filters.eq("userName", oldUserName), Updates.popFirst("currentWorkingProjects"));
                    }

                    // Update a document
                    for (Project projectDetailsObj : testers_working_projects) {
                        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                        Document projectDocumentDoc = obj1.convertProject(projectDetailsObj);
                        collection.updateOne(Filters.eq("userName", oldUserName), Updates.push("currentWorkingProjects", projectDocumentDoc));
                    }
                }
            }
            // change made 17-5-2022
            else if (flag == false) {

//                first_check_fail = false;
                // get the delete developer all working projects
                ArrayList<Project> testers_working_projects = get_All_Testers_Working_Projects(oldUserName);


                // pop the current project on which this function is called : project will contain the developers list which will have not this current developer
                int size_of_array_working_projects = testers_working_projects.size();

                // Removing the project from the developer list

                for (int i = 0; i < testers_working_projects.size(); i++) {
                    if (testers_working_projects.get(i).getProjectUniqueIdentifer() == project.getProjectUniqueIdentifer()) {
                        testers_working_projects.remove(i);
                        testers_working_projects.add(project);
                        break;
                    }
                }

//                1. Establish Connection
                try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {


                    //2. Connect to the Database
                    MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


                    //3. Connect to the collection
                    MongoCollection<Document> collection = db.getCollection("Testers");

                    System.out.println(project.getProjectTeamHead().getId());

                    // First Remove The Current Working Projects
                    for (int i = 0; i < size_of_array_working_projects; i++) {
                        collection.updateOne(Filters.eq("userName", oldUserName), Updates.popFirst("currentWorkingProjects"));
                    }

                    // Update a document
                    for (Project projectDetailsObj : testers_working_projects) {
                        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                        Document projectDocumentDoc = obj1.convertProject(projectDetailsObj);
                        collection.updateOne(Filters.eq("userName", oldUserName), Updates.push("currentWorkingProjects", projectDocumentDoc));
                    }
                }
            }
        }
        // Now check for the new testers that came in
        for(String newUsername : updatedTestersUsernameWorkingOnThisProject)
        {
            boolean flag=true;
            for(String oldUserName : oldTestersUsernameWorkingOnThisProject)
            {
                if(newUsername.compareTo(oldUserName) ==0)
                {
                    flag=false;
                }
            }

            if(flag)
            {
                //1. Establish Connection
                try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {


                    //2. Connect to the Database
                    MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


                    //3. Connect to the collection
                    MongoCollection<Document> collection = db.getCollection("Testers");

                    Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                    Document projectDocumentDoc = obj1.convertProject(project);
                    collection.updateOne(Filters.eq("userName", newUsername), Updates.push("currentWorkingProjects", projectDocumentDoc));
                }
            }
        }
    }

    //------------------------------------ Find Team Head Credentials --------------------------------------------------
    //Find Tester Credentials
    public  Tester findTesterDetails(String userName, String password) {
        Tester queryTester = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Testers");

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

            queryTester = new Tester(name, cnic, contactNo, email, userName1, userPassword);
        }
        return queryTester;
    }
}
