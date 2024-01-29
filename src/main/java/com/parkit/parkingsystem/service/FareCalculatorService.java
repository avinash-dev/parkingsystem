package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        // Verify that the exit time is valid & later that the entry time
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
        }
        //Calculate the arrival time in milliseconds
        long inTimeMillis = ticket.getInTime().getTime();//changing int for long, getHours for getTime

        //Calculate the exit time in milliseconds.
        long outTimeMillis = ticket.getOutTime().getTime();

        //Calculate the parking duration in minutes
        long duration = outTimeMillis - inTimeMillis;

        //Convert from milliseconds to minutes
        int durationInMinutes = (int) (duration / (60 * 1000));

        //If the time is less than or equal to 30
        if (durationInMinutes <= 30) {
            //then the price is 0
            ticket.setPrice(0.0);
        } else {
            //otherwise, convert the minutes to hours
            // The line `double additionalHours = durationInMinutes / 60.0;` is calculating the
            // additional hours of parking based on the duration in minutes.
            double additionalHours = durationInMinutes / 60.0;
            switch (ticket.getParkingSpot().getParkingType()) {
                //According to type of vehicle, multiply the hours by the rate
                case CAR:
                    ticket.setPrice(additionalHours * Fare.CAR_RATE_PER_HOUR);
                    break;
                case BIKE:
                    ticket.setPrice(additionalHours * Fare.BIKE_RATE_PER_HOUR);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }

}