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

    @Test
    public void testprocessIncomingVehicle(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));//creation
    }

    @Test
    //complétez le test de sortie d’un véhicule.
    //Simula el metodo de salida del parking service 
    //Ce test doit également mocker l’appel à la méthode getNbTicket()
    //implémentée lors de l’étape précédente.
    public void processExitingVehicleTest() throws Exception{
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        parkingService.processExitingVehicle();
        ticketDAO.getNbTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));//creation
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,Mockito.times(2)).getNbTicket(any(String.class));//creation

    }

    @Test
    public void processExitingVehicleTestUnableUpdate() throws Exception{
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
        parkingService.processExitingVehicle();
        verify(ticketDAO,Mockito.times(1)).updateTicket(ticket);
    }

    @Test
    public void testGetNextParkingNumberIfAvailable() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO,Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

@Test
public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() throws Exception {
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);

    parkingService.processIncomingVehicle();
    verify(parkingSpotDAO,Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    verify(inputReaderUtil, Mockito.times(0)).readVehicleRegistrationNumber();
}
@Test
public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){
    when(inputReaderUtil.readSelection()).thenReturn(3);
    parkingService.processIncomingVehicle();
    verify(inputReaderUtil, Mockito.times(1)).readSelection();
    verify(parkingSpotDAO, never()).getNextAvailableSlot(any(ParkingType.class));

}
}

