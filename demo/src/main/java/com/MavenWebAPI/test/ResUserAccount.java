package com.MavenWebAPI.test;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ResUserAccount {
    private String id;
    private String useremail;
    private String username;
    private String first_name;
    private String last_name;
    private String updated_on;
    private String updated_by;
    private String password;
    
    private String rc; // Response code field
    private String rcDesc; // Response description field
    private String message; //Response message field 

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for useremail
    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    // Getter for useremail
    public String getUseremail() {
        return useremail;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for first_name
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    // Getter for first_name
    public String getFirst_name() {
        return first_name;
    }

    // Setter for last_name
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    // Getter for last_name
    public String getLast_name() {
        return last_name;
    }

    // Setter for updated_on
    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    // Getter for updated_on
    public String getUpdated_on() {
        return updated_on;
    }

    // Setter for updated_by
    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    // Getter for updated_by
    public String getUpdated_by() {
        return updated_by;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Getter and Setter for rc (Response code)
    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    // Getter and Setter for rcDesc (Response description)
    public String getRcDesc() {
        return rcDesc;
    }

    public void setRcDesc(String rcDesc) {
        this.rcDesc = rcDesc;
    }

    // Getter and Setter for message (Response message)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
}

