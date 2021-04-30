package model;

public abstract class Product {
    protected String productName;
    protected String productDesc;
    protected Restaurant restaurant;
    protected int price;

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
