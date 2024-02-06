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



import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private  void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }
    //!!!!!!!!!!Etape 5 Testez unitairement la classe ParkingService!!!!!!!!
    @Test
    //complétez le test de sortie d’un véhicule.
    //Simula el metodo de salida del parking service 
    //Ce test doit également mocker l’appel à la méthode getNbTicket()
    //implémentée lors de l’étape précédente.
    public void processExitingVehicleTest(){
        parkingService.processExitingVehicle();
        
        verify(ticketDAO,Mockito.times(1)).getTicket(any(String.class));//creation
        verify(ticketDAO,Mockito.times(1)).getNbTicket(any(String.class));//creation
        verify(ticketDAO,Mockito.times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));//creation
    }

    @Test
    public void testprocessIncomingVehicle(){
        ParkingSpot mockedParkingSpot = new ParkingSpot();
        when(parkingService.getNextParkingNumberIfAvailable()).thenReturn(mockedParkingSpot);

        parkingService.processIncomingVehicle();
        verify(parkingService, times(1)).getNextParkingNumberIfAvailable();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));//creation
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));//creation
        verify(ticketDAO, Mockito.times(1)).getNbTicket(any(String.class));//creation
    }

    @Test
    public void processExitingVehicleTestUnableUpdate(){
        String vehicleRegNumber = "ABC123";
        Ticket mockedTicket = new Ticket();
        mockedTicket.setVehicleRegNumber(vehicleRegNumber);
        mockedTicket.getParkingSpot().setParkingType(ParkingType.CAR);

        when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(mockedTicket);
        when(ticketDAO.updateTicket(mockedTicket)).thenReturn(false);

        parkingService.processExitingVehicle();

        verify(ticketDAO, times(1)).updateTicket(mockedTicket);
    }
    @Test
    public void testGetNextParkingNumberIfAvailable(){
    
        //afin de simuler je dois créer une variable où est choisi le type de véhicule
        //puis quand condition qui renvoie une place de parking disponible
        //puis création d'un résultat varibla qui est de type place de parking, résultat de l'appel à getNextParkingNumberIfAvailable
        //de la classe arkingService
        ParkingType vehicleType = ParkingType.CAR;
        ParkingSpot mockedParkingSpot = new ParkingSpot(1, vehicleType, true);
        when(parkingSpotDAO.getNextAvailableSlot(vehicleType)).thenReturn(1);
        ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();
        assertEquals(mockedParkingSpot, result);
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(vehicleType);
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound(){

        ParkingType vehicleType=ParkingType.CAR;
        when (parkingSpotDAO.getNextAvailableSlot(vehicleType)).thenReturn(-1);
        //ParkingSpot result=parkingService.getNextParkingNumberIfAvailable();
        parkingService.getNextParkingNumberIfAvailable();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(vehicleType);
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument(){
        ParkingType vehicleType=ParkingType.UNKNOWN;
        when (parkingSpotDAO.getNextAvailableSlot(vehicleType)).thenThrow(new IllegalArgumentException("Unknow Vehicle"));
        //ParkingSpot result=parkingService.getNextParkingNumberIfAvailable();
        parkingService.getNextParkingNumberIfAvailable();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(vehicleType);

    }
    }
