package com.example.siddhant.pricyfy;

/**
 * Created by siddhant on 28/3/17.
 */

public class Deal {
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
}
