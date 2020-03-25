package com.example.restoapp;

public class ProductModel {
    private String name, photo, created_by, created_at, changed_by, changed_at, is_delet;
    private int id, category, price, discount;

    public ProductModel(String name, String photo, String created_by, String created_at, String changed_by, String changed_at, String is_delet, int id, int category, int price, int discount) {
        this.name = name;
        this.photo = photo;
        this.created_by = created_by;
        this.created_at = created_at;
        this.changed_by = changed_by;
        this.changed_at = changed_at;
        this.is_delet = is_delet;
        this.id = id;
        this.category = category;
        this.price = price;
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getIs_delet() {
        return is_delet;
    }

    public void setIs_delet(String is_delet) {
        this.is_delet = is_delet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
