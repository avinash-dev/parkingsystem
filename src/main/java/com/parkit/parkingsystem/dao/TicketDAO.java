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

/**
 * The type Ticket dao.
 */
public class TicketDAO {

    /**
     * The constant logger.
     */
    private static final Logger logger = LogManager.getLogger("TicketDAO");

    /**
     * The Data base config.
     */
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Save ticket.
     *
     * @param ticket the ticket
     */
    public void saveTicket(Ticket ticket) {
        Connection con;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET.getSQLMessage());
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (Timestamp.valueOf(ticket.getOutTime())));
            ps.execute();
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        }
    }

    /**
     * Gets ticket.
     *
     * @param vehicleRegNumber the vehicle reg number
     * @return the ticket
     */
    public Ticket getTicket(String vehicleRegNumber) {
        Connection con;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET.getSQLMessage());
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
                ticket.setOutTime(rs.getTimestamp(5).toLocalDateTime());
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        }
        return ticket;
    }

    /**
     * Update ticket boolean.
     *
     * @param ticket the ticket
     * @return the boolean
     */
    public boolean updateTicket(Ticket ticket) {
        Connection con;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET.getSQLMessage());
            assert ticket != null;
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
            ps.setInt(3, ticket.getId());
            ps.execute();
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);

        } catch (Exception ex) {
            logger.error("Error saving ticket info", ex);
            return false;
        }
        return true;
    }
}
