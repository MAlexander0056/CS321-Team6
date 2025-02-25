package com.group1.cs321.team6.Plugins;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

@Mojo(name="initialize-database")
public class CreateDatabaseMojo extends AbstractMojo{

    @Parameter
    static final private String URL = "jdbc:sqlite:my.db" + System.getProperty("user.home") + "/.EquationSolver/StoredEquations.db";

    @Override
    public void execute() throws MojoExecutionException{
        // Checking if db exists -> if it doesnt its created
        File directory = new File(System.getProperty("user.home")+"/.EquationSolver");
        if(!directory.exists()){
            directory.mkdirs();

            getLog().info("Database was not found and a new one was created!");
        } else getLog().info("DataBase was found!");

        try (Connection conn = DriverManager.getConnection(URL)) {
            getLog().info("Database connection established successfully.");
            // Additional database initialization code here
        } catch (SQLException e) {
            throw new MojoExecutionException("Failed to connect to the database.", e);
        }
    }
}



