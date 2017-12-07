package com.example.ian.rentermanager2.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Ian on 2017/8/28 0028.
 */

public class Bill extends BmobObject {
    private String room;
    private double bill;
    private double waterBill;
    private double electricityBill;
    private double roomBill;
    private String month;
    public boolean isChecked;
    private String status;

    public double getRoomBill() {
        return roomBill;
    }

    public void setWaterBill(double waterBill) {
        this.waterBill = waterBill;
    }

    public void setElectricityBill(double electricityBill) {
        this.electricityBill = electricityBill;
    }

    public void setRoomBill(double roomBill) {
        this.roomBill = roomBill;
    }

    public String getStatus() {
        return status;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public double getWaterBill() {
        return waterBill;
    }

    public double getElectricityBill() {
        return electricityBill;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
