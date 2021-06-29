package com.parkit.parkingsystem.UnitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }


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
        assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR - (Fare.FREE_PARKING_TIME_IN_MINUTE * Fare.CAR_RATE_PER_MINUTE));
    }

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
        assertThat(ticket.getPrice()).isEqualTo(Fare.BIKE_RATE_PER_HOUR - (Fare.FREE_PARKING_TIME_IN_MINUTE * Fare.BIKE_RATE_PER_MINUTE));
    }

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
        assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.BIKE_RATE_PER_HOUR) - (Fare.FREE_PARKING_TIME_IN_MINUTE * Fare.BIKE_RATE_PER_MINUTE));
    }

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
        assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.CAR_RATE_PER_HOUR) - (Fare.FREE_PARKING_TIME_IN_MINUTE * Fare.CAR_RATE_PER_MINUTE));
    }

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
        assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_DAY - (Fare.FREE_PARKING_TIME_IN_MINUTE * Fare.CAR_RATE_PER_MINUTE));
    }

    @Test
    @Disabled
    public void calculateFareCar_ForRecurentUser() {

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
        assertThat(ticket.getPrice()).isEqualTo(
                (Fare.CAR_RATE_PER_HOUR - (Fare.FREE_PARKING_TIME_IN_MINUTE * Fare.CAR_RATE_PER_MINUTE)) * (1 - Fare.RECURRING_USER_DISCOUNT));
    }

}
