package ru.itis.dis403.lab02.context.model;

public class ImportProduct {
    private Product product;
    private int count;
    private String supplier;

    public ImportProduct() {
    }

    public ImportProduct(Product product, int count, String supplier) {
        this.product = product;
        this.count = count;
        this.supplier = supplier;
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
