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

    public Bill(String room,String time){
        this.bill = bill;
        this.room = room;
        this.waterBill = waterBill;
        this.electricityBill = electricityBill;
        this.time = time;
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
