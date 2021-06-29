package com.parkit.parkingsystem.UnitTests;

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

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

/**
 * The type Parking service test.
 */
@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    /**
     * The constant parkingService.
     */
    private static ParkingService parkingService;

    /**
     * The constant inputReaderUtil.
     */
    @Mock
    private static InputReaderUtil inputReaderUtil;
    /**
     * The constant parkingSpotDAO.
     */
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    /**
     * The constant ticketDAO.
     */
    @Mock
    private static TicketDAO ticketDAO;

    /**
     * Sets up per test.
     */
    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(LocalDateTime.now().minusHours(1));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    /**
     * Process exiting vehicle test.
     */
    @Test
    public void processExitingVehicleTest() {
        // GIVEN

        // WHEN
        parkingService.processExitingVehicle();

        // THEN
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }


    /**
     * Process incoming vehicle test.
     */
    @Test
    public void processIncomingVehicleTest() {
        // GIVEN
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1);
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // WHEN
        parkingService.processIncomingVehicle();


        // THEN
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

}
