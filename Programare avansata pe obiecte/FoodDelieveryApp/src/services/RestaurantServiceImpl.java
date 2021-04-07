package services;

import model.Restaurant;
import model.User;

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

    /*public void AddProduct(String name, String desc, int pr, boolean type, int quantity){
        model.Product p;
        if(!type) // beverage
            p = new model.Beverage(name, desc, this, pr, quantity);
        else // food
            p = new model.Food(name, desc, this, pr, quantity);
        products.add(p);
    }*/

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
}
