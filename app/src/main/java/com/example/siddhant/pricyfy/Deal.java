package com.example.siddhant.pricyfy;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by siddhant on 28/3/17.
 */

public class Deal implements Parcelable{
    private int id;
    private String deal_name;
    private String deal_image;
    private String deal_link;


    public Deal(int id, String deal_name, String deal_image, String deal_link)
    {
        this.id=id;
        this.deal_image=deal_image;
        this.deal_link=deal_link;
        this.deal_name=deal_name;
    }

    public int getId()
    {
        return id;
    }

    public String getDeal_name()
    {
        return deal_name;
    }

    public String getDeal_image()
    {
        return deal_image;
    }

    public String getDeal_link()
    {
        return deal_link;
    }

    public Deal(Parcel in)
    {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Deal> CREATOR = new Parcelable.Creator<Deal>(){
        public Deal createFromParcel(Parcel in)
        {
            return new Deal(in);
        }

        public Deal[] newArray(int size)
        {
            return new Deal[size];
        }
    };

    public void readFromParcel(Parcel in)
    {
        id = in.readInt();
        deal_name = in.readString();
        deal_image = in.readString();
        deal_link = in.readString();
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(deal_name);
        dest.writeString(deal_image);
        dest.writeString(deal_link);
    }
}
