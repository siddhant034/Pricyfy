package com.example.siddhant.pricyfy;

/**
 * Created by siddhant on 22/3/17.
 */

public class Product {
    private int id;
    private String product_number;
    private String title;
    private String description;
    private int price;
    private String image_link;
    private double seller_rating;

    public Product(int id, String title, String description, int price, String image_link, double seller_rating)
    {
        this.id=id;
        this.title=title;
        this.description=description;
        this.price=price;
        this.image_link=image_link;
        this.seller_rating=seller_rating;
    }
    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getImageLink()
    {
        return image_link;
    }

    public String getProduct_number()
    {
        return product_number;
    }

    public String getDescription()
    {
        return description;
    }

    public int getPrice()
    {
        return price;
    }

    public double getSeller_rating() { return seller_rating;}
}
