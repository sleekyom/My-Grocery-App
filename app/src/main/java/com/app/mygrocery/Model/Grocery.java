package com.app.mygrocery.Model;

import com.google.firebase.Timestamp;

/**
 * Created by paulodichone on 4/7/17.
 */

public class Grocery {

    private String name;
    private String quantity;
    private Timestamp dateItemAdded;
    private String id;

    public Grocery() {

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Timestamp getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(Timestamp dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
