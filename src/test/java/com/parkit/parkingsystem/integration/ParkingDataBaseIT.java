package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The type Parking data base it.
 */
@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    /**
     * The constant dataBaseTestConfig.
     */
// private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static final DataBaseConfig dataBaseTestConfig = new DataBaseConfig();
    /**
     * The constant REGISTRATION_NUMBER.
     */
    private static final String REGISTRATION_NUMBER = "ABCDEF";
    /**
     * The constant parkingSpotDAO.
     */
    private static ParkingSpotDAO parkingSpotDAO;
    /**
     * The constant ticketDAO.
     */
    private static TicketDAO ticketDAO;
    /**
     * The constant dataBasePrepareService.
     */
    private static DataBasePrepareService dataBasePrepareService;
    /**
     * The constant inputReaderUtil.
     */
    @Mock
    private static InputReaderUtil inputReaderUtil;

    /**
     * Sets up.
     */
    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    /**
     * Sets up per test.
     */
    @BeforeEach
    private void setUpPerTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(REGISTRATION_NUMBER);
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     * Test parking a car.
     */
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

    /**
     * Test parking lot exit car.
     */
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


    /**
     * Test parking lot exit car with 1 hour parking.
     */
    @Test
    public void testParkingLotExit_CAR_With1HourParking() {

        // GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        Ticket bufferTicket = ticketDAO.getTicket(REGISTRATION_NUMBER);
        bufferTicket.setInTime(LocalDateTime.now().minusHours(1));
        ticketDAO.saveTicket(bufferTicket);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // WHEN
        parkingService.processExitingVehicle();

        // THEN
        Ticket actualTicket = ticketDAO.getTicket(REGISTRATION_NUMBER);

        assertThat(actualTicket.getInTime()).isCloseTo(LocalDateTime.now().minusHours(1), within(2, ChronoUnit.SECONDS));
        assertThat(actualTicket.getOutTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
        assertThat(actualTicket.getParkingSpot()).isExactlyInstanceOf(ParkingSpot.class);
        assertThat(actualTicket.getParkingSpot().getId()).isEqualTo(1);
        assertThat(actualTicket.getParkingSpot().getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(actualTicket.getVehicleRegNumber()).isEqualTo(REGISTRATION_NUMBER);
        assertThat(actualTicket.getPrice()).isCloseTo(Fare.CAR_RATE_PER_HOUR.getValue() - Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.CAR_RATE_PER_MINUTE, offset(0.25));

        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);

        verify(inputReaderUtil, times(1)).readSelection();
        verify(inputReaderUtil, times(2)).readVehicleRegistrationNumber();
    }


    /**
     * Test parking lot exit recurring customer car with 1 hour parking.
     */
    @Test
    public void testParkingLotExit_RecurringCustomer_CAR_With1HourParking() {

        // GIVEN
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();


        // WHEN
        parkingService.processIncomingVehicle();
        Ticket bufferTicket = ticketDAO.getTicket(REGISTRATION_NUMBER);
        bufferTicket.setInTime(LocalDateTime.now().minusHours(1));
        ticketDAO.saveTicket(bufferTicket);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        parkingService.processExitingVehicle();

        // THEN
        Ticket actualTicket = ticketDAO.getTicket(REGISTRATION_NUMBER);


        assertThat(actualTicket.getInTime()).isCloseTo(LocalDateTime.now().minusHours(1), within(2, ChronoUnit.SECONDS));
        assertThat(actualTicket.getOutTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
        assertThat(actualTicket.getParkingSpot()).isExactlyInstanceOf(ParkingSpot.class);
        assertThat(actualTicket.getParkingSpot().getId()).isEqualTo(1);
        assertThat(actualTicket.getParkingSpot().getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(actualTicket.getVehicleRegNumber()).isEqualTo(REGISTRATION_NUMBER);
        assertThat(actualTicket.getPrice()).isCloseTo(((Fare.CAR_RATE_PER_HOUR.getValue() - Fare.FREE_PARKING_TIME_IN_MINUTE.getValue() * Fare.CAR_RATE_PER_MINUTE) * 0.95), offset(0.25));

        assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);

        verify(inputReaderUtil, times(2)).readSelection();
        verify(inputReaderUtil, times(4)).readVehicleRegistrationNumber();
    }
}
