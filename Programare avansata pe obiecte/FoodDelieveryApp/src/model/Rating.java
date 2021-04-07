package model;

public class Rating {
    private Restaurant restaurant;
    private User user;
    private int value;

    public Rating(Restaurant r, User u, int v){
        restaurant = r;
        user = u;
        value = v;
    }

    public Restaurant getRestaurant(){
        return restaurant;
    }
    public User getUser(){
        return user;
    }
    public void setValue(int val){
        value = val;
    }
    public int getValue(){
        return value;
    }
}
