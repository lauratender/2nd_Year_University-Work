package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserCRUD {
    private final static Connection connection = MySqlConn.getInstance().getConnection();
    private UserCRUD(){}

    public static List<List<String>> getUsers(){
        String sql = "SELECT * FROM user";
        try{
            Statement statement = connection.createStatement();
            ResultSet users = statement.executeQuery(sql);
            List<List<String>> result = new ArrayList<>();
            while (users.next()){
                List <String> user = new ArrayList<>();
                String name = users.getString(2);
                String email = users.getString(3);
                String password = users.getString(4);
                String phone = users.getString(5);
                String address = users.getString(6);

                user.add(name);
                user.add(email);
                user.add(password);
                user.add(phone);
                user.add(address);

                result.add(user);
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addUser(String name, String email, String password, String phoneNumber, String userAddress){
        try {
            String sql = "INSERT INTO user (name, email, password, phone, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, phoneNumber);
            statement.setString(5, userAddress);
            int rows = statement.executeUpdate();
            if(rows > 0){
                System.out.println("Registered succesfully.");
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static void changePassword(String email, String newPassword){
        try {
            String sql = "UPDATE user SET password = ? WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newPassword);
            statement.setString(2, email);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Password was changed.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void changeAddress(String email, String newAddress){
        try {
            String sql = "UPDATE user SET address = ? WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newAddress);
            statement.setString(2, email);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Address was changed.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void changePhone(String email, String newPhone){
        try {
            String sql = "UPDATE user SET phone = ? WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newPhone);
            statement.setString(2, email);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Phone number was changed.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
