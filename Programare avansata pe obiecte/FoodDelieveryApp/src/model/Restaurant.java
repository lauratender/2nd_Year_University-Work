package model;

import java.util.ArrayList;
import java.util.List;
public class Restaurant {
    private static int cnt = 0;
    private int restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private double rating;
    private List<Product> products;

    public Restaurant(String name, String address){
        restaurantName = name;
        restaurantAddress = address;
        restaurantId = ++cnt;
        rating = 5;
        products = new ArrayList<>();
    }
    public String getName(){
        return restaurantName;
    }

    public int getId(){
        return restaurantId;
    }
    public void PrintInfo() {
        System.out.println(restaurantId);
        System.out.println(restaurantName);
        System.out.println(restaurantAddress);
        System.out.println(rating);
    }
    public void updateRating(List<Rating> l){
        double nr = 0, sum = 0;
        for(Rating rating:l){
            if(rating.getRestaurant() == this){
                sum+=rating.getValue();
                nr++;
            }
        }
        rating = sum/nr;
    }

    /*public void AddProduct(String name, String desc, int pr, boolean type, int quantity){
        model.Product p;
        if(!type) // beverage
            p = new model.Beverage(name, desc, this, pr, quantity);
        else // food
            p = new model.Food(name, desc, this, pr, quantity);
        products.add(p);
    }*/

    public void AddFood(String name, String desc, int pr, int quantity, List <String> ingredints){
        Product p;
        p = new Food(name, desc, this, pr, quantity, ingredints);
        products.add(p);
    }

    public void AddBeverage(String name, String desc, int pr, int quantity){
        Product p;
        p = new Beverage(name, desc, this, pr, quantity);
        products.add(p);
    }

    public Product takeProduct(String name){
        for(Product p:products)
            if(p.getName().equals(name))
                return p;
        return null;
    }

    public void printProducts(){
        for (Product p:products)
            p.print();
    }

    public void printRating(){
        System.out.println("Restaurantul " + restaurantName + " are ratingul " + rating);
    }
}
