package services;

import model.*;

import java.util.*;

public class OrderServiceImpl implements OrderService{
    private static List<Driver> drivers;
    private static List <Driver> driversAvailable;
    private static PriorityQueue<Order> currentOrders;
    private static List <Order> ordersHistory;
    private static OrderServiceImpl instance = null;

    private OrderServiceImpl() {
        drivers = new ArrayList<>();
        driversAvailable = new ArrayList<>();
        currentOrders = new PriorityQueue<>();
        ordersHistory = new ArrayList<>();
    }

    public static OrderServiceImpl getInstance() {
        if (instance == null)
            instance = new OrderServiceImpl();
        return instance;
    }

    private void refreshOrders(){
        Date now = new Date();
        while(!currentOrders.isEmpty() && currentOrders.peek().getEndTime().before(now)) {
            driversAvailable.add(currentOrders.peek().getDriver());
            ordersHistory.add(currentOrders.poll());
            currentOrders.remove();
        }
    }

    @Override
    public void Order(String restName, String emailUser, List <String> prodStr){
        refreshOrders();
        if(driversAvailable.size() == 0) {
            System.out.println("No driver available at the moment, try again later.");
            return;
        }

        int id = RestaurantServiceImpl.GetResturantId(restName);
        if(id == 0){
            System.out.println("Restaurantul de la care" + emailUser + "doreste sa comande nu exista.");
            return;
        }
        List <Product> products = new ArrayList<>();
        HashMap<String, Restaurant> restaurants = RestaurantServiceImpl.getRestaurants();
        for(String s:prodStr){
            Product p = restaurants.get(restName).takeProduct(s);
            if(p != null)
                products.add(p);
            else
                System.out.println("Comanda a fost anulata pentru ca produsul " + s + " nu a fost gasit la resturantul " + restName);
            return;
        }
        Order o = new Order(id, emailUser, products);
        o.assignDriver(driversAvailable.get(driversAvailable.size()-1));
        driversAvailable.remove(driversAvailable.size()-1);
        currentOrders.add(o);
        o.PrintOrder();
    }

    @Override
    public void printOrdersInProgress(){
        for (Order o:currentOrders)
            o.PrintOrder();
    }

    @Override
    public void addDriver(String name, String email, String phone, double sal, String carName, String carNumber){
        Driver d = new Driver(name, email, phone, sal, new Car(carName, carNumber));
        drivers.add(d);
        if(d.isWorkingToday() < 2)
            driversAvailable.add(d);
    }

    @Override
    public void printUserOrders(String email){
        for (Order o:ordersHistory)
            if(o.getUserEmail().equals(email))
                o.PrintOrder();
        for (Order o:currentOrders)
            if(o.getUserEmail().equals(email))
                o.PrintOrder();
    }
}
