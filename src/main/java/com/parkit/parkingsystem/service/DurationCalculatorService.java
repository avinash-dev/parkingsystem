package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Duration;

import java.util.Date;

public class DurationCalculatorService {

    public Duration calculateDifference(Date inTime, Date outTime) {

        Duration duration = new Duration();
        duration.difference(inTime, outTime);

        duration.setSeconde((duration.getDifferenceInTime() / 1000) % 60);
        duration.setMinute((duration.getDifferenceInTime() / (1000 * 60)) % 60);
        duration.setHour((duration.getDifferenceInTime() / (1000 * 60 * 60)) % 24);
        duration.setDay((duration.getDifferenceInTime() / (1000 * 60 * 60 * 24)) % 365);
        duration.setYear((duration.getDifferenceInTime() / (1000L * 60 * 60 * 24 * 365)));

        return duration;
    }
}
