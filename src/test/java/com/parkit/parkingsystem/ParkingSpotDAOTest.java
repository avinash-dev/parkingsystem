package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();//variable statica para poder hacer el test
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpotDAO parkingSpotDAO;


    @BeforeAll//que se ejecute una vez antes de que haga todas las pruebas
    public  static void setUp() {
        ticketDAO = new TicketDAO();
        dataBasePrepareService = new DataBasePrepareService();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig=dataBaseTestConfig;



    }

    @BeforeEach //se ejecuta cada vez que hace la prueba individual
    public void setUpPerTest(){
        dataBasePrepareService.clearDataBaseEntries();


    }
    @Test
    public void getNextAvailableSlotTest()
    {
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }
    
    @Test
    public void updateParkingTest(){
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);

        assertEquals(2,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }
    

    @AfterAll
    public static void clean(){
        dataBasePrepareService.clearDataBaseEntries();
    }
}
