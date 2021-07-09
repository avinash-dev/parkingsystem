package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Duration;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Objects;

/**
 * The type Fare calculator service.
 */
public class FareCalculatorService {

    /**
     * Calculate fare.
     *
     * @param ticket the ticket
     */
    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + Objects.requireNonNull(ticket.getOutTime()));
        }

        DurationCalculatorService durationCalculatorService = new DurationCalculatorService();
        Duration duration = durationCalculatorService.calculateDifference_WithFreeTime(ticket.getInTime(), ticket.getOutTime());


        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (duration.isFree()) {
                    ticket.setPrice(0);
                } else if (ticket.getRecurringCustomer()) {
                    ticket.setPrice(carTicketPrice_forRecurringCustomer(duration));
                } else {
                    ticket.setPrice(carTicketPrice(duration));
                }
                break;
            }
            case BIKE: {
                if (duration.isFree()) {
                    ticket.setPrice(0);
                } else if (ticket.getRecurringCustomer()) {
                    ticket.setPrice(bikeTicketPrice_forRecurringCustomer(duration));
                } else {
                    ticket.setPrice(bikeTicketPrice(duration));
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    /**
     * Car ticket price double.
     *
     * @param duration the duration
     * @return the double
     */
    public double carTicketPrice(Duration duration) {
        return (Fare.CAR_RATE_PER_MONTH * duration.getMonth()
                + duration.getDay() * Fare.CAR_RATE_PER_DAY
                + duration.getHour() * Fare.CAR_RATE_PER_HOUR.getValue()
                + duration.getMinute() * Fare.CAR_RATE_PER_MINUTE
                - Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.CAR_RATE_PER_MINUTE);
    }

    /**
     * Car ticket price for recurring customer double.
     *
     * @param duration the duration
     * @return the double
     */
    public double carTicketPrice_forRecurringCustomer(Duration duration) {
        return carTicketPrice(duration) * (1 - Fare.RECURRING_USER_DISCOUNT.getValue());
    }

    /**
     * Bike ticket price double.
     *
     * @param duration the duration
     * @return the double
     */
    public double bikeTicketPrice(Duration duration) {
        return (duration.getMonth() * Fare.BIKE_RATE_PER_MONTH
                + duration.getDay() * Fare.BIKE_RATE_PER_DAY
                + duration.getHour() * Fare.BIKE_RATE_PER_HOUR.getValue()
                + duration.getMinute() * Fare.BIKE_RATE_PER_MINUTE
                - Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.BIKE_RATE_PER_MINUTE);
    }

    /**
     * Bike ticket price for recurring customer double.
     *
     * @param duration the duration
     * @return the double
     */
    public double bikeTicketPrice_forRecurringCustomer(Duration duration) {
        return bikeTicketPrice(duration) * (1 - Fare.RECURRING_USER_DISCOUNT.getValue());
    }
}