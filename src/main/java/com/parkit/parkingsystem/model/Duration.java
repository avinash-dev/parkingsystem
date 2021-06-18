package com.parkit.parkingsystem.model;

import java.util.Date;

public class Duration {

    private long differenceInTime;
    private long year;
    private long month;
    private long day;
    private long hour;
    private long minute;
    private long seconde;

    public long difference(Date inTime, Date outTime) {
        this.differenceInTime = outTime.getTime() - inTime.getTime();
        return differenceInTime;
    }

    public long getDifferenceInTime() {
        return differenceInTime;
    }

    public void setDifferenceInTime(long differenceInTime) {
        this.differenceInTime = differenceInTime;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getMinute() {
        return minute;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public long getSeconde() {
        return seconde;
    }

    public void setSeconde(long seconde) {
        this.seconde = seconde;
    }
}
