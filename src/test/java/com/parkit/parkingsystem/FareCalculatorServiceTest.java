package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
/*
 * This class performs unit tests   under various scenarios  
 *  to assess the proper functioning of 
 *  the methods in FareCalculatorService
 *  under various scenarios
 */
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    //Create an instance of FareCalculatorService
    @BeforeAll //before running all the tests
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    //Create an instance of tiket
    @BeforeEach //Before of each test
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test //represents an individual test for a specific case

    //Test in the case of a car that parks for one hour
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        //AssertEquals verifies that the first value is equal to the second
        //the first value is manually set 
        //he second value is part of the already established code
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    //Case of a motorcycle that parks for one hour
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    //Case of an unknown parking type that parks for one hour
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    //Test for a future entry time of one hour for a motorcycle
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    //est for a motorcycle when it parks for less than an hour but more than 30 minutes
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        //45 minutes*60 seconds*1000 miliseconds
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        //indicate that is a BIKE
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        //Sets the values of the ticket (entry time, exit time, type of vehicle)
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Calculate the price of that ticket
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
        //The 0.75 comes from 45 minutes divided / by 60 (one hour), and it is multiplied by the BIKE price
    }

    @Test
    //Test for a car when it parks for less than an hour but more than 30 minutes.
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        //45 minutes*60 seconds*1000 milliseconds
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        //indicate that is a CAR
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        //Sets the values of the ticket (entry time, exit time, type of vehicle)
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //Calculate the price of that ticket
        fareCalculatorService.calculateFare(ticket);

        //The 0.75 comes from 45 minutes divided / by 60 (one hour), and it is multiplied by the CAR price
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }


    @Test
    //Test for a motorcycle when it parks for 30 minutes
    public void calculateFareBikeWith30ParkingTime(){

        //The process is similar to the previous one
        Date inTime = new Date();

        //Test witch 30 minutes
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);//Set the entry time in the ticket
        ticket.setOutTime(outTime);//Set the exit time in the ticket
        ticket.setParkingSpot(parkingSpot);//Set the parking spot in the ticket
        fareCalculatorService.calculateFare(ticket);//call the method to calculate the fare
        //The value of the ticket is calculated with the assigned values (setInTime, setOutTime, setParkingSpot)
        assertEquals((0.0 ), ticket.getPrice() );//Expecting the method to calculate the fare
        //The 0.0 comes because when it's 30 minutes, the price is 0
        //and with the assert, it is comparing that 0.0 is equal to the price of the created ticket."
    }

    
    @Test
    //Test for a car when it parks for 30 minutes
    public void calculateFareCarWith30ParkingTime(){
        //The same as the previous method (calculateFareBikeWith30ParkingTime) 
        //but with Car
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.0 ) , ticket.getPrice());//was set to 0.0
    }

    @Test
    //Test for a car when it parks for one day
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
        ////24 because there are 24 hours in a day multiplied by the car price or rate"
    }
}