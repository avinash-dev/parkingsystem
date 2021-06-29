package com.parkit.parkingsystem.service;

import java.util.Objects;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Duration;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + Objects.requireNonNull(ticket.getOutTime()));
        }
        // False, we need all the intel : date and time exactly
//        int inHour = ticket.getInTime().getHours();
//        int outHour = ticket.getOutTime().getHours();
//        //TODO: Some tests are failing here. Need to check if this logic is correct
//        int duration = outHour - inHour;


        DurationCalculatorService durationCalculatorService = new DurationCalculatorService();
        Duration duration = durationCalculatorService.calculateDifference_WithFreeTime(ticket.getInTime(), ticket.getOutTime());


        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (duration.isFree()) {
                    ticket.setPrice(0);
                } else {
                    ticket.setPrice(carTicketPrice(duration));
                }
                break;
            }
            case BIKE: {
                if (duration.isFree()) {
                    ticket.setPrice(0);
                } else {
                    ticket.setPrice(bikeTicketPrice(duration));
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public double carTicketPrice(Duration duration) {
        return (Fare.CAR_RATE_PER_MONTH * duration.getMonth()
                + duration.getDay() * Fare.CAR_RATE_PER_DAY
                + duration.getHour() * Fare.CAR_RATE_PER_HOUR
                + duration.getMinute() * Fare.CAR_RATE_PER_MINUTE
                - Fare.FREE_PARKING_TIME_IN_MINUTE * Fare.CAR_RATE_PER_MINUTE);
    }

    public double bikeTicketPrice(Duration duration) {
        return (duration.getMonth() * Fare.BIKE_RATE_PER_MONTH
                + duration.getDay() * Fare.BIKE_RATE_PER_DAY
                + duration.getHour() * Fare.BIKE_RATE_PER_HOUR
                + duration.getMinute() * Fare.BIKE_RATE_PER_MINUTE
                - Fare.FREE_PARKING_TIME_IN_MINUTE * Fare.BIKE_RATE_PER_MINUTE);
    }
}