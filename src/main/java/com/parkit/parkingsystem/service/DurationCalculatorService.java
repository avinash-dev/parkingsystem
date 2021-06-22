package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Duration;

import java.time.LocalDateTime;

public class DurationCalculatorService {

    public Duration calculateDifference(LocalDateTime inTime, LocalDateTime outTime) {

        Duration duration = new Duration();
        duration.difference(inTime, outTime);

        duration.setSeconde((duration.getDifferenceInTime() / 1000) % 60);
        duration.setMinute((duration.getDifferenceInTime() / (1000 * 60)) % 60);
        duration.setHour((duration.getDifferenceInTime() / (1000 * 60 * 60)) % 24);
        duration.setDay((duration.getDifferenceInTime() / (1000 * 60 * 60 * 24)) % 365);
        duration.setMonth((long) (duration.getDifferenceInTime() / (1000 * 60 * 60 * 24 * 30.41666666)) % 30);
        duration.setYear((duration.getDifferenceInTime() / (1000L * 60 * 60 * 24 * 365)));

        return duration;
    }
}
