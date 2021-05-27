package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodCRUD{
    private final static Connection connection = MySqlConn.getInstance().getConnection();
    private FoodCRUD(){}

    public static List<List<String>> getFoods(){
        String sql = "SELECT * FROM food";
        try{
            Statement statement = connection.createStatement();
            ResultSet foods = statement.executeQuery(sql);
            List<List<String>> result = new ArrayList<>();
            while (foods.next()){
                List <String> food = new ArrayList<>();
                String restName = foods.getString(2);
                String foodName = foods.getString(3);
                String desc = foods.getString(4);
                String price = foods.getString(5);
                String quantity = foods.getString(6);
                String ingredients = foods.getString(7);

                food.add(restName);
                food.add(foodName);
                food.add(desc);
                food.add(price);
                food.add(quantity);
                food.add(ingredients);

                result.add(food);
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<List<String>> getBeverages(){
        String sql = "SELECT * FROM beverage";
        try{
            Statement statement = connection.createStatement();
            ResultSet beverages = statement.executeQuery(sql);
            List<List<String>> result = new ArrayList<>();
            while (beverages.next()){
                List <String> beverage = new ArrayList<>();
                String restName = beverages.getString(2);
                String beverageName = beverages.getString(3);
                String desc = beverages.getString(4);
                String price = beverages.getString(5);
                String quantity = beverages.getString(6);

                beverage.add(restName);
                beverage.add(beverageName);
                beverage.add(desc);
                beverage.add(price);
                beverage.add(quantity);

                result.add(beverage);
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void addFood(String restName, String prodName, String prodDesc, int price, int g, String ing){
        try {
            String sql = "INSERT INTO food (restaurant_name, food_name, description, price, quantity, ingredients) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, restName);
            statement.setString(2, prodName);
            statement.setString(3, prodDesc);
            statement.setInt(4, price);
            statement.setInt(5, g);
            statement.setString(6, ing);
            int rows = statement.executeUpdate();
            if(rows > 0){
                System.out.println("Food added succesfully.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addBeverage(String restName, String prodName, String prodDesc, int price, int ml){
        try {
            String sql = "INSERT INTO beverage (restaurant_name, beverage_name, description, price, quantity) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, restName);
            statement.setString(2, prodName);
            statement.setString(3, prodDesc);
            statement.setInt(4, price);
            statement.setInt(5, ml);
            int rows = statement.executeUpdate();
            if(rows > 0){
                System.out.println("Beverage added succesfully.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void updatePrices(String restName) {
        try {
            String sql = "UPDATE food SET price = price * 1.05 WHERE restaurant_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, restName);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Food prices updated.");
            }
            String sql1 = "UPDATE beverage SET price = price * 1.05 WHERE restaurant_name = ?";
            PreparedStatement statement1 = connection.prepareStatement(sql);
            statement.setString(1, restName);
            int rows1 = statement.executeUpdate();
            if (rows1 > 0){
                System.out.println("Beverages prices updated.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteFood(String type, String restName, String foodName){
        if (!(type.equals("food") || type.equals("beverage"))){
            System.out.println("Inccorect type - should be food or beverage");
            return;
        }
        try {
            String sql = "DELETE FROM " + type +" WHERE " + type +"_name = ? and restaurant_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, foodName);
            statement.setString(2, restName);
            int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Food deleted");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
