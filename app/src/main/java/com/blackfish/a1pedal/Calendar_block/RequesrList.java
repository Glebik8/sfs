package com.blackfish.a1pedal.Calendar_block;

public class RequesrList {

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public RequesrList(String date, String time, String status) {
        Date = date;
        Time = time;
        Status = status;
    }

    String Date;
    String Time;
    String Status;

}
