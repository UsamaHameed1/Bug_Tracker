package com.bug_tracker.Business_Logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Bug {
    private String bugUniqueID;
    private String authorTesterUsername;
    private String bugTitle;
    private String bugDescription;
    private LocalDate bugAssignmentDate;
    private String bugStatus;
    private String codeLineNumber="NULL";
    private String repositoryCommitHash="NULL";
    private String message;
    private String bugCategory;
    private Developer assignedDeveloper;
    private boolean bugFixFlag;

    //------ Relvant To Developer --------------------------------------------------------------------------------------
    private String describeSolution;
    private LocalDate closingDate=null;

    //------------------- CONSTRUCTOR ----------------------------------------------------------------------------------
    public Bug(String authorTesterUsername, String bugTitle, String bugDescription, LocalDate bugAssignmentDate, String bugStatus, String codeLineNumber, String repositoryCommitHash, String message, String bugCategory, Developer assignedDeveloper) {
        this.authorTesterUsername = authorTesterUsername;
        this.bugTitle = bugTitle;
        this.bugDescription = bugDescription;
        this.bugAssignmentDate = bugAssignmentDate;
        this.bugStatus = bugStatus;
        this.codeLineNumber = codeLineNumber;
        this.repositoryCommitHash = repositoryCommitHash;
        this.message = message;
        this.bugCategory = bugCategory;
        this.assignedDeveloper = assignedDeveloper;
        this.describeSolution = "NO SOLUTION RIGHT NOW AS BUG IS OPENED";
        this.bugFixFlag=false;

        //--------- Generate Unique ID --------------------------------------
        Random rand = new Random(); //instance of random class
        this.bugUniqueID ="BUG_TT-"+rand.nextInt(99999);
    }

    //---------------------------------------- GETTERS AND SETTERS -----------------------------------------------------
    public String getBugUniqueID() {
        return bugUniqueID;
    }

    public String getBugTitle() {
        return bugTitle;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public LocalDate getBugAssignmentDate() {
        return bugAssignmentDate;
    }

    public String getBugStatus() {
        return bugStatus;
    }

    public boolean isBugFixFlag() {
        return bugFixFlag;
    }

    public void setBugFixFlag(boolean bugFixFlag) {
        this.bugFixFlag = bugFixFlag;
    }

    public String getCodeLineNumber() {
        return codeLineNumber;
    }

    public String getRepositoryCommitHash() {
        return repositoryCommitHash;
    }

    public String getMessage() {
        return message;
    }

    public String getBugCategory() {
        return bugCategory;
    }

    public Developer getAssignedDeveloper() {
        return assignedDeveloper;
    }

    public String getDescribeSolution() {
        return describeSolution;
    }

    public String getAuthorTesterUsername() {
        return authorTesterUsername;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setBugUniqueID(String bugUniqueID) {
        this.bugUniqueID = bugUniqueID;
    }

    public void setAuthorTesterUsername(String authorTesterUsername) {
        this.authorTesterUsername = authorTesterUsername;
    }

    public void setBugTitle(String bugTitle) {
        this.bugTitle = bugTitle;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    public void setBugAssignmentDate(LocalDate bugAssignmentDate) {
        this.bugAssignmentDate = bugAssignmentDate;
    }

    public void setBugStatus(String bugStatus) {
        this.bugStatus = bugStatus;
    }

    public void setCodeLineNumber(String codeLineNumber) {
        this.codeLineNumber = codeLineNumber;
    }

    public void setRepositoryCommitHash(String repositoryCommitHash) {
        this.repositoryCommitHash = repositoryCommitHash;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBugCategory(String bugCategory) {
        this.bugCategory = bugCategory;
    }

    public void setAssignedDeveloper(Developer assignedDeveloper) {
        this.assignedDeveloper = assignedDeveloper;
    }

    public void setDescribeSolution(String describeSolution) {
        this.describeSolution = describeSolution;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    @Override
    public String toString() {
        return "Bug{" +
                "bugUniqueID='" + bugUniqueID + '\'' +
                ", authorTesterUsername='" + authorTesterUsername + '\'' +
                ", bugTitle='" + bugTitle + '\'' +
                ", bugDescription='" + bugDescription + '\'' +
                ", bugAssignmentDate=" + bugAssignmentDate +
                ", bugStatus='" + bugStatus + '\'' +
                ", codeLineNumber='" + codeLineNumber + '\'' +
                ", repositoryCommitHash='" + repositoryCommitHash + '\'' +
                ", message='" + message + '\'' +
                ", bugCategory='" + bugCategory + '\'' +
                ", assignedDeveloper=" + assignedDeveloper +
                ", describeSolution='" + describeSolution + '\'' +
                ", closingDate=" + closingDate +
                '}';
    }
}
