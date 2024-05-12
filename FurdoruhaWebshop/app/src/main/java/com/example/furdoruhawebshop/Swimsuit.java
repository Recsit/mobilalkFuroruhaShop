package com.example.furdoruhawebshop;

public class Swimsuit {
    private String id;
    private String name;
    private int price;
    private String details;
    private int image;

    public Swimsuit() {
    }

    public Swimsuit(String name, int price, String details, int image) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.image = image;
    }


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}
    public String getDetails() {return details;}
    public void setDetails(String details) {this.details = details;}
    public int getImage() {return image;}
    public void setImage(int image) {this.image = image;}
    public String _getId() {return id;}
    public void setId(String id) {this.id = id;}
}
