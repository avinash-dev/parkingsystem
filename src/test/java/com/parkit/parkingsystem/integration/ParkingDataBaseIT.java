package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    // private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static final DataBaseConfig dataBaseTestConfig = new DataBaseConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    private static final String REGISTRATION_NUMBER = "ABCDEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(REGISTRATION_NUMBER);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar() {

        // GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        // WHEN
        parkingService.processIncomingVehicle();

        // THEN
        Ticket actualTicket = ticketDAO.getTicket(REGISTRATION_NUMBER);
        assertThat(actualTicket.getInTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
        assertThat(actualTicket.getOutTime()).isNull();
        assertThat(actualTicket.getParkingSpot()).isExactlyInstanceOf(ParkingSpot.class);
        assertThat(actualTicket.getParkingSpot().getId()).isEqualTo(1);
        assertThat(actualTicket.getParkingSpot().getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(actualTicket.getVehicleRegNumber()).isEqualTo(REGISTRATION_NUMBER);
        assertThat(actualTicket.getPrice()).isEqualTo(0);

        verify(inputReaderUtil, times(1)).readSelection();
        verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();

    }

    @Test
    public void testParkingLotExit_CAR() {

        // GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // WHEN
        parkingService.processExitingVehicle();

        Ticket actualTicket = ticketDAO.getTicket(REGISTRATION_NUMBER);
        assertThat(actualTicket.getInTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
        assertThat(actualTicket.getOutTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
        assertThat(actualTicket.getParkingSpot()).isExactlyInstanceOf(ParkingSpot.class);
        assertThat(actualTicket.getParkingSpot().getId()).isEqualTo(1);
        assertThat(actualTicket.getParkingSpot().getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(actualTicket.getVehicleRegNumber()).isEqualTo(REGISTRATION_NUMBER);
        assertThat(actualTicket.getPrice()).isCloseTo(0., offset(0.25));

        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);

        verify(inputReaderUtil, times(1)).readSelection();
        verify(inputReaderUtil, times(2)).readVehicleRegistrationNumber();
    }
}
