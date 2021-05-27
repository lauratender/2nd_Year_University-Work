package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverCRUD {
    private final static Connection connection = MySqlConn.getInstance().getConnection();
    private DriverCRUD(){}

    public static List<List<String>> getDrivers(){
        String sql = "SELECT * FROM driver";
        try{
            Statement statement = connection.createStatement();
            ResultSet foods = statement.executeQuery(sql);
            List<List<String>> result = new ArrayList<>();
            while (foods.next()){
                List <String> driver = new ArrayList<>();
                String name = foods.getString(2);
                String phone = foods.getString(3);
                String salary = foods.getString(4);
                String carName = foods.getString(5);
                String carNumber = foods.getString(6);
                String email = foods.getString(7);

                driver.add(name);
                driver.add(email);
                driver.add(phone);
                driver.add(salary);
                driver.add(carName);
                driver.add(carNumber);

                result.add(driver);
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void addDriver(String name, String email, String phoneNumber, double salary, String carName, String carNumber){
        try {
            String sql = "INSERT INTO driver (driver_name, phone, salary, car_name, car_number, driver_email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, phoneNumber);
            statement.setDouble(3, salary);
            statement.setString(4, carName);
            statement.setString(5, carNumber);
            statement.setString(6, email);
            int rows = statement.executeUpdate();
            if(rows > 0){
                System.out.println("Driver added succesfully.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void updateCar(String driverName, String carName, String carNumber) {
        try {
            String sql = "UPDATE driver SET car_name = ?, car_number = ? WHERE driver_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, carName);
            statement.setString(2, carNumber);
            statement.setString(3, driverName);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Car updated.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteDriver(String driverName){
        try {
            String sql = "DELETE FROM driver WHERE driver_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, driverName);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Driver deleted");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
