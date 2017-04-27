package com.example.siddhant.pricyfy;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by siddhant on 22/3/17.
 */

public class Coupons{
    private int id;
    private String campaign;
    private String title;
    private String coupon_code;
    private String slug;
    private String landing_page;
    private String image_link;

    public Coupons(int id, String campaign, String title, String coupon_code, String slug, String landing_page, String image_link)
    {
        this.id = id;
        this.campaign = campaign;
        this.title = title;
        this.coupon_code = coupon_code;
        this.slug = slug;
        this.landing_page = landing_page;
        this.image_link = image_link;
    }

    public int getId()
    {
        return id;
    }

    public String getCampaign()
    {
        return campaign;
    }

    public String getTitle()
    {
        return title;
    }

    public String getCoupon_code()
    {
        return coupon_code;
    }

    public String getSlug()
    {
        return slug;
    }

    public String getLanding_page()
    {
        return landing_page;
    }

    public String getImage_link()
    {
        return image_link;
    }

}
