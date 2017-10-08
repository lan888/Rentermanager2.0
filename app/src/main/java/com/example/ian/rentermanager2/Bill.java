package com.example.ian.rentermanager2;

/**
 * Created by Ian on 2017/8/28 0028.
 */

public class Bill {
    private String room;
    private String bill;
    private String waterBill;
    private String electricityBill;
    private String time;
    private String month;

    public Bill(String room,String time,String month){
        this.room = room;
        this.time = time;
        this.month = month;
    }

    public String getRoom(){
        return  room;
    }
    public String getBill(){
        return bill;
    }
    public String getWaterBill(){
        return waterBill;
    }
    public String getElectricityBill(){
        return electricityBill;
    }
    public String getTime(){
        return time;
    }
}

