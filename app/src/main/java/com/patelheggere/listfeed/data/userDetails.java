package com.patelheggere.listfeed.data;

import java.io.Serializable;

/**
 * Created by Talkative Parents on 10/18/2017.
 */

public class userDetails implements Serializable {
   public userDetails()
    {

    }
    String Phone,Name,Place;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }
}
