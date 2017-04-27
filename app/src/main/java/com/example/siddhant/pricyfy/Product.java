package com.example.siddhant.pricyfy;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by siddhant on 22/3/17.
 */

public class Product implements Parcelable{
    private int id;
    private String title;
    private String description;
    private int lowest_price, discount, mrp;
    private String image_link;
    private double seller_rating;
    private String features;
    private String stock;
    private String cod;
    private String delivery;
    private int shipping_charge;
    private int total_ratings;
    private String flipkart_link;
    private int seller_reviews;

    public Product(int id, String title, String description, int lowest_price, String image_link, double seller_rating, int mrp, int discount)
    {
        this.id=id;
        this.title=title;
        this.description=description;
        this.lowest_price=lowest_price;
        this.image_link=image_link;
        this.seller_rating=seller_rating;
        this.mrp=mrp;
        this.discount = discount;
    }

    public Product(int id, String title, String description, int lowest_price, String image_link, double seller_rating, int mrp, int discount, String features, String stock, String cod, int shipping_charge, String delivery, int total_ratings, String flipkart_link, int seller_reviews)
    {
        this.id=id;
        this.title=title;
        this.description=description;
        this.lowest_price=lowest_price;
        this.image_link=image_link;
        this.seller_rating=seller_rating;
        this.mrp=mrp;
        this.discount = discount;
        this.features = features;
        this.stock = stock;
        this.cod = cod;
        this.delivery = delivery;
        this.shipping_charge = shipping_charge;
        this.total_ratings = total_ratings;
        this.flipkart_link = flipkart_link;
        this.seller_reviews = seller_reviews;
    }

    public Product(int id, String title, int lowest_price, String image_link, int discount, int mrp, double seller_rating)
    {
        this.title=title;
        this.lowest_price=lowest_price;
        this.image_link=image_link;
        this.discount = discount;
        this.mrp = mrp;
        this.seller_rating = seller_rating;
        this.id=id;
    }
    public int getId()
    {
        return id;
    }

    public int getDiscount()
    {
        return discount;
    }

    public String getTitle()
    {
        return title;
    }

    public String getImageLink()
    {
        return image_link;
    }

    public String getDescription()
    {
        return description;
    }

    public int getLowestPrice()
    {
        return lowest_price;
    }

    public double getSeller_rating() { return seller_rating;}

    public int getMrp()
    {
        return mrp;
    }

    public String getFeatures()
    {
        return features;
    }

    public String getStock() {return stock;}

    public String getCod() {return cod;}

    public String getDelivery() {return delivery;}

    public int getShippingCharge() {return shipping_charge;}

    public int getTotal_ratings() {return total_ratings;}

    public String getFlipkart_link() {return flipkart_link;}

    public int getSeller_reviews() {return seller_reviews;}

    public Product(Parcel in)
    {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>(){
        public Product createFromParcel(Parcel in)
        {
            return new Product(in);
        }

        public Product[] newArray(int size)
        {
            return new Product[size];
        }
    };

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        lowest_price = in.readInt();
        mrp = in.readInt();
        image_link = in.readString();
        seller_rating = in.readDouble();
        discount = in.readInt();
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(lowest_price);
        dest.writeInt(mrp);
        dest.writeString(image_link);
        dest.writeDouble(seller_rating);
        dest.writeInt(discount);
    }

}
