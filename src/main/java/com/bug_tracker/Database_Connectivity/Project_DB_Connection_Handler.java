package com.bug_tracker.Database_Connectivity;

import com.bug_tracker.Business_Logic.*;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

public class Project_DB_Connection_Handler {

    // Insert New Developer in to the mongo DB
    public  void addNewProject(Project object) {

//        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
//        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");


        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();
        Document newDeveloperDoc = obj1.convertProject(object);

        //4. Insert the new Object
        collection.insertOne(newDeveloperDoc);
        System.out.println("New Project Inserted Inserted");

        mongoClient.close();
    }

    // Find a Project Details from the project unique identifier
    public  Project findProjectDetails(int projectUniqueIdentifier) {
        Project qureyProject = null;

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");

        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("projectUniqueID", projectUniqueIdentifier);


        for (Document document : collection.find(whereQuery)) {
            System.out.println("project founded !!!!");

            var doc = document;

            if (doc.getInteger("projectUniqueID") == projectUniqueIdentifier) {
                //----------- Mapping of Document to Java Object --------------------------
                String projectName = doc.getString("projectName");
                String clientName = doc.getString("clientName");
                String description = doc.getString("description");

                Date startingDate = doc.getDate("startingDate");
                Date endingDate = doc.getDate("endingDate");

                int projectID = doc.getInteger("projectUniqueID");
                String repositoryLink = doc.getString("repositoryLink");
                String currentProjectStatus = doc.getString("projectCurrentStatus");

                Project projectDetails = new Project(projectName, clientName, Helper_Functions.convertToLocalDateViaInstant(startingDate), Helper_Functions.convertToLocalDateViaInstant(endingDate), description, repositoryLink);
                projectDetails.setProjectUniqueIdentifer(projectID);
                projectDetails.setProjectCurrentStatus(currentProjectStatus);

                Document teamHeadDoc = (Document) doc.get("teamHead");
                TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
                Team_Head teamHeadDetails = teamHeadDbConnectionHandler.findTeamHead(teamHeadDoc.getString("Teamhead_ID"));
                projectDetails.setProjectTeamHead(teamHeadDetails);

                //------------- All Testers Details ----------------------------------------------------------------
                ArrayList<Tester> testersTeam = new ArrayList<>();
                for (Document currentTester : (ArrayList<Document>) doc.get("testersTeam")) {
                    String testerName = currentTester.getString("name");
                    String testerCNIC = currentTester.getString("cnic");
                    String testerEmail = currentTester.getString("email");
                    String testerContact = currentTester.getString("contactNo");
                    String testerUserName = currentTester.getString("userName");

                    //------- Get All Working Projects of the Tester -----------------------------------------------
                    Tester currentTesterObject = new Tester(testerName, testerCNIC, testerContact, testerEmail, testerUserName, null);
                    currentTesterObject.setCurrentWorkingProjects(null);

                    //append in the list
                    testersTeam.add(currentTesterObject);
                }

                // Set The Testers Team of the project
                projectDetails.setTestersTeam(testersTeam);

                //------------- All Developers Details -------------------------------------------------------------
                ArrayList<Developer> developersTeam = new ArrayList<>();
                for (Document currentTester : (ArrayList<Document>) doc.get("developersTeam")) {
                    String developerName = currentTester.getString("name");
                    String developerCNIC = currentTester.getString("cnic");
                    String developerEmail = currentTester.getString("email");
                    String developerContact = currentTester.getString("contactNo");
                    String developerUserName = currentTester.getString("userName");


                    //------- Get All Working Projects of the Tester -----------------------------------------------
                    Developer currentDeveloper = new Developer(developerName, developerCNIC, developerEmail, developerContact, developerUserName, null);
                    currentDeveloper.setCurrentWorkingProjects(null);

                    //append in the list
                    developersTeam.add(currentDeveloper);
                }

                // Set The Developers Team of the project
                projectDetails.setDevelopersTeam(developersTeam);

                qureyProject = projectDetails;
            }

            // Update the reported bugs list

            qureyProject.setList_Of_Bugs_Reported(getAllReportedBugs(projectUniqueIdentifier));
        }
        return qureyProject;
    }

    //-------------------------- Functions Related To Delete Project Details --------------------------------------
    // Remove A Project Details From the project collect but store in the close projects collection
    public  boolean removeProjectDetails(Project projectDetails) {


        //------ Remove Project From Tester Record ---------------------------------------------------------------------
        removeProjectFromTester(projectDetails);

        //------ Remove Project From Developer Record ------------------------------------------------------------------
        removeProjectFromDeveloper(projectDetails);

        //------ Remove Project From Team Head Record ------------------------------------------------------------------
        removeProjectDetailsFromTeamHead(projectDetails);

        //---------------------- Move the project Details to Closed Project Details ------------------------------------
//        projectDetails.setProjectCurrentStatus("Project Removed By the Team Head" + projectDetails.getProjectTeamHead().getName());

        moveProjectToClosedProjects(projectDetails);


        //------------ First Remove the Project From Project Collection ------------------------------------------------
        //1. Establish Connection

        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");

        // Delete the document
        collection.deleteOne(Filters.eq("projectUniqueID", projectDetails.getProjectUniqueIdentifer()));
        System.out.println("Project Delete Successful");

        mongoClient.close();

        return true;
    }

    // Get list of all testers working on the particular project
    public  ArrayList<String> getTestersTeamList(Project projectDetails) {
        ArrayList<String> testerTeamUserNames = null;

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");


        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("projectUniqueID", projectDetails.getProjectUniqueIdentifer());
        MongoCursor<Document> cursor = collection.find(whereQuery).iterator();

        while (cursor.hasNext()) {
            System.out.println("Project Record founded !!!!");

            var doc = cursor.next();
            testerTeamUserNames = new ArrayList<>();

            //----------- Finding Array of Testers --------------------------
            for (Document currentTester : (ArrayList<Document>) doc.get("testersTeam")) {
                testerTeamUserNames.add(currentTester.getString("userName"));
            }
        }

        return testerTeamUserNames;
    }

    // Remove Project Details From the Tester Document
    public  void removeProjectFromTester(Project selectedProjectDetails) {
        ArrayList<String> allTestersUserNameWorkingOnProject = getTestersTeamList(selectedProjectDetails);

        ArrayList<Project> allWorkingProjectsOfTester = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Testers");

        for (String currentTesterUserName : allTestersUserNameWorkingOnProject) {
            //4. Search For the login username and password
            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("userName", currentTesterUserName);
            MongoCursor<Document> cursor = collection.find(whereQuery).iterator();

            allWorkingProjectsOfTester = new ArrayList<>();

            while (cursor.hasNext()) {
                var doc = cursor.next();

                for (Document projectDocument : (ArrayList<Document>) doc.get("currentWorkingProjects")) {
                    System.out.println(projectDocument);

                    String projectName = projectDocument.getString("projectName");
                    String clientName = projectDocument.getString("clientName");
                    String description = projectDocument.getString("description");

                    Date startingDate = projectDocument.getDate("startingDate");
                    Date endingDate = projectDocument.getDate("endingDate");

                    int projectID = projectDocument.getInteger("projectUniqueID");
                    String repositoryLink = projectDocument.getString("repositoryLink");
                    String currentProjectStatus = projectDocument.getString("projectCurrentStatus");

                    //---------------------- Getting The Team Head Details of Project ----------------------------------

                    Project projectDetails = new Project(projectName, clientName, Helper_Functions.convertToLocalDateViaInstant(startingDate), Helper_Functions.convertToLocalDateViaInstant(endingDate), description, repositoryLink);
                    projectDetails.setProjectUniqueIdentifer(projectID);
                    projectDetails.setProjectCurrentStatus(currentProjectStatus);
                    projectDetails.setProjectTeamHead(selectedProjectDetails.getProjectTeamHead());

                    //------------- All Testers Details ----------------------------------------------------------------
                    ArrayList<Tester> testersTeam = new ArrayList<>();
                    for (Document currentTester : (ArrayList<Document>) projectDocument.get("testersTeam")) {
                        String testerName = currentTester.getString("name");
                        String testerCNIC = currentTester.getString("cnic");
                        String testerEmail = currentTester.getString("email");
                        String testerContact = currentTester.getString("contact");

                        //------- Get All Working Projects of the Tester -----------------------------------------------
                        Tester currentTesterObject = new Tester(testerName, testerCNIC, testerContact, testerEmail, null, null);
                        currentTesterObject.setCurrentWorkingProjects(null);

                        //append in the list
                        testersTeam.add(currentTesterObject);
                    }

                    // Set The Testers Team of the project
                    projectDetails.setTestersTeam(testersTeam);

                    //------------- All Developers Details -------------------------------------------------------------
                    ArrayList<Developer> developersTeam = new ArrayList<>();
                    for (Document currentTester : (ArrayList<Document>) projectDocument.get("developersTeam")) {
                        String developerName = currentTester.getString("name");
                        String developerCNIC = currentTester.getString("cnic");
                        String developerEmail = currentTester.getString("email");
                        String developerContact = currentTester.getString("contact");

                        //------- Get All Working Projects of the Tester -----------------------------------------------
                        Developer currentDeveloper = new Developer(developerName, developerCNIC, developerEmail, developerContact, null, null);
                        currentDeveloper.setCurrentWorkingProjects(null);

                        //append in the list
                        developersTeam.add(currentDeveloper);
                    }

                    // Set The Developers Team of the project
                    projectDetails.setDevelopersTeam(developersTeam);

                    allWorkingProjectsOfTester.add(projectDetails);
                }

                //---------- Traverse on all the working project and find the qurey project ----------------------------
                int totalWorkingProjectNumber = allWorkingProjectsOfTester.size();
                for (int i = 0; i < allWorkingProjectsOfTester.size(); i++) {
                    if (allWorkingProjectsOfTester.get(i).getProjectUniqueIdentifer() == selectedProjectDetails.getProjectUniqueIdentifer()) {
                        allWorkingProjectsOfTester.remove(i);
                    }
                }

                //--------- Update the Nested Document in the Tester Document in Mongo DB Tester Collection
                // First Remove The Collection
                for (int i = 0; i < totalWorkingProjectNumber; i++) {
                    collection.updateOne(Filters.eq("userName", currentTesterUserName), Updates.popFirst("currentWorkingProjects"));
                }

                //Insert the new updated one
                for (int i = 0; i < allWorkingProjectsOfTester.size(); i++) {

                    Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                    Document newProjectDocument = obj1.convertProject(allWorkingProjectsOfTester.get(i));
                    collection.updateOne(Filters.eq("userName", currentTesterUserName), Updates.push("currentWorkingProjects", newProjectDocument));
                }


                allWorkingProjectsOfTester.clear();
            }


        }
    }

    // Get All Developers Username working on the particular project
    public  ArrayList<String> getDevelopersTeamList(Project projectDetails) {
        ArrayList<String> developersTeamUserName = null;

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");


        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("projectUniqueID", projectDetails.getProjectUniqueIdentifer());
        MongoCursor<Document> cursor = collection.find(whereQuery).iterator();

        while (cursor.hasNext()) {
            System.out.println("Project Record founded !!!!");

            var doc = cursor.next();
            developersTeamUserName = new ArrayList<>();

            //----------- Finding Array of Testers --------------------------
            for (Document currentTester : (ArrayList<Document>) doc.get("developersTeam")) {
                System.out.println(currentTester.getString("userName"));
                developersTeamUserName.add(currentTester.getString("userName"));
            }
        }

        return developersTeamUserName;
    }

    // Remove Project Details From Developer Document
    public  void removeProjectFromDeveloper(Project selectedProjectDetails) {
        ArrayList<String> allTestersUserNameWorkingOnProject = getDevelopersTeamList(selectedProjectDetails);

        ArrayList<Project> allWorkingProjectsOfDeveloper = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Developers");

        for (String currentTesterUserName : allTestersUserNameWorkingOnProject) {
            //4. Search For the login username and password
            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("userName", currentTesterUserName);
            MongoCursor<Document> cursor = collection.find(whereQuery).iterator();

            allWorkingProjectsOfDeveloper = new ArrayList<>();

            while (cursor.hasNext()) {
                var doc = cursor.next();

                for (Document projectDocument : (ArrayList<Document>) doc.get("currentWorkingProjects")) {
                    System.out.println(projectDocument);

                    String projectName = projectDocument.getString("projectName");
                    String clientName = projectDocument.getString("clientName");
                    String description = projectDocument.getString("description");

                    Date startingDate = projectDocument.getDate("startingDate");
                    Date endingDate = projectDocument.getDate("endingDate");

                    int projectID = projectDocument.getInteger("projectUniqueID");
                    String repositoryLink = projectDocument.getString("repositoryLink");
                    String currentProjectStatus = projectDocument.getString("projectCurrentStatus");

                    //---------------------- Getting The Team Head Details of Project ----------------------------------

                    Project projectDetails = new Project(projectName, clientName, null, null, description, repositoryLink);
                    projectDetails.setProjectUniqueIdentifer(projectID);
                    projectDetails.setProjectCurrentStatus(currentProjectStatus);
                    projectDetails.setProjectTeamHead(selectedProjectDetails.getProjectTeamHead());

                    //------------- All Testers Details ----------------------------------------------------------------
                    ArrayList<Tester> testersTeam = new ArrayList<>();
                    for (Document currentTester : (ArrayList<Document>) projectDocument.get("testersTeam")) {
                        String testerName = currentTester.getString("name");
                        String testerCNIC = currentTester.getString("cnic");
                        String testerEmail = currentTester.getString("email");
                        String testerContact = currentTester.getString("contact");

                        //------- Get All Working Projects of the Tester -----------------------------------------------
                        Tester currentTesterObject = new Tester(testerName, testerCNIC, testerContact, testerEmail, null, null);
                        currentTesterObject.setCurrentWorkingProjects(null);

                        //append in the list
                        testersTeam.add(currentTesterObject);
                    }

                    // Set The Testers Team of the project
                    projectDetails.setTestersTeam(testersTeam);

                    //------------- All Developers Details -------------------------------------------------------------
                    ArrayList<Developer> developersTeam = new ArrayList<>();
                    for (Document currentTester : (ArrayList<Document>) projectDocument.get("developersTeam")) {
                        String developerName = currentTester.getString("name");
                        String developerCNIC = currentTester.getString("cnic");
                        String developerEmail = currentTester.getString("email");
                        String developerContact = currentTester.getString("contact");

                        //------- Get All Working Projects of the Tester -----------------------------------------------
                        Developer currentDeveloper = new Developer(developerName, developerCNIC, developerEmail, developerContact, null, null);
                        currentDeveloper.setCurrentWorkingProjects(null);

                        //append in the list
                        developersTeam.add(currentDeveloper);
                    }

                    // Set The Developers Team of the project
                    projectDetails.setDevelopersTeam(developersTeam);

                    allWorkingProjectsOfDeveloper.add(projectDetails);
                }

                //---------- Traverse on all the working project and find the qurey project ----------------------------
                int totalWorkingProjectNumber = allWorkingProjectsOfDeveloper.size();
                for (int i = 0; i < allWorkingProjectsOfDeveloper.size(); i++) {
                    if (allWorkingProjectsOfDeveloper.get(i).getProjectUniqueIdentifer() == selectedProjectDetails.getProjectUniqueIdentifer()) {
                        allWorkingProjectsOfDeveloper.remove(i);
                    }
                }

                //--------- Update the Nested Document in the Tester Document in Mongo DB Tester Collection

                // First Remove The Collection
                for (int i = 0; i < totalWorkingProjectNumber; i++) {
                    collection.updateOne(Filters.eq("userName", currentTesterUserName), Updates.popFirst("currentWorkingProjects"));
                }

                //Insert the new updated one
                for (int i = 0; i < allWorkingProjectsOfDeveloper.size(); i++) {

                    Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

                    Document newProjectDocument = obj1.convertProject(allWorkingProjectsOfDeveloper.get(i));
                    collection.updateOne(Filters.eq("userName", currentTesterUserName), Updates.push("currentWorkingProjects", newProjectDocument));
                }
                allWorkingProjectsOfDeveloper.clear();
            }
        }
    }

    // Remove Project Details From The Team Head Document
    public  void removeProjectDetailsFromTeamHead(Project selectedProjectDetails) {

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Team_Heads");

        TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();

        ArrayList<Project> allOngoingProjectsOfTeamHead = teamHeadDbConnectionHandler.getAllTeam_HeadWorkingProjectDetails(selectedProjectDetails.getProjectTeamHead().getUserName());

        int numberofOngoingProjects = allOngoingProjectsOfTeamHead.size();

        // First Remove Project From the list
        for (int i = 0; i < allOngoingProjectsOfTeamHead.size(); i++) {
            if (allOngoingProjectsOfTeamHead.get(i).getProjectUniqueIdentifer() == selectedProjectDetails.getProjectUniqueIdentifer()) {
                allOngoingProjectsOfTeamHead.remove(i);
            }
        }

        // First Empty the currentWorkingProject Attribute of the Document
        for (int i = 0; i < numberofOngoingProjects; i++) {
            collection.updateOne(Filters.eq("userName", selectedProjectDetails.getProjectTeamHead().getUserName()), Updates.popFirst("currentWorkingProjects"));
        }


        // Insert the new update current working projects list
        //Insert the new updated one
        for (int i = 0; i < allOngoingProjectsOfTeamHead.size(); i++) {
            Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

            Document newProjectDocument = obj1.convertProject(allOngoingProjectsOfTeamHead.get(i));
            collection.updateOne(Filters.eq("userName", selectedProjectDetails.getProjectTeamHead().getUserName()), Updates.push("currentWorkingProjects", newProjectDocument));
        }
    }

    // Move A Project To Closed Projects Collection --------------------------------------------------------------------
    public  void moveProjectToClosedProjects(Project projectDetails) {

        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Closed_Projects");

        //4. Making the document of the closed project
        Object_To_Document_Conversions obj1 =new Object_To_Document_Conversions();

        Document closedProjectDocument = obj1.convertProject(projectDetails);


        //5. Insert the document in to the collection
        collection.insertOne(closedProjectDocument);

        mongoClient.close();
    }

    //-------------------- Update Project Details ----------------------------------------------------------------------
    public  void updateProjectDetails(Project project) {
        updateProjectDetails_ProjectCollection(project);

    }

    private  void updateProjectDetails_ProjectCollection(Project project) {

        ArrayList<Document> listOfTesterWorkingOnProject_Docs = new ArrayList<>();
        ArrayList<Document> listOfDevelopersWorkingOnProject_Docs = new ArrayList<>();

        Object_To_Document_Conversions object =new Object_To_Document_Conversions();
        for (Tester iteratorTester : project.getTestersTeam()) {


            Document newTesterDocs = object.convertTesterAccordingToProject(iteratorTester);
            listOfTesterWorkingOnProject_Docs.add(newTesterDocs);
        }

        for (Developer iteratorDeveloper : project.getDevelopersTeam()) {
            Document newTesterDocs = object.convertDeveloperAccordingToProject(iteratorDeveloper);
            listOfDevelopersWorkingOnProject_Docs.add(newTesterDocs);
        }


        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");

        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");

        //4.Update a document
        collection.updateOne(Filters.eq("projectUniqueID", project.getProjectUniqueIdentifer()),
                Updates.combine(
                        Updates.set("projectName", project.getProjectName()),
                        Updates.set("clientName", project.getClientName()),
                        Updates.set("description", project.getProjectDescription()),
                        Updates.set("startingDate", project.getProjectStartingDate()),
                        Updates.set("endingDate", project.getProjectEndingDate()),
                        Updates.set("repositoryLink", project.getRepositoryLink()),
                        Updates.set("testersTeam", listOfTesterWorkingOnProject_Docs),
                        Updates.set("developersTeam", listOfDevelopersWorkingOnProject_Docs),
                        Updates.set("projectCurrentStatus",project.getProjectCurrentStatus())
                )
        );
        mongoClient.close();
    }

    //------------------ Insert A New Project Bug In the Project -------------------------------------------------------
    public  void insertNewBug(Bug reportedBug, int projectUniqueID) {

        Object_To_Document_Conversions object=new Object_To_Document_Conversions();
        Document BugDocument = object.convertBugToDocument(reportedBug);
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");

        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");

        //4.Update a document
        collection.updateOne(Filters.eq("projectUniqueID", projectUniqueID), Updates.push("reportedBugs", BugDocument));

        mongoClient.close();
    }

    //--------------- Get Array List of all the bugs details reported in the project -----------------------------------
    public  ArrayList<Bug> getAllReportedBugs(int projectUniqueID) {

        ArrayList<Bug> bugsList = null;

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");

        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("projectUniqueID", projectUniqueID);
        MongoCursor<Document> cursor = collection.find(whereQuery).iterator();

        while (cursor.hasNext()) {
            var doc = cursor.next();

            if (doc.getInteger("projectUniqueID") == projectUniqueID) {
                //------------- Get Bugs List ----------------------------------------------------------------
                bugsList = new ArrayList<>();

                for (Document bug : (ArrayList<Document>) doc.get("reportedBugs")) {
                    String bugUniqueID = bug.getString("bugUniqueID");
                    String bugTesterAuthorUsername = bug.getString("testAuthorUsername");
                    String bugTitle = bug.getString("bugTitle");
                    String bugDescription = bug.getString("bugDescription");
                    Date bugAssignmentDate = bug.getDate("bugAssignmentDate");
                    String codeLineNumber = bug.getString("codeLineNumber");
                    String repositoryCommitHash = bug.getString("repositoryCommitHash");
                    String message = bug.getString("message");
                    String bugCategory = bug.getString("bugCategory");
                    String bugStatus = bug.getString("bugStatus");
                    String assignedDeveloperUsername = bug.getString("assignedDeveloperUsername");
                    String describeSolution =bug.getString("describeSolution");
                    Date closingDate=bug.getDate("closingDate");
                    boolean bugFixStatus =bug.getBoolean("bugFixFlag");

                    Developer_DB_Connection_Handler dbHandlerDev = new Developer_DB_Connection_Handler();
                    Developer bugAssignedDeveloper = dbHandlerDev.getDeveloperRecord(assignedDeveloperUsername);

                    Bug bugRecord = new Bug(bugTesterAuthorUsername, bugTitle, bugDescription, Helper_Functions.convertToLocalDateViaInstant(bugAssignmentDate), bugStatus, codeLineNumber, repositoryCommitHash, message, bugCategory, bugAssignedDeveloper);
                    bugRecord.setBugUniqueID(bugUniqueID);
                    bugRecord.setDescribeSolution(describeSolution);

                    if(closingDate!=null)
                        bugRecord.setClosingDate(Helper_Functions.convertToLocalDateViaInstant(closingDate));

                    bugRecord.setBugFixFlag(bugFixStatus);

                    bugsList.add(bugRecord);
                }
            }
        }
        return bugsList;
    }

    public  void updateBugDetailsFromDB(ArrayList<Bug> updatedBugDetails, int projectUniqueID) {

        int noReportedBugsOfProject = getAllReportedBugs(projectUniqueID).size();

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");

        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");

        // First Pop All The Old Bugs Reported List
        for (int i = 0; i < noReportedBugsOfProject; i++) {
            collection.updateOne(Filters.eq("projectUniqueID",projectUniqueID), Updates.popFirst("reportedBugs"));
        }

        // Then Insert the new list in the bugs reported list
        for (int i = 0; i < updatedBugDetails.size(); i++) {

            // convert new bug to document and push the document in the reported bugs
            Object_To_Document_Conversions object=new Object_To_Document_Conversions();
            Document bugDocument =object.convertBugToDocument(updatedBugDetails.get(i));
            collection.updateOne(Filters.eq("projectUniqueID",projectUniqueID), Updates.push("reportedBugs", bugDocument));
        }
        mongoClient.close();

    }

    //---------------- Get All Projects From The Database (Open and Closed) --------------------------------------------
    public  ArrayList<Project> getAllProjectsInDatabase()
    {
        ArrayList allProjectsInDatabase =new ArrayList();

        for(Project openProject :getAllOpenProjectDetails())
        {
            allProjectsInDatabase.add(openProject);
        }

        for(Project closedProject :getAllClosedProjects())
        {
            allProjectsInDatabase.add(closedProject);
        }
        return allProjectsInDatabase;
    }

    //GET ALL OPENED PROJECTS DETAILS ONLY
    public  ArrayList<Project> getAllOpenProjectDetails()
    {
        ArrayList<Project>allProjectsInDatabase=new ArrayList<>();
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Projects");

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            Project qureyProject=null;
            while (cursor.hasNext())
            {
                var doc = cursor.next();

                //----------- Mapping of Document to Java Object --------------------------
                String projectName = doc.getString("projectName");
                String clientName = doc.getString("clientName");
                String description = doc.getString("description");

                Date startingDate = doc.getDate("startingDate");
                Date endingDate = doc.getDate("endingDate");

                int projectID = doc.getInteger("projectUniqueID");
                String repositoryLink = doc.getString("repositoryLink");
                String currentProjectStatus = doc.getString("projectCurrentStatus");

                Project projectDetails = new Project(projectName, clientName, Helper_Functions.convertToLocalDateViaInstant(startingDate), Helper_Functions.convertToLocalDateViaInstant(endingDate), description, repositoryLink);
                projectDetails.setProjectUniqueIdentifer(projectID);
                projectDetails.setProjectCurrentStatus(currentProjectStatus);

                Document teamHeadDoc =(Document) doc.get("teamHead");

                TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();

                Team_Head teamHeadDetails = teamHeadDbConnectionHandler.findTeamHead(teamHeadDoc.getString("Teamhead_ID"));
                projectDetails.setProjectTeamHead(teamHeadDetails);

                //------------- All Testers Details ----------------------------------------------------------------
                ArrayList<Tester> testersTeam = new ArrayList<>();
                for (Document currentTester : (ArrayList<Document>) doc.get("testersTeam")) {
                    String testerName = currentTester.getString("name");
                    String testerCNIC = currentTester.getString("cnic");
                    String testerEmail = currentTester.getString("email");
                    String testerContact = currentTester.getString("contactNo");
                    String testerUserName = currentTester.getString("userName");

                    //------- Get All Working Projects of the Tester -----------------------------------------------
                    Tester currentTesterObject = new Tester(testerName, testerCNIC, testerContact, testerEmail, testerUserName, null);

                    Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
                    currentTesterObject.setCurrentWorkingProjects(testerDbConnectionHandler.get_All_Testers_Working_Projects(testerUserName));

                    //append in the list
                    testersTeam.add(currentTesterObject);
                }

                // Set The Testers Team of the project
                projectDetails.setTestersTeam(testersTeam);

                //------------- All Developers Details -------------------------------------------------------------
                ArrayList<Developer> developersTeam = new ArrayList<>();
                for (Document currentTester : (ArrayList<Document>) doc.get("developersTeam")) {
                    String developerName = currentTester.getString("name");
                    String developerCNIC = currentTester.getString("cnic");
                    String developerEmail = currentTester.getString("email");
                    String developerContact = currentTester.getString("contactNo");
                    String developerUserName = currentTester.getString("userName");


                    //------- Get All Working Projects of the Tester -----------------------------------------------
                    Developer currentDeveloper = new Developer(developerName, developerCNIC, developerEmail, developerContact, developerUserName, null);

                    Developer_DB_Connection_Handler dbHandlerDev = new Developer_DB_Connection_Handler();
                    currentDeveloper.setCurrentWorkingProjects(dbHandlerDev.get_All_Developers_Working_Projects(developerUserName));

                    //append in the list
                    developersTeam.add(currentDeveloper);
                }

                // Set The Developers Team of the project
                projectDetails.setDevelopersTeam(developersTeam);
                // Update the reported bugs list
                projectDetails.setList_Of_Bugs_Reported(getAllReportedBugs(projectID));

                allProjectsInDatabase.add(projectDetails);
            }
        }


        mongoClient.close();
        return allProjectsInDatabase;
    }

    // GET ALL CLOSED PROJECT DETAILS ONLY
    public  ArrayList<Project> getAllClosedProjects()
    {
        ArrayList<Project>allProjectsInDatabase=new ArrayList<>();
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Closed_Projects");

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            Project qureyProject=null;
            while (cursor.hasNext())
            {
                var doc = cursor.next();

                //----------- Mapping of Document to Java Object --------------------------
                String projectName = doc.getString("projectName");
                String clientName = doc.getString("clientName");
                String description = doc.getString("description");

                Date startingDate = doc.getDate("startingDate");
                Date endingDate = doc.getDate("endingDate");

                int projectID = doc.getInteger("projectUniqueID");
                String repositoryLink = doc.getString("repositoryLink");
                String currentProjectStatus = doc.getString("projectCurrentStatus");

                Project projectDetails = new Project(projectName, clientName, Helper_Functions.convertToLocalDateViaInstant(startingDate), Helper_Functions.convertToLocalDateViaInstant(endingDate), description, repositoryLink);
                projectDetails.setProjectUniqueIdentifer(projectID);
                projectDetails.setProjectCurrentStatus(currentProjectStatus);

                Document teamHeadDoc =(Document) doc.get("teamHead");

                TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
                Team_Head teamHeadDetails = teamHeadDbConnectionHandler.findTeamHead(teamHeadDoc.getString("Teamhead_ID"));
                projectDetails.setProjectTeamHead(teamHeadDetails);


                //------------- All Testers Details ----------------------------------------------------------------
                ArrayList<Tester> testersTeam = new ArrayList<>();
                for (Document currentTester : (ArrayList<Document>) doc.get("testersTeam")) {
                    String testerName = currentTester.getString("name");
                    String testerCNIC = currentTester.getString("cnic");
                    String testerEmail = currentTester.getString("email");
                    String testerContact = currentTester.getString("contactNo");
                    String testerUserName = currentTester.getString("userName");

                    //------- Get All Working Projects of the Tester -----------------------------------------------
                    Tester currentTesterObject = new Tester(testerName, testerCNIC, testerContact, testerEmail, testerUserName, null);

                    Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
                    currentTesterObject.setCurrentWorkingProjects(testerDbConnectionHandler.get_All_Testers_Working_Projects(testerUserName));

                    //append in the list
                    testersTeam.add(currentTesterObject);
                }

                // Set The Testers Team of the project
                projectDetails.setTestersTeam(testersTeam);

                //------------- All Developers Details -------------------------------------------------------------
                ArrayList<Developer> developersTeam = new ArrayList<>();
                for (Document currentTester : (ArrayList<Document>) doc.get("developersTeam")) {
                    String developerName = currentTester.getString("name");
                    String developerCNIC = currentTester.getString("cnic");
                    String developerEmail = currentTester.getString("email");
                    String developerContact = currentTester.getString("contactNo");
                    String developerUserName = currentTester.getString("userName");


                    //------- Get All Working Projects of the Tester -----------------------------------------------
                    Developer currentDeveloper = new Developer(developerName, developerCNIC, developerEmail, developerContact, developerUserName, null);

                    Developer_DB_Connection_Handler dbHandlerDev = new Developer_DB_Connection_Handler();
                    currentDeveloper.setCurrentWorkingProjects(dbHandlerDev.get_All_Developers_Working_Projects(developerUserName));

                    //append in the list
                    developersTeam.add(currentDeveloper);
                }

                // Set The Developers Team of the project
                projectDetails.setDevelopersTeam(developersTeam);
                // Update the reported bugs list
                projectDetails.setList_Of_Bugs_Reported(getAllReportedBugs(projectID));

                allProjectsInDatabase.add(projectDetails);
            }
        }

        mongoClient.close();
        return allProjectsInDatabase;
    }


    // GET CLOSED PROJECT DETAILS
    public  Project getClosedProjectDetails(int projectUniqueIdentifier)
    {
        Project qureyProject = null;

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Closed_Projects");

        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("projectUniqueID", projectUniqueIdentifier);
        MongoCursor<Document> cursor = collection.find(whereQuery).iterator();


        while (cursor.hasNext()) {
            System.out.println("project founded !!!!");

            var doc = cursor.next();

            if (doc.getInteger("projectUniqueID") == projectUniqueIdentifier) {
                //----------- Mapping of Document to Java Object --------------------------
                String projectName = doc.getString("projectName");
                String clientName = doc.getString("clientName");
                String description = doc.getString("description");

                Date startingDate = doc.getDate("startingDate");
                Date endingDate = doc.getDate("endingDate");

                int projectID = doc.getInteger("projectUniqueID");
                String repositoryLink = doc.getString("repositoryLink");
                String currentProjectStatus = doc.getString("projectCurrentStatus");

                Project projectDetails = new Project(projectName, clientName, Helper_Functions.convertToLocalDateViaInstant(startingDate), Helper_Functions.convertToLocalDateViaInstant(endingDate), description, repositoryLink);
                projectDetails.setProjectUniqueIdentifer(projectID);
                projectDetails.setProjectCurrentStatus(currentProjectStatus);

                Document teamHeadDoc =(Document) doc.get("teamHead");

                TeamHead_DB_Connection_Handler teamHeadDbConnectionHandler =new TeamHead_DB_Connection_Handler();
                Team_Head teamHeadDetails = teamHeadDbConnectionHandler.findTeamHead(teamHeadDoc.getString("Teamhead_ID"));
                projectDetails.setProjectTeamHead(teamHeadDetails);

                //------------- All Testers Details ----------------------------------------------------------------
                ArrayList<Tester> testersTeam = new ArrayList<>();
                for (Document currentTester : (ArrayList<Document>) doc.get("testersTeam")) {
                    String testerName = currentTester.getString("name");
                    String testerCNIC = currentTester.getString("cnic");
                    String testerEmail = currentTester.getString("email");
                    String testerContact = currentTester.getString("contactNo");
                    String testerUserName = currentTester.getString("userName");

                    //------- Get All Working Projects of the Tester -----------------------------------------------
                    Tester currentTesterObject = new Tester(testerName, testerCNIC, testerContact, testerEmail, testerUserName, null);

                    Tester_DB_Connection_Handler testerDbConnectionHandler =new Tester_DB_Connection_Handler();
                    currentTesterObject.setCurrentWorkingProjects(testerDbConnectionHandler.get_All_Testers_Working_Projects(testerUserName));


                    //append in the list
                    testersTeam.add(currentTesterObject);
                }

                // Set The Testers Team of the project
                projectDetails.setTestersTeam(testersTeam);

                //------------- All Developers Details -------------------------------------------------------------
                ArrayList<Developer> developersTeam = new ArrayList<>();
                for (Document currentTester : (ArrayList<Document>) doc.get("developersTeam")) {
                    String developerName = currentTester.getString("name");
                    String developerCNIC = currentTester.getString("cnic");
                    String developerEmail = currentTester.getString("email");
                    String developerContact = currentTester.getString("contactNo");
                    String developerUserName = currentTester.getString("userName");


                    //------- Get All Working Projects of the Tester -----------------------------------------------
                    Developer currentDeveloper = new Developer(developerName, developerCNIC, developerEmail, developerContact, developerUserName, null);

                    Developer_DB_Connection_Handler dbHandlerDev = new Developer_DB_Connection_Handler();
                    currentDeveloper.setCurrentWorkingProjects(dbHandlerDev.get_All_Developers_Working_Projects(developerUserName));

                    //append in the list
                    developersTeam.add(currentDeveloper);
                }

                // Set The Developers Team of the project
                projectDetails.setDevelopersTeam(developersTeam);

                qureyProject = projectDetails;
            }

            // Update the reported bugs list

            qureyProject.setList_Of_Bugs_Reported(getAllReportedBugs(projectUniqueIdentifier));
        }
        return qureyProject;
    }
}

