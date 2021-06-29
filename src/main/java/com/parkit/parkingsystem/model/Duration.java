package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Duration {

    private long differenceInTime;
    private long year;
    private long month;
    private long day;
    private long hour;
    private long minute;

    private boolean free;

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public long getDifferenceInTime() {
        return differenceInTime;
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

    public void differenceWithFreeTime(LocalDateTime inTime, LocalDateTime outTime) {
        this.differenceInTime = outTime.toInstant(ZoneOffset.UTC).toEpochMilli()
                - inTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
