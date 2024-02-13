package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        inputReaderUtil = new InputReaderUtil();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
    }

    // The `@AfterAll` annotation is used in JUnit to indicate that the annotated method should be
    // executed after all test methods in the test class have been run. In this case, the `tearDown()`
    // method is called after all the test methods in the `ParkingDataBaseIT` class have been executed.
    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        // The line `Ticket ticket = ticketDAO.getTicket("ABCDEF");` is retrieving a ticket object from
        // the ticketDAO (Ticket Data Access Object) using the vehicle registration number "ABCDEF" as
        // a parameter. The retrieved ticket object is then assigned to the `ticket` variable.
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        // The line `assertNotNull(ticket);` is asserting that the `ticket` object is not null. It is
        // used to check if a ticket is actually saved in the database after a car is parked.
        assertNotNull(ticket);
        //assertEquals("ABCDEF", ticket.getVehichleRegNumber());
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    @Test
    public void testParkingLotExit() {
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        // The line `assertNotNull(ticketDAO.getTicket("ABCDEF").getPrice());` is asserting that the
        // `getPrice()` method of the ticket retrieved from the `ticketDAO` is not null. It is used to
        // check if the price of the ticket is correctly populated in the database after the vehicle
        // has exited the parking lot.
        assertNotNull(ticketDAO.getTicket("ABCDEF").getPrice());
        // The line `assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());` is asserting that the
        // `outTime` property of the ticket retrieved from the `ticketDAO` is not null. It is used to
        // check if the out time of the ticket is correctly populated in the database after the vehicle
        // has exited the parking lot.
        assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

   /**
    * The testParkingLotExitRecurringUser function tests the process of a recurring user exiting a
    * parking lot and verifies that the ticket price is updated correctly.
    */
    @Test
    public void testParkingLotExitRecurringUser() {
        // The line `ParkingService parkingService = new ParkingService(inputReaderUtil,
        // parkingSpotDAO, ticketDAO);` is creating an instance of the `ParkingService` class and
        // passing in the `inputReaderUtil`, `parkingSpotDAO`, and `ticketDAO` objects as parameters to
        // the constructor. This allows the `ParkingService` object to have access to the necessary
        // dependencies for its functionality.
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        // `parkingService.processIncomingVehicle();` is a method call that simulates the process of a
        // vehicle entering the parking lot. It is part of the integration test for parking a car.
        parkingService.processIncomingVehicle();
        // `Ticket ticket = ticketDAO.getTicket("ABCDEF");` is retrieving a ticket object from the
        // ticketDAO (Ticket Data Access Object) using the vehicle registration number "ABCDEF" as a
        // parameter. The retrieved ticket object is then assigned to the `ticket` variable.
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        // `ticket.setOutTime(new Date(System.currentTimeMillis()));` is setting the out time of the
        // ticket to the current time. It creates a new `Date` object with the current system time
        // using `System.currentTimeMillis()` and sets it as the out time of the ticket.
        ticket.setOutTime(new Date(System.currentTimeMillis()));
        // `ticketDAO.updateTicket(ticket);` is updating the ticket information in the database. It is
        // used to save any changes made to the ticket object, such as updating the out time or price.
        ticketDAO.updateTicket(ticket);
        // The line `parkingService.recurringUser("ABCDEF");` is calling a method `recurringUser()` on
        // the `parkingService` object and passing the vehicle registration number "ABCDEF" as a
        // parameter. This method is likely used to identify a recurring user and apply any special
        // pricing or discounts for them.
        parkingService.recurringUser("ABCDEF");
        // `parkingService.processExitingVehicle();` is a method call that simulates the process of a
        // vehicle exiting the parking lot. It is part of the integration test for testing the
        // functionality of exiting a parked vehicle.
        parkingService.processExitingVehicle();
        // The `assertEquals(0.95 * ticket.getPrice(), ticketDAO.getTicket("ABCDEF").getPrice())` is
        // comparing the expected ticket price for a recurring user with the actual ticket price
        // retrieved from the database. It is checking if the calculated price for a recurring user is
        // 95% of the original ticket price.
        assertEquals(0.95 * ticket.getPrice(), ticketDAO.getTicket("ABCDEF").getPrice());
    }
}


/*package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(ticket);
        //assertEquals("ABCDEF"), ticket.getVehichleRegNumber();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    @Test
    
    public void testParkingLotExit() {
		testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();
		assertNotNull(ticketDAO.getTicket("ABCDEF").getPrice());
		assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());
        //TODO: check that the fare generated and out time are populated correctly in the database
	}

    @Test

    public void testParkingLotExitRecurringUser(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        ticket.setOutTime(new Date(System.currentTimeMillis()));
        ticketDAO.updateTicket(ticket);
        parkingService.recurringUser("ABCDEF");
        parkingService.processExitingVehicle();
        assertEquals(0.95*ticket.getPrice(), ticketDAO.getTicket("ABCDEF").getPrice());
    }
}*/
