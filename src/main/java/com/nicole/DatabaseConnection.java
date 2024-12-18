package com.nicole;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private String url = "jdbc:mysql://localhost:3306/infoman_db";
    private String user = "nixzia";
    private String password = "2725";
    private Connection connection;

    public DatabaseConnection(){
        try{
            connection = DriverManager.getConnection(url,user,password);
            System.out.println("Connected Successfully");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        return connection;
    }
}
