package com.example.user.map;

public class driverDetail {
    private String driverName;
    private String driverID;
    private String driverRating;
    private String driverContact;

    public driverDetail()
    {

    }

    public driverDetail(String driverName, String driverID, String driverRating, String driverContact) {
        this.driverName = driverName;
        this.driverID = driverID;
        this.driverRating = driverRating;
        this.driverContact = driverContact;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(String driverRating) {
        this.driverRating = driverRating;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }
}
