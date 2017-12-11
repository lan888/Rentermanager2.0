package com.example.ian.rentermanager2.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Ian on 2017/8/28 0028.
 */

public class Rooms extends BmobObject {
    private String status;
    private String room;
    private String type;
    private String area;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

