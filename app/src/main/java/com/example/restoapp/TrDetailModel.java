package com.example.restoapp;

import android.text.Editable;

public class TrDetailModel {
    private String name,description, created_by, created_at, changed_by, changed_at, photo;
    private int id, id_order, id_product, amount, price, discount;

    public TrDetailModel(String photo,String name, String description, String created_by, String created_at, String changed_by, String changed_at, int id, int id_order, int id_product, int amount, int price, int discount) {
        this.name = name;
        this.description = description;
        this.created_by = created_by;
        this.created_at = created_at;
        this.changed_by = changed_by;
        this.changed_at = changed_at;
        this.id = id;
        this.id_order = id_order;
        this.id_product = id_product;
        this.amount = amount;
        this.price = price;
        this.photo = photo;
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getChanged_by() {
        return changed_by;
    }

    public void setChanged_by(String changed_by) {
        this.changed_by = changed_by;
    }

    public String getChanged_at() {
        return changed_at;
    }

    public void setChanged_at(String changed_at) {
        this.changed_at = changed_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
