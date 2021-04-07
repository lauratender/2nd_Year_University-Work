package model;

public class Beverage extends Product {
    int size;
    public Beverage(String name, String description, Restaurant restaurant, int pr, int s){
        super(name, description, restaurant, pr);
        size = s;
    }

    @Override
    public void print(){
        super.print();
        System.out.print(size + "ml\n");
    }
}
