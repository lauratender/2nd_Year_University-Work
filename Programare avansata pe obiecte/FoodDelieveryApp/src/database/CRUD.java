package database;

import java.sql.*;

public class CRUD {
    private final static Connection connection = MySqlConn.getInstance().getConnection();
    private CRUD(){}

    public static void writeAction(String actionName){
        try {
            String sql = "INSERT INTO audit (action_name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, actionName);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
