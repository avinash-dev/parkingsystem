package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * The type Parking service.
 */
public class ParkingService {

    /**
     * The constant logger.
     */
    private static final Logger logger = LogManager.getLogger("ParkingService");

    /**
     * The constant fareCalculatorService.
     */
    private static final FareCalculatorService fareCalculatorService = new FareCalculatorService();
    /**
     * The Data base config.
     */
    public final DataBaseConfig dataBaseConfig = new DataBaseConfig();
    /**
     * The Input reader util.
     */
    private final InputReaderUtil inputReaderUtil;
    /**
     * The Parking spot dao.
     */
    private final ParkingSpotDAO parkingSpotDAO;
    /**
     * The Ticket dao.
     */
    private final TicketDAO ticketDAO;

    /**
     * Instantiates a new Parking service.
     *
     * @param inputReaderUtil the input reader util
     * @param parkingSpotDAO  the parking spot dao
     * @param ticketDAO       the ticket dao
     */
    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    /**
     * Process incoming vehicle.
     */
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getId() > 0) {
                String vehicleRegNumber = getVehicleRegNumber();
                Ticket ticket = new Ticket();
                if (checkRecurringCustomer(vehicleRegNumber)) {
                    System.out.println("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
                }
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);//allocate this parking space and mark it's availability as false
                LocalDateTime inTime = LocalDateTime.now();
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                //ticket.setId(ticketID);
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
            }
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
        }
    }

    /**
     * Check recurring customer boolean.
     *
     * @param vehicleRegNumber the vehicle reg number
     * @return the boolean
     */
    private boolean checkRecurringCustomer(String vehicleRegNumber) {
        boolean check = false;
        try {
            Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.CHECK_IF_RECURRING_CUSTOMER.getSQLMessage());
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.next()) {
                check = true;
            }
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closeConnection(con);

        } catch (SQLException | ClassNotFoundException sqlException) {
            sqlException.printStackTrace();
        }
        return check;
    }

    /**
     * Gets vehicle reg number.
     *
     * @return the vehicle reg number
     */
    private String getVehicleRegNumber() {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * Get next parking number if available parking spot.
     *
     * @return the parking spot
     */
    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            logger.error("Error parsing user input for type of vehicle", ie);
        } catch (Exception e) {
            logger.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * Get vehicle type parking type.
     *
     * @return the parking type
     */
    private ParkingType getVehicleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /**
     * Process exiting vehicle.
     */
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            if (checkRecurringCustomer(vehicleRegNumber)) {
                ticket.setRecurringCustomer(true);
            }
            LocalDateTime outTime = LocalDateTime.now();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
            }
        }catch(Exception e){
            logger.error("Unable to process exiting vehicle",e);
        }
    }
}
