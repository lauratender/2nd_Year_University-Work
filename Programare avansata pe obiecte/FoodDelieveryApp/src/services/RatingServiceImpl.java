package services;

import model.Rating;
import model.Restaurant;
import model.User;

import java.util.*;

public class RatingServiceImpl implements RatingService {

    private static RatingServiceImpl instance = null;

    private static List<Rating> ratings = new ArrayList<>();

    private RatingServiceImpl(){ }

    public static RatingServiceImpl getInstance(){
        if (instance == null)
            instance = new RatingServiceImpl();
        return instance;
    }

    /*@Override
    public Rating getRating() {
        return null;
    }*/

    @Override
    public void seeRating(String restName){
        Restaurant r;
        HashMap<String, Restaurant> restaurants = RestaurantServiceImpl.getRestaurants();
        if(restaurants.containsKey(restName))
            r = restaurants.get(restName);
        else {
            System.out.println("Restaurantul " + restName + " nu exista.");
            return;
        }
        r.printRating();
    }

    @Override
    public void giveRating(String userEmail, String restName, int value) {
        Restaurant r;
        HashMap<String, Restaurant> restaurants = RestaurantServiceImpl.getRestaurants();
        HashMap<String, User> users = UserServiceImpl.getUsers();
        if(restaurants.containsKey(restName))
            r = restaurants.get(restName);
        else {
            System.out.println("Restaurantul " + restName + " nu exista.");
            return;
        }
        if(users.containsKey(userEmail))
            users.get(userEmail).giveRating(ratings, r, value);
        else
            System.out.println("Userul " + userEmail + " nu exista.");
    }
}
