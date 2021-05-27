package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantCRUD {
    private final static Connection connection = MySqlConn.getInstance().getConnection();
    private RestaurantCRUD(){}
    public static List<List<String>> getRestaurants(){
        String sql = "SELECT * FROM restaurant";
        try{
            Statement statement = connection.createStatement();
            ResultSet restaurants = statement.executeQuery(sql);
            List<List<String>> result = new ArrayList<>();
            while (restaurants.next()){
                List <String> restaurant = new ArrayList<>();
                String restName = restaurants.getString(2);
                String restAdr = restaurants.getString(3);
                restaurant.add(restName);
                restaurant.add(restAdr);
                result.add(restaurant);
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void addRestaurant(String restName, String address){
        try {
            String sql = "INSERT INTO restaurant (restaurant_name, address) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, restName);
            statement.setString(2, address);
            int rows = statement.executeUpdate();
            if(rows > 0){
                System.out.println("Restaurant added succesfully.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteRestaurant(String restName){
        try {
            String sql = "DELETE FROM restaurant WHERE restaurant_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, restName);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Restaurant deleted");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
}
