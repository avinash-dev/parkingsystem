package com.parkit.parkingsystem.constants;

/**
 * The enum Db constants.
 */
public enum DBConstants {

    /**
     * The Get next parking spot.
     */
    GET_NEXT_PARKING_SPOT("select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?"),
    /**
     * The Update parking spot.
     */
    UPDATE_PARKING_SPOT("update parking set available = ? where PARKING_NUMBER = ?"),
    /**
     * The Check if recurring customer.
     */
    CHECK_IF_RECURRING_CUSTOMER("SELECT test.ticket.VEHICLE_REG_NUMBER FROM test.ticket WHERE VEHICLE_REG_NUMBER = ? LIMIT 2"),
    /**
     * The Save ticket.
     */
    SAVE_TICKET("insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)"),
    /**
     * The Update ticket.
     */
    UPDATE_TICKET("update ticket set PRICE=?, OUT_TIME=? where ID=?"),
    /**
     * The Get ticket.
     */
    GET_TICKET("select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1");

    /**
     * The Sql message.
     */
    private final String SQLMessage;

    /**
     * Instantiates a new Db constants.
     *
     * @param SQLMessage the sql message
     */
    DBConstants(String SQLMessage) {
        this.SQLMessage = SQLMessage;
    }

    /**
     * Gets sql message.
     *
     * @return the sql message
     */
    public String getSQLMessage() {
        return SQLMessage;
    }
}
