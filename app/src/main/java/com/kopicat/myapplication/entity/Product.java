package com.kopicat.myapplication.entity;

public class Product implements Comparable<Product> {
    public final Integer id;
    public final String name;
    public Double opening;
    public Double balance;


    public Product(int id, String name, Double opening, Double balance) {
        this.id = id;
        this.name = name;
        this.opening = opening;
        this.balance = balance;
    }

    public Double getTotal() {
        return opening - balance;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.opening + ", " + this.balance + ", " + this.getTotal();
    }

    @Override
    public int compareTo(Product o) {
        return this.id.compareTo(o.id);
    }
}
