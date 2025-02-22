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
    public void execute(){
        File directory = new File(System.getProperty("user.home")+"/.EquationSolver");
        if(!directory.exists()){
            directory.mkdirs();
            // Log here saying Database created?
        }
        // Maybe a log here in an else statement saying that a database already existed
        try(Connection conn = DriverManager.getConnection(URL)){
            // connection succeded?
        }
        catch (Exception e) {
            // Failed connection
        }
    }
}
