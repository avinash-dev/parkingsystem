package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    //private static ParkingService parkingService;

    @Mock
    private ParkingSpot parkingSpot;

    @InjectMocks
    private ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    @Mock
    private static TicketDAO ticketDAO;

    @Mock
    private static Ticket ticket;

    

    @BeforeEach
    private  void setUpPerTest() {
        try {
            parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }
    //!!!!!!!!!!Etape 5 Testez unitairement la classe ParkingService!!!!!!!!

    /**
     * The testprocessIncomingVehicle function tests the processIncomingVehicle method in the
     * parkingService class by mocking the inputReaderUtil and parkingSpotDAO objects and verifying
     * that the saveTicket method in the ticketDAO object is called once.
     */
    @Test
    public void testprocessIncomingVehicle(){
        // `when(inputReaderUtil.readSelection()).thenReturn(1);` is setting up the mock behavior for
        // the `readSelection()` method of the `inputReaderUtil` object. It specifies that when this
        // method is called, it should return the integer value 1. This is done in order to simulate
        // the user input of selecting an option during the test execution.
        when(inputReaderUtil.readSelection()).thenReturn(1);
        // `when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);` is
        // setting up the mock behavior for the `getNextAvailableSlot` method of the `parkingSpotDAO`
        // object. It specifies that when this method is called with any `ParkingType` object as an
        // argument, it should return the integer value 1. This is done in order to simulate the
        // retrieval of the next available parking slot from the parking spot DAO during the test
        // execution.
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        // `parkingService.processIncomingVehicle();` is calling the `processIncomingVehicle()` method
        // of the `ParkingService` class. This method is responsible for handling the process of a
        // vehicle entering the parking system. It prompts the user to select a parking type, retrieves
        // the next available parking spot of that type from the parking spot DAO, creates a new ticket
        // with the vehicle registration number and parking spot, and saves the ticket in the ticket
        // DAO.
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));//creation
    }

    //!!!!!!!!Étape 5 : Testez unitairement la classe ParkingService grâce aux Mocks

    @Test 
    //complétez le test de sortie d’un véhicule.
    //Simula el metodo de salida del parking service 
    //Ce test doit également mocker l’appel à la méthode getNbTicket()
    //implémentée lors de l’étape précédente.
    // This code is setting up the mock behavior for the `inputReaderUtil`, `ticketDAO`, and `ticket`
    // objects in the `processExitingVehicleTest` method.
    public void processExitingVehicleTest() throws Exception{
        // `when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");` is setting up
        // the mock behavior for the `readVehicleRegistrationNumber()` method of the `inputReaderUtil`
        // object. It is specifying that when this method is called, it should return the string
        // "ABCDEF". This is done in order to simulate the user input of a vehicle registration number
        // during the test execution.
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        // `when(ticketDAO.getTicket(anyString())).thenReturn(ticket);` is setting up the mock behavior
        // for the `getTicket` method of the `ticketDAO` object. It specifies that when this method is
        // called with any string argument, it should return the `ticket` object. This is done in order
        // to simulate the retrieval of a ticket from the ticket DAO during the test execution.
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        // `when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);` is setting up the mock
        // behavior for the `updateTicket` method of the `ticketDAO` object. It specifies that when
        // this method is called with any `Ticket` object as an argument, it should return `true`. This
        // is done in order to simulate the successful update of a ticket in the ticket DAO during the
        // test execution.
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        // `parkingService.processExitingVehicle();` is calling the `processExitingVehicle()` method of
        // the `ParkingService` class. This method is responsible for handling the process of a vehicle
        // exiting the parking system. It prompts the user to enter the vehicle registration number,
        // retrieves the corresponding ticket from the ticket DAO, updates the ticket with the exit
        // time and calculates the parking duration and fare. Finally, it updates the ticket and
        // parking spot in the respective DAOs.
        parkingService.processExitingVehicle();
        // `ticketDAO.getNbTicket("ABCDEF");` is calling the `getNbTicket` method of the `ticketDAO`
        // object with the argument "ABCDEF". This method is used to retrieve the number of tickets
        // associated with a specific vehicle registration number. In this case, it is being called to
        // verify that the method is being called correctly during the test execution.
        ticketDAO.getNbTicket("ABCDEF");
        // `verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));` is verifying that the
        // `updateTicket` method of the `ticketDAO` object is called exactly 1 time with any `Ticket`
        // object as an argument. This is used to check if the method is being called correctly during
        // the test execution.
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));//creation
        // `verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));` is
        // verifying that the `updateParking` method of the `parkingSpotDAO` object is called exactly 1
        // time with any `ParkingSpot` object as an argument. This is used to check if the method is
        // being called correctly during the test execution.
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        // `verify(ticketDAO,Mockito.times(2)).getNbTicket(any(String.class))` is verifying that the
        // `getNbTicket` method of the `ticketDAO` object is called exactly 2 times with any string
        // argument. This is used to check if the method is being called correctly during the test
        // execution.
        verify(ticketDAO,Mockito.times(2)).getNbTicket(any(String.class));//creation

    }

    /**
     * The function tests the processExitingVehicle method in the parkingService class, specifically
     * checking if the ticketDAO's updateTicket method is called once when the ticket cannot be
     * updated.
     */
    @Test
    public void processExitingVehicleTestUnableUpdate() throws Exception{
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
        parkingService.processExitingVehicle();
        verify(ticketDAO,Mockito.times(1)).updateTicket(ticket);
    }

    /**
     * The testGetNextParkingNumberIfAvailable function tests if the getNextAvailableSlot method is
     * called once when processing an incoming vehicle.
     */
    @Test
    public void testGetNextParkingNumberIfAvailable() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO,Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

/**
 * The testGetNextParkingNumberIfAvailableParkingNumberNotFound function tests the scenario where there
 * is no available parking spot and verifies that the inputReaderUtil.readVehicleRegistrationNumber()
 * method is not called.
 */
@Test
public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() throws Exception {
    // `when(inputReaderUtil.readSelection()).thenReturn(1);` is setting up the mock behavior for the
    // `readSelection()` method of the `inputReaderUtil` object. It specifies that when this method is
    // called, it should return the integer value 1. This is done in order to simulate the user input
    // of selecting an option during the test execution.
    when(inputReaderUtil.readSelection()).thenReturn(1);
    // The line `when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);` is
    // setting up the mock behavior for the `getNextAvailableSlot` method of the `parkingSpotDAO`
    // object. It specifies that when this method is called with any `ParkingType` object as an
    // argument, it should return the integer value -1. This is done in order to simulate the scenario
    // where there is no available parking spot.
    when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);

    parkingService.processIncomingVehicle();
    // The above code is using Mockito to verify that the method `getNextAvailableSlot` of the
    // `parkingSpotDAO` object is called exactly once with any `ParkingType` argument.
    verify(parkingSpotDAO,Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    // `verify(inputReaderUtil, Mockito.times(0)).readVehicleRegistrationNumber();` is verifying that
    // the method `readVehicleRegistrationNumber()` of the `inputReaderUtil` object is not called
    // during the test execution. It is used to check that the method is not invoked when there is no
    // available parking spot, as specified in the test case.
    verify(inputReaderUtil, Mockito.times(0)).readVehicleRegistrationNumber();
}
@Test
public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){
    // The above code is using a mocking framework (such as Mockito) to mock the behavior of the
    // `readSelection()` method from the `inputReaderUtil` object. It is specifying that when the
    // `readSelection()` method is called, it should return the value 3.
    when(inputReaderUtil.readSelection()).thenReturn(3);
   // The above code is testing the behavior of the `processIncomingVehicle()` method in the
   // `parkingService` class. It verifies that the `readSelection()` method in the `inputReaderUtil`
   // class is called exactly once, and it also verifies that the `getNextAvailableSlot()` method in
   // the `parkingSpotDAO` class is never called with any `ParkingType` argument.
    parkingService.processIncomingVehicle();
    verify(inputReaderUtil, Mockito.times(1)).readSelection();
    verify(parkingSpotDAO, never()).getNextAvailableSlot(any(ParkingType.class));

}
}

