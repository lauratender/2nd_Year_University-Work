package services;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface OrderService {
    void Order(String restName, String emailUser, List<String> prodStr);
    void printOrdersInProgress();
    void addDriver(String name, String email, String phone, double sal, String carName, String carNumber);
    void printUserOrders(String email);
}
