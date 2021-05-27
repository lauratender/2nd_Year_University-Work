package services;

import model.Restaurant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RestaurantServiceImpl implements RestaurantService{
    private static RestaurantServiceImpl instance = null;
    private static HashMap<String, Restaurant> restaurants;

    private RestaurantServiceImpl() {
        restaurants = new HashMap<>();
    }

    public static RestaurantServiceImpl getInstance() {
        if (instance == null)
            instance = new RestaurantServiceImpl();
        return instance;
    }

    public static int GetResturantId(String name) {
        int id = 0;
        if (restaurants.containsKey(name))
            id = restaurants.get(name).getId();
        return id;
    }

    @Override
    public void addRestaurant(String name, String address) {
        Restaurant r = new Restaurant(name, address);
        restaurants.put(name, r);
    }

    public void addRestaurants(List<List<String>> restaurantsParam) {
        int i = 0;
        while (i < restaurantsParam.size()){
            List <String> line = restaurantsParam.get(i);
            String name = line.get(0);
            String address = line.get(1);
            Restaurant r = new Restaurant(name, address);
            restaurants.put(name, r);
            i += 1;
        }
    }

    @Override
    public void addFood(String restName, String name, String desc, int pr, int quantity, List<String> ingredients) {
        Restaurant r;
        if (restaurants.containsKey(restName))
            r = restaurants.get(restName);
        else {
            System.out.println("Restaurantul " + restName + " nu exista.");
            return;
        }
        r.AddFood(name, desc, pr, quantity, ingredients);
    }

    public void addFoods(List<List <String>> foodsParam){
        int i = 0;
        while (i < foodsParam.size()){
            List <String> line = foodsParam.get(i);
            String restName = line.get(0);
            String productName = line.get(1);
            String desc = line.get(2);
            int price = Integer.parseInt(line.get(3));
            int quantity = Integer.parseInt(line.get(4));
            List <String> ingredients = Arrays.asList(line.get(5).split(";"));
            addFood(restName, productName, desc, price, quantity, ingredients);
            i += 1;
        }
    }

    @Override
    public void addBeverage(String restName, String name, String desc, int pr, int quantity){
        Restaurant r;
        if(restaurants.containsKey(restName))
            r = restaurants.get(restName);
        else {
            System.out.println("Restaurantul " + restName + " nu exista.");
            return;
        }
        r.AddBeverage(name, desc, pr, quantity);
    }

    public void addBeverages(List<List <String>> beveragesParam){
        int i = 0;
        while (i < beveragesParam.size()){
            List <String> line = beveragesParam.get(i);
            String restName = line.get(0);
            String productName = line.get(1);
            String desc = line.get(2);
            int price = Integer.parseInt(line.get(3));
            int quantity = Integer.parseInt(line.get(4));
            addBeverage(restName, productName, desc, price, quantity);
            i += 1;
        }
    }

    @Override
    public void seeProducts(){
        for (Restaurant r: restaurants.values()) {
            System.out.println("Restaurantul " + r.getName() + " contine produsele: ");
            r.printProducts();
            System.out.println("\n");
        }
    }

    @Override
    public void seeProducts(String restName){
        Restaurant r;
        if(restaurants.containsKey(restName))
            r = restaurants.get(restName);
        else {
            System.out.println("Restaurantul " + restName + " nu exista.");
            return;
        }
        r.printProducts();
    }
    public static HashMap<String, Restaurant> getRestaurants(){
        return restaurants;
    }

    public String foodToLine(String restName, String prodName, String prodDesc, int price, int g, List <String> ing){
        String ingredientsStr = String.join(";", ing);
        List<String> row = Arrays.asList(restName, prodName, prodDesc, Integer.toString(price), Integer.toString(g), ingredientsStr);
        String line = "\n" + String.join(",", row) ;
        return line;
    }

    public String beverageToLine(String restName, String prodName, String prodDesc, int price, int ml){
        List<String> row = Arrays.asList(restName, prodName, prodDesc, Integer.toString(price), Integer.toString(ml));
        String line = "\n" + String.join(",", row) ;
        return line;
    }
    public boolean findRestaurant(String nume){
        return restaurants.containsKey(nume);
    }
}
