package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public boolean saveTicket(Ticket ticket){
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null)?null: (new Timestamp(ticket.getOutTime().getTime())) );
            return ps.execute();
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return false;
        }
    }

    public Ticket getTicket(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return ticket;
        }
    }

    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3,ticket.getId());
            ps.execute();
            return true;
        }catch (Exception ex){
            logger.error("Error saving ticket info",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    // MLO New method to get the number of tickets for a specific vehicle
    //This method returns an integer representing the number of tickets for a specific vehicle
    // identified by its registration number.

    public int getNbTicket(String vehicleRegNumber) {
    //local variables 
        //Connection con = null;: Declares a Connection object and initializes it to null.
        // This will be used to establish a connection to the database.
        Connection con = null;
        //int numberOfTickets = 0;: Initializes an integer variable to zero, 
        //which will store the count of tickets for the specified vehicle.
        int numberOfTickets = 0;
        //try block
        try {
        //con = dataBaseConfig.getConnection();: Attempts to establish a 
        //connection to the database using the dataBaseConfig object.
            con = dataBaseConfig.getConnection();
            // Creates a PreparedStatement object ps using the SQL query defined in DBConstants.GET_NB_TICKET
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NB_TICKET);//This query is expected to return the count of tickets for a specific vehicle.
            //Sets the parameter in the SQL query to the provided vehicleRegNumber. 
            //This ensures that the query retrieves information for the specified vehicle.
            ps.setString(1, vehicleRegNumber);
            //Executes the SQL query and retrieves the result set, 
            //which contains the count of tickets for the specified vehicle.
            ResultSet rs = ps.executeQuery();
            //Checks if the result set has at least one row (i.e., if there is data). 
            //If true, it retrieves the count from the first column of the result set and assigns it 
            //to the numberOfTickets variable.
            if (rs.next()) {
                numberOfTickets = rs.getInt(1);
            }
            //dataBaseConfig.closeResultSet(rs); and dataBaseConfig.closePreparedStatement(ps);: 
            //Closes the ResultSet and PreparedStatement to free up resources.
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching the number of tickets for a vehicle", ex);
        } finally {
            //Ensures that the database connection is closed, regardless of whether an exception occurred or not.
            // It then returns the numberOfTickets variable, which contains the count of tickets for the specified vehicle.
            dataBaseConfig.closeConnection(con);
            return numberOfTickets;
        
    }
}
}

