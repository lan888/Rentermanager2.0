package com.example.ian.rentermanager2.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Ian on 2017/8/28 0028.
 */

public class Bill extends BmobObject {
    private String room;
    private String bill;
    private String waterBill;
    private String electricityBill;
    private String roomBill;
    private String month;
    public boolean isChecked;
    private String status;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getWaterBill() {
        return waterBill;
    }

    public void setWaterBill(String waterBill) {
        this.waterBill = waterBill;
    }

    public String getElectricityBill() {
        return electricityBill;
    }

    public void setElectricityBill(String electricityBill) {
        this.electricityBill = electricityBill;
    }

    public String getRoomBill() {
        return roomBill;
    }

    public void setRoomBill(String roomBill) {
        this.roomBill = roomBill;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
