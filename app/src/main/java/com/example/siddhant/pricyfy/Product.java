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

    public Product(String title, int price, String image_link)
    {
        this.title=title;
        this.price=price;
        this.image_link=image_link;
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

    public String getDescription()
    {
        return description;
    }

    public int getPrice()
    {
        return price;
    }

    public double getSeller_rating() { return seller_rating;}

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
        price = in.readInt();
        image_link = in.readString();
        seller_rating = in.readDouble();
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
        dest.writeInt(price);
        dest.writeString(image_link);
        dest.writeDouble(seller_rating);
    }

}
