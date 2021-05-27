package database;

import java.sql.*;

public class MySqlConn {
    private final String url = "jdbc:mysql://localhost:3306/proiect";
    private final String user = "root";
    private final String password = "(Sql2205)";
    private Connection connection = null;
    private static MySqlConn instance = null;

    private MySqlConn(){
        try{
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to database.");
                connection = conn;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static MySqlConn getInstance(){
        if (instance == null)
            instance = new MySqlConn();
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
