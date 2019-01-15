package com.example.user.map;

public class AddItemToData {

    private String icNumber;
    private String itemID;
    private String itemName;
    private String itemDesp;
    private String itemStatus;

    public AddItemToData() {

    }

    public AddItemToData(String icNumber, String itemID, String itemName, String itemDesp, String itemStatus) {
        this.icNumber = icNumber;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDesp = itemDesp;
        this.itemStatus = itemStatus;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesp() {
        return itemDesp;
    }

    public void setItemDesp(String itemDesp) {
        this.itemDesp = itemDesp;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
}
