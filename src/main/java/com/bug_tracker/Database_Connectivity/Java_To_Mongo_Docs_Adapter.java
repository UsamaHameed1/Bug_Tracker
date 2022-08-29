package com.bug_tracker.Database_Connectivity;

import com.bug_tracker.Business_Logic.*;
import org.bson.Document;

import java.util.ArrayList;

public interface Java_To_Mongo_Docs_Adapter {

    // ----------------------------Convert Admin Java Object To Admin Mongo Object -------------------------------------
    public  Document convertAdmin(Admin object);

    // --------------------------- Convert Developer Java Object To Mongo Object ---------------------------------------
    public Document convertDeveloper(Developer object);

    // -------------------------- Convert Java Object To Mongo Object --------------------------------------------------
    public Document convertTester(Tester object);

    // ----------------------- Convert Java Object To Mongo Object -----------------------------------------------------
    public  Document convertProject(Project object);


    // --------------------------- Convert Java Object To Mongo Object -------------------------------------------------
    public  Document convertTeamHead(Team_Head object);


    // ----------------------------- Convert Testers Object According to the Project Requirements ----------------------
    public  Document convertTesterAccordingToProject(Tester object);

    // ----------------------------- Convert Developer Object According to the Project Requirements ----------------------
    public   Document convertDeveloperAccordingToProject(Developer object);
    public  Document convertBugToDocument(Bug bugObject);
}
