package com.bug_tracker.Database_Connectivity;

import com.bug_tracker.Business_Logic.*;
import org.bson.Document;

import java.util.ArrayList;

public class Object_To_Document_Conversions implements Java_To_Mongo_Docs_Adapter {


    // ----------------------------Convert Admin Java Object To Admin Mongo Object -------------------------------------
    @Override
    public  Document convertAdmin(Admin object) {
        Document doc = new Document();
        // fill as per attributes of the object
        doc.append("name", object.getName());
        doc.append("cnic", object.getCnic());
        doc.append("contactNo", object.getContactNo());
        doc.append("email", object.getEmail());
        doc.append("userName", object.getUserName());
        doc.append("userPassword", object.getUserPassword());

        return doc;
    }

    // --------------------------- Convert Developer Java Object To Mongo Object ---------------------------------------
    @Override

    public  Document convertDeveloper(Developer object) {
        Document doc = new Document();
        // fill as per attributes of the object
        doc.append("name", object.getName());
        doc.append("cnic", object.getCnic());
        doc.append("contactNo", object.getContactNo());
        doc.append("email", object.getEmail());
        doc.append("userName", object.getUserName());
        doc.append("userPassword", object.getUserPassword());

        ArrayList<Document> projectsList=new ArrayList<>();
        doc.append("currentWorkingProjects",projectsList);

        return doc;
    }

    // -------------------------- Convert Java Object To Mongo Object --------------------------------------------------
    @Override

    public  Document convertTester(Tester object) {
        Document doc = new Document();
        // fill as per attributes of the object
        doc.append("name", object.getName());
        doc.append("cnic", object.getCnic());
        doc.append("contactNo", object.getContactNo());
        doc.append("email", object.getEmail());
        doc.append("userName", object.getUserName());
        doc.append("userPassword", object.getUserPassword());

        ArrayList<Document> projectsList=new ArrayList<>();
        doc.append("currentWorkingProjects",projectsList);

        return doc;
    }

    // ----------------------- Convert Java Object To Mongo Object -----------------------------------------------------
    @Override
    public  Document convertProject(Project object) {
        Document doc = new Document();

        // fill as per attributes of the object
        doc.append("projectName", object.getProjectName());
        doc.append("clientName", object.getClientName());
        doc.append("description", object.getProjectDescription());
        doc.append("startingDate", object.getProjectStartingDate());
        doc.append("endingDate", object.getProjectEndingDate());
        doc.append("projectUniqueID",object.getProjectUniqueIdentifer());
        doc.append("repositoryLink", object.getRepositoryLink());
        doc.append("projectCurrentStatus", object.getProjectCurrentStatus());

        Document teamHeadDoc =new Document();
        teamHeadDoc.append("name",object.getProjectTeamHead().getName());
        teamHeadDoc.append("MongoID",object.getProjectTeamHead().getMongoId());
        teamHeadDoc.append("Email",object.getProjectTeamHead().getEmail());
        teamHeadDoc.append("Teamhead_ID",object.getProjectTeamHead().getId());
        doc.append("teamHead",teamHeadDoc);

        //------------------------------- For Testers Team -------------------------------------------------------------
        ArrayList<Document> testersTeam=new ArrayList<>();
        for(Tester iteratorTester: object.getTestersTeam())
        {
            Document newTesterDoc =convertTesterAccordingToProject(iteratorTester);
            testersTeam.add(newTesterDoc);
        }
        doc.append("testersTeam",testersTeam);

        //------------------------------- Developers Team --------------------------------------------------------------
        ArrayList<Document> developersTeam=new ArrayList<>();
        for(Developer iteratorDeveloper: object.getDevelopersTeam())
        {
            Document newDeveloperDoc =convertDeveloperAccordingToProject(iteratorDeveloper);
            developersTeam.add(newDeveloperDoc);
        }
        doc.append("developersTeam",developersTeam);

        //----------------------------- Reported Bugs List -------------------------------------------------------------
        ArrayList<Document> reportedBugs =new ArrayList<>();
        for(Bug reportedBug : object.getList_Of_Bugs_Reported())
        {
            Document reportedBugDoc =convertBugToDocument(reportedBug);
            reportedBugs.add(reportedBugDoc);
        }
        doc.append("reportedBugs",reportedBugs);

        return doc;
    }


    // --------------------------- Convert Java Object To Mongo Object -------------------------------------------------
    @Override
    public  Document convertTeamHead(Team_Head object) {
        Document doc = new Document();
        // fill as per attributes of the object
        doc.append("Teamhead_ID",object.getId());
        doc.append("name", object.getName());
        doc.append("cnic", object.getCnic());
        doc.append("contactNo", object.getContactNo());
        doc.append("email", object.getEmail());
        doc.append("userName", object.getUserName());
        doc.append("userPassword", object.getUserPassword());

        ArrayList<Document> projectsList=new ArrayList<>();
        doc.append("currentWorkingProjects",projectsList);

        return doc;
    }


    // ----------------------------- Convert Testers Object According to the Project Requirements ----------------------
    @Override
    public   Document convertTesterAccordingToProject(Tester object)
    {
        Document doc = new Document();
        // fill as per attributes of the object required to the project
        doc.append("name", object.getName());
        doc.append("cnic", object.getCnic());
        doc.append("contactNo", object.getContactNo());
        doc.append("email", object.getEmail());
        doc.append("mongoID", object.getMongoId());
        doc.append("userName", object.getUserName());
        return doc;
    }

    // ----------------------------- Convert Developer Object According to the Project Requirements ----------------------
    @Override
    public   Document convertDeveloperAccordingToProject(Developer object)
    {
        Document doc = new Document();
        // fill as per attributes of the object required to the project
        doc.append("name", object.getName());
        doc.append("cnic", object.getCnic());
        doc.append("contactNo", object.getContactNo());
        doc.append("email", object.getEmail());
        doc.append("mongoID", object.getMongoId());
        doc.append("userName", object.getUserName());

        return doc;
    }

    @Override
    public  Document convertBugToDocument(Bug bugObject)
    {
        Document doc =new Document();
        doc.append("bugUniqueID",bugObject.getBugUniqueID());
        doc.append("testAuthorUsername",bugObject.getAuthorTesterUsername());
        doc.append("bugTitle",bugObject.getBugTitle());
        doc.append("bugDescription",bugObject.getBugDescription());
        doc.append("bugAssignmentDate",bugObject.getBugAssignmentDate());
        doc.append("bugStatus",bugObject.getBugStatus());
        doc.append("codeLineNumber",bugObject.getCodeLineNumber());
        doc.append("repositoryCommitHash",bugObject.getRepositoryCommitHash());
        doc.append("message",bugObject.getMessage());
        doc.append("bugCategory",bugObject.getBugCategory());
        doc.append("assignedDeveloperUsername",bugObject.getAssignedDeveloper().getUserName());
        doc.append("describeSolution",bugObject.getDescribeSolution());
        doc.append("closingDate",bugObject.getClosingDate());
        doc.append("bugFixFlag",bugObject.isBugFixFlag());

        return doc;
    }

}
