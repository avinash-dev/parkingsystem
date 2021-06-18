package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Duration;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Objects;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + Objects.requireNonNull(ticket.getOutTime()).toString());
        }
        // False, we need all the intel : date and time exactly
//        int inHour = ticket.getInTime().getHours();
//        int outHour = ticket.getOutTime().getHours();
//        //TODO: Some tests are failing here. Need to check if this logic is correct
//        int duration = outHour - inHour;


        DurationCalculatorService durationCalculatorService = new DurationCalculatorService();
        Duration duration = durationCalculatorService.calculateDifference(ticket.getInTime(), ticket.getOutTime());


        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                double price = duration.getMonth() * Fare.CAR_RATE_PER_MONTH
                        + duration.getDay() * Fare.CAR_RATE_PER_DAY
                        + duration.getHour() * Fare.CAR_RATE_PER_HOUR
                        + duration.getMinute() * Fare.CAR_RATE_PER_MINUTE;
                ticket.setPrice(price);
                break;
            }
            case BIKE: {
                double price = duration.getMonth() * Fare.BIKE_RATE_PER_MONTH
                        + duration.getDay() * Fare.BIKE_RATE_PER_DAY
                        + duration.getHour() * Fare.BIKE_RATE_PER_HOUR
                        + duration.getMinute() * Fare.BIKE_RATE_PER_MINUTE;
                ticket.setPrice(price);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}