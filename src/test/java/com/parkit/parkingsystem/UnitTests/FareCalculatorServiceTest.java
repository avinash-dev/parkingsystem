package com.parkit.parkingsystem.UnitTests;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * The type Fare calculator service test.
 */
public class FareCalculatorServiceTest {

    /**
     * The constant fareCalculatorService.
     */
    private static FareCalculatorService fareCalculatorService;

    /**
     * Sets up.
     */
    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }


    /**
     * Calculate fare car.
     */
    @Test
    public void calculateFareCar() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusHours(1);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR.getValue() - (Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.CAR_RATE_PER_MINUTE));
    }

    /**
     * Calculate fare bike.
     */
    @Test
    public void calculateFareBike() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusHours(1);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo(Fare.BIKE_RATE_PER_HOUR.getValue() - (Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.BIKE_RATE_PER_MINUTE));
    }

    /**
     * Calculate fare unkown type.
     */
    @Test
    public void calculateFareUnkownType() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusHours(1);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        // WHEN
        ticket.setParkingSpot(parkingSpot);

        // THEN
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * Calculate fare bike with future in time.
     */
    @Test
    public void calculateFareBikeWithFutureInTime() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().plusHours(1);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        // WHEN
        ticket.setParkingSpot(parkingSpot);

        // THEN
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * Calculate fare bike with less than one hour parking time.
     */
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.BIKE_RATE_PER_HOUR.getValue()) - (Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.BIKE_RATE_PER_MINUTE));
    }

    /**
     * Calculate fare car with less than one hour parking time.
     */
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.CAR_RATE_PER_HOUR.getValue()) - (Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.CAR_RATE_PER_MINUTE));
    }

    /**
     * Calculate free fare car.
     */
    @Test
    public void calculateFreeFareCar() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(28);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo(0);
    }

    /**
     * Calculate free fare bike.
     */
    @Test
    public void calculateFreeFareBike() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(28);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo(0);
    }

    /**
     * Fare car out time in the past.
     */
    @Test
    public void FareCar_outTimeInThePast() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().minusMinutes(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        // THEN
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * Calculate fare car with more than a day parking time.
     */
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusDays(1);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_DAY - (Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.CAR_RATE_PER_MINUTE));
    }

    /**
     * Calculate fare car for recurent user for an hour parking.
     */
    @Test
    public void calculateFareCar_forRecurentUser_forAnHourParking() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusHours(1);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setRecurringCustomer(true);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo(
                (Fare.CAR_RATE_PER_HOUR.getValue() - (Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.CAR_RATE_PER_MINUTE)) * (1 - Fare.RECURRING_USER_DISCOUNT.getValue()));
    }

    /**
     * Calculate fare bike for recurent user for an hour parking.
     */
    @Test
    public void calculateFareBike_forRecurentUser_forAnHourParking() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusHours(1);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setRecurringCustomer(true);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket);

        // THEN
        assertThat(ticket.getPrice()).isEqualTo(
                (Fare.BIKE_RATE_PER_HOUR.getValue() - (Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.BIKE_RATE_PER_MINUTE)) * (1 - Fare.RECURRING_USER_DISCOUNT.getValue()));
    }

}
