package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Duration;

public class DurationCalculatorService {

    public Duration calculateDifference_WithFreeTime(LocalDateTime inTime, LocalDateTime outTime) {

        Duration duration = new Duration();
        duration.differenceWithFreeTime(inTime, outTime);

        duration.setFree(duration.getDifferenceInTime() <= TimeUnit.MINUTES.toMillis(Fare.FREE_PARKING_TIME_IN_MINUTE));

        duration.setMinute((duration.getDifferenceInTime() / (1000 * 60)) % 60);
        duration.setHour((duration.getDifferenceInTime() / (1000 * 60 * 60)) % 24);
        duration.setDay((duration.getDifferenceInTime() / (1000 * 60 * 60 * 24)) % 365);
        duration.setMonth((long) (duration.getDifferenceInTime() / (1000 * 60 * 60 * 24 * 30.41666666)) % 30);
        duration.setYear((duration.getDifferenceInTime() / (1000L * 60 * 60 * 24 * 365)));

        return duration;
    }
}
