package model;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class Order implements Comparable<Order>{
    private static int contor = 0;
    private int orderId;
    private int restaurantId;
    private String userEmail;
    private List<Product> products;
    private int cost;
    private Date date;
    private Driver driver;
    private int processingTime; //minutes
    private Date endTime;
    private final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs


    public Order(int id, String user, List <Product> p){
        orderId = ++contor;
        restaurantId = id;
        userEmail = user;
        products = p;
        cost = computeCost();
        date = new Date();
        computeProcessingTime();

        long curTimeInMs = date.getTime();
        endTime = new Date(curTimeInMs + (processingTime * ONE_MINUTE_IN_MILLIS));
    }

    private void computeProcessingTime(){
        int sz = products.size();
        int prep_time = sz * 3; // minutes
        // because we can't compute the delivery time between the restaurant and customer we use a random one
        Random rand = new Random();
        int del_time = 15 + rand.nextInt(45);
        processingTime = prep_time + del_time;
    }

    private int computeCost(){
        int res = 0;
        for(Product p: products)
            res+= p.getPrice();
        return res;
    }

    public void assignDriver(Driver d){
        driver = d;
    }

    public void PrintOrder(){
        System.out.println(new StringBuilder().append("Order Id: ").append(orderId).toString());
        System.out.println(new StringBuilder().append("Restaurant Id: ").append(restaurantId).toString());
        System.out.println(userEmail);
        System.out.println(new StringBuilder().append(cost).append(" lei").toString());
        System.out.println(new StringBuilder().append("Order placed at ").append(date).toString());
        System.out.println(new StringBuilder().append("Order delivered will be delivered at ").append(endTime).toString());
    }

    public Date getEndTime(){
        return endTime;
    }

    @Override
    public int compareTo(Order o) {
        return o.endTime.compareTo(endTime);
    }

    public Driver getDriver(){
        return driver;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
