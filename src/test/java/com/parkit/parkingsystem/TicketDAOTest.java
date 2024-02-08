package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();//variable statica para poder hacer el test
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static ParkingSpot parkingSpot;

    @BeforeAll//que se ejecute una vez antes de que haga todas las pruebas
    public  static void setUp() {
        ticketDAO = new TicketDAO();
        dataBasePrepareService = new DataBasePrepareService();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;

    }

    @BeforeEach //se ejecuta cada vez que hace la prueba individual
    public void setUpPerTest(){
        dataBasePrepareService.clearDataBaseEntries();
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    }

    @Test

    public void saveTicketTest(){
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis()-(60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDF");

        ticketDAO.saveTicket(ticket);
        assertNotNull(ticketDAO.getTicket("ABCDF"));

       
    }
    @Test

    public void updateTicketTest(){
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis()-(60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDF");
        ticket.setOutTime(new Date(System.currentTimeMillis()));

        ticketDAO.updateTicket(ticket);
        assertNotNull(ticketDAO.updateTicket(ticket));


    }

    @Test

    public void getNbTicketTest(){
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis()-(60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDF");
        ticketDAO.saveTicket(ticket);
        Ticket ticket2 = new Ticket();
        ticket2.setInTime(new Date(System.currentTimeMillis()-(60*60*1000)));
        ticket2.setParkingSpot(parkingSpot);
        ticket2.setVehicleRegNumber("ABCDF");
        ticketDAO.saveTicket(ticket2);
        //espera dos tickets con la misma placa, si se cumple igual, quiere decir que el metodo funciona
        assertEquals(2, ticketDAO.getNbTicket("ABCDF"));
        
    }

    @AfterAll
    public static void clean(){
        dataBasePrepareService.clearDataBaseEntries();
    }
    
}
