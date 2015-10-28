package com.pischik.nikita.rentalmanager;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Class that represent list data and database table
 */

@DatabaseTable(tableName = "addresses")
public class AddressesItem {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String Street;

    @DatabaseField
    private String City;

    @DatabaseField
    private String State;

    @DatabaseField
    private String Rent;

    @DatabaseField
    private String LandLord;

    @DatabaseField
    private String DateIn;

    @DatabaseField
    private String DateOut;

    /**
     * constructor for creating list element
     */

    public AddressesItem(String street, String city, String state, String monthlyRent,
                         String landLord, String dateIn, String dateOut) {
        Street = street;
        City = city;
        State = state;
        Rent = monthlyRent;
        LandLord = landLord;
        DateIn = dateIn;
        DateOut = dateOut;
    }

    /**
     * empty constructor for creating DAO
     */

    public AddressesItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getRent() {
        return Rent;
    }

    public void setRent(String rent) {
        Rent = rent;
    }

    public String getLandLord() {
        return LandLord;
    }

    public void setLandLord(String landLord) {
        LandLord = landLord;
    }

    public String getDateIn() {
        return DateIn;
    }

    public void setDateIn(String dateIn) {
        DateIn = dateIn;
    }

    public String getDateOut() {
        return DateOut;
    }

    public void setDateOut(String dateOut) {
        DateOut = dateOut;
    }
}
