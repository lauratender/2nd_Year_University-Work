package model;

public abstract class Product {
    private String productName;
    private String productDesc;
    private Restaurant restaurant;
    private int price;

    public Product(String productName, String productDesc, Restaurant restaurant, int price) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.restaurant = restaurant;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return productName;
    }

    public void print() {
        System.out.print(productName + " " + productDesc + " " + price + " lei ");
    }
}
