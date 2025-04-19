/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;
import com.group1.cs321.team6.UserInput;

/**
 *
 * @author cates
 */

/**
 * 
 *  Responsible for establishing connectivity w/ the database
 *  Member variables: Each user preset. Sends to database
 */
public class SQLConnection {
    // URL can assigned whenever we figure that out.
    private static final String URL = null;
    private UserInput user;
    
    SQLConnection (UserInput someUser) {
        this.user = someUser;
    }
    
    /**
     * 
     * @return UserInput
     *  This method is public, as SQLExecution will need access to a UserInput instance
     *      if it is to INSERT a user.
     */
    public UserInput getUser () {
        return this.user;
    }
    
    /**
     * 
     *  Attempt to find user
     */
    private void databaseConnection () {
    
    }
    
    /**
     * 
     *  Simple getter method for connectivity state
     */
    private void getConnection() {
        
    }
   
    private void closeConnection() {
        
    }
}
