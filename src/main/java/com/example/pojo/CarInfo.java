package com.example.pojo;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@ToString
public class CarInfo {
    String brand;
    int yearOfMade;
    String listPrice;
    String retailPrice;
    int numberOfSold;

    public CarInfo(String brand, int yearOfMade, String listPrice, String retailPrice, int numberOfSold) {
        this.brand = brand;
        this.yearOfMade = yearOfMade;
        this.listPrice = listPrice;
        this.retailPrice = retailPrice;
        this.numberOfSold = numberOfSold;
    }

    public CarInfo(int yearOfMade) {
        this.yearOfMade = yearOfMade;
    }

    public CarInfo() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYearOfMade() {
        return yearOfMade;
    }

    public void setYearOfMade(int yearOfMade) {
        this.yearOfMade = yearOfMade;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public int getNumberOfSold() {
        return numberOfSold;
    }

    public void setNumberOfSold(int numberOfSold) {
        this.numberOfSold = numberOfSold;
    }
}
