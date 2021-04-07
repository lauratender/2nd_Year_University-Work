package services;

import model.Restaurant;

import java.util.List;

public interface RestaurantService {
    void addRestaurant(String name, String address);
    void addFood(String restName, String name, String desc, int pr, int quantity, List<String> ingredients);
    void addBeverage(String restName, String name, String desc, int pr, int quantity);
    void seeProducts();
    void seeProducts(String restName);
}
