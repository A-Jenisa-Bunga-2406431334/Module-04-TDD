package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;

import java.util.List;

public class Order {
    String id;
    List<Product> products;
    Long orderTime;
    String author;
    String status;

    // Constructor tanpa status (default WAITING_PAYMENT)
    public Order(String id, List<Product> products, Long orderTime, String author) {
        this.id = id;
        this.orderTime = orderTime;
        this.author = author;
        this.status = OrderStatus.WAITING_PAYMENT.getValue();
        
        if (products.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.products = products;
        }
    }

    // Constructor dengan status
    public Order(String id, List<Product> products, Long orderTime, String author, String status) {
        this(id, products, orderTime, author);
        this.setStatus(status);
    }

    // setStatus dengan validasi pakai Enum
    public void setStatus(String status) {
        if (OrderStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status;
    }
}