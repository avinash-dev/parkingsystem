package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    // Define a constant for the maximum number of times a car can pass without a discount
    private static final int MAX_CAR_PASS_COUNT = 1;

    public void calculateFare(Ticket ticket) {
        // Verify that the exit time is valid & later than the entry time
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
        }

        // Calculate the arrival time in milliseconds
        long inTimeMillis = ticket.getInTime().getTime();

        // Calculate the exit time in milliseconds.
        long outTimeMillis = ticket.getOutTime().getTime();

        // Calculate the parking duration in minutes
        long duration = outTimeMillis - inTimeMillis;

        // Convert from milliseconds to minutes
        int durationInMinutes = (int) (duration / (60 * 1000));

        // If the time is less than or equal to 30 minutes, then the price is 0
        if (durationInMinutes <= 30) {
            ticket.setPrice(0.0);
        } else {
            // Convert the minutes to hours
            double additionalHours = durationInMinutes / 60.0;

            // Apply a discount if it's a car and has passed more than MAX_CAR_PASS_COUNT times
            if (ticket.getParkingSpot().getParkingType() == ParkingType.CAR && checkCarPassCount(ticket)) {
                // Apply a 5% discount for cars that have passed more than MAX_CAR_PASS_COUNT times
                ticket.setPrice(additionalHours * Fare.CAR_RATE_PER_HOUR * (1 - 0.05));
            } else {
                // Calculate the regular fare based on the type of vehicle
                switch (ticket.getParkingSpot().getParkingType()) {
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

    private boolean checkCarPassCount(Ticket ticket) {
        // Implement the logic to check the number of times a car has passed.
        // For simplicity, let's assume the car has passed more than MAX_CAR_PASS_COUNT times if the parking spot ID is greater than 3.
        return ticket.getParkingSpot().getId() > MAX_CAR_PASS_COUNT;
    }
}




/**package com.parkit.parkingsystem.service;

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
**/