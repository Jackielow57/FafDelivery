package com.example.user.map;

public class AddTrackData {
    private String trackid;
    private String itemcode;
    private String driver;
    private String status;
    private String itemlocation;
    public AddTrackData(){


    }
    public AddTrackData(String TrackID,String ItemCode,String DriverName,String Status,String ItemLocation){
        this.trackid=TrackID;
        this.itemcode=ItemCode;
        this.itemlocation=ItemLocation;
        this.driver=DriverName;
        this.status=Status;
    }
    public String getTrackid(){
        return trackid;
    }
    public void setTrackid(String trackid)
    {
        this.trackid=trackid;
    }
    public String getItemcode(){
        return itemcode;
    }
    public void setItemcode(String itemcode){
        this.itemcode=itemcode;
    }
    public String getDriver(){
        return driver;
    }
    public void setDriver(String driver){
        this.driver=driver;
    }
    public String getItemlocation(){
        return itemlocation;
    }
    public void setItemlocation(String itemlocation){
        this.itemlocation=itemlocation;
    }
    public String getStatus(){
      return status;
    }
    public void setStatus(String status){
        this.status=status;
    }
}
