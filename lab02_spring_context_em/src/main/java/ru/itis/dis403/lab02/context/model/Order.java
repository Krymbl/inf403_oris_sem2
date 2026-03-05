package ru.itis.dis403.lab02.context.model;

public class Order {
    private Product product;
    private int count;
    private String client;

    public Order() {
    }

    public Order(Product product, int count, String client) {
        this.product = product;
        this.count = count;
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
