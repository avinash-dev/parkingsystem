package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Duration;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * The type Duration calculator service.
 */
public class DurationCalculatorService {

    /**
     * Calculate difference with free time duration.
     *
     * @param inTime  the in time
     * @param outTime the out time
     * @return the duration
     */
    public Duration calculateDifference_WithFreeTime(LocalDateTime inTime, LocalDateTime outTime) {

        Duration duration = new Duration();
        duration.differenceWithFreeTime(inTime, outTime);

        duration.setFree(duration.getDifferenceInTime() <= TimeUnit.MINUTES.toMillis((long) Fare.FREE_PARKING_TIME_IN_MINUTE.getValue()));

        duration.setMinute((duration.getDifferenceInTime() / (1000 * 60)) % 60);
        duration.setHour((duration.getDifferenceInTime() / (1000 * 60 * 60)) % 24);
        duration.setDay((duration.getDifferenceInTime() / (1000 * 60 * 60 * 24)) % 365);
        duration.setMonth((long) (duration.getDifferenceInTime() / (1000 * 60 * 60 * 24 * 30.41666666)) % 30);
        duration.setYear((duration.getDifferenceInTime() / (1000L * 60 * 60 * 24 * 365)));

        return duration;
    }
}
