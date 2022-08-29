module com.bug_tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;


    opens com.bug_tracker to javafx.fxml;
    opens com.bug_tracker.GUI.Admin;
    opens com.bug_tracker.GUI.Team_Head;
    opens com.bug_tracker.Business_Logic;
    opens com.bug_tracker.GUI.Tester;
    opens com.bug_tracker.GUI.Developer;
    exports com.bug_tracker;
    exports com.bug_tracker.GUI;

    opens com.bug_tracker.GUI to javafx.fxml;
}