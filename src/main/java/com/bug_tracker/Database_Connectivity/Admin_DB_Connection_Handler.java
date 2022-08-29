package com.bug_tracker.Database_Connectivity;

import com.bug_tracker.Business_Logic.Admin;
import com.bug_tracker.Business_Logic.Developer;
import com.bug_tracker.Business_Logic.Tester;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
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
import java.util.Iterator;

public class Admin_DB_Connection_Handler {

    // ------------------------------ Insert New Developer in to the mongo DB ------------------------------------------
    public void addNewAdminInDB(Admin object) {

//        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
//        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.

        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Admins");


        Object_To_Document_Conversions objectDOc = new Object_To_Document_Conversions();
        Document newDeveloperDoc = objectDOc.convertAdmin(object);

        //4. Insert the new Object
        collection.insertOne(newDeveloperDoc);
        System.out.println("New Admin Inserted");

        mongoClient.close();
    }

    // ------------------------------ Fetch All Admins Record ----------------------------------------------------------
    public ArrayList<Admin> fetchAllAdminsRecords() {

        ArrayList<Admin> admins = new ArrayList<>();
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");

        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Admins");

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

                Admin admin = new Admin(name, cnic, contactNo, email, userName, userPassword);
                admin.setMongoId(mongoId);
                admins.add(admin);
            }
        }
        mongoClient.close();
        return admins;
    }


    // -------------------------------------- Delete Admin Record ------------------------------------------------------
    public boolean Delete_Admin(ObjectId mongoID) {
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Admins");

        // Delete the document
        collection.deleteOne(Filters.eq("_id", mongoID));
        System.out.println("Admin Delete Successful");

        mongoClient.close();
        return true;
    }


    // ---------------------------------------- Update Admin Details ---------------------------------------------------
    public void Update_Admin_Details(Admin updatedAdminObject) {
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Admins");

        //4.Update a document
        collection.updateOne(Filters.eq("_id", updatedAdminObject.getMongoId()),
                Updates.combine(
                        Updates.set("name", updatedAdminObject.getName()),
                        Updates.set("cnic", updatedAdminObject.getCnic()),
                        Updates.set("contactNo", updatedAdminObject.getContactNo()),
                        Updates.set("email", updatedAdminObject.getEmail()),
                        Updates.set("userName", updatedAdminObject.getUserName()),
                        Updates.set("userPassword", updatedAdminObject.getUserPassword())
                )
        );
        mongoClient.close();
    }


    //------------------------------------ Find Admin Credentials ------------------------------------------------------
    public Admin findAdminCredentials(String userName, String password) {
        Admin qureyAdmin = null;
        //1. Establish Connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);


        //2. Connect to the Database
        MongoDatabase db = mongoClient.getDatabase("Bug_Tracker");


        //3. Connect to the collection
        MongoCollection<Document> collection = db.getCollection("Admins");

        //4. Search For the login username and password
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("userName", userName);
        whereQuery.put("userPassword", password);


        for (Document document : collection.find(whereQuery)) {
            System.out.println("admin founded !!!!");

            var doc = document;

            //----------- Mapping of Document to Java Object --------------------------
            ObjectId mongoId = doc.getObjectId("_id");
            String name = doc.getString("name");
            String cnic = doc.getString("cnic");
            String contactNo = doc.getString("contactNo");
            String email = doc.getString("email");
            String userName1 = doc.getString("userName");
            String userPassword = doc.getString("userPassword");

            qureyAdmin = new Admin(name, cnic, contactNo, email, userName1, userPassword);
            qureyAdmin.setMongoId(mongoId);
        }
        return qureyAdmin;
    }
}
