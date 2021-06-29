package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * The type Ticket.
 */
public class Ticket {
    /**
     * The Id.
     */
    private int id;
    /**
     * The Parking spot.
     */
    private ParkingSpot parkingSpot;
    /**
     * The Vehicle reg number.
     */
    private String vehicleRegNumber;
    /**
     * The Price.
     */
    private double price;
    /**
     * The In time.
     */
    private LocalDateTime inTime;
    /**
     * The Out time.
     */
    private LocalDateTime outTime;


    /**
     * The Recurring customer.
     */
    private boolean recurringCustomer;

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets parking spot.
     *
     * @return the parking spot
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Sets parking spot.
     *
     * @param parkingSpot the parking spot
     */
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * Gets vehicle reg number.
     *
     * @return the vehicle reg number
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Sets vehicle reg number.
     *
     * @param vehicleRegNumber the vehicle reg number
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets in time.
     *
     * @return the in time
     */
    public LocalDateTime getInTime() {
        return inTime;
    }

    /**
     * Sets in time.
     *
     * @param inTime the in time
     */
    public void setInTime(LocalDateTime inTime) {
        this.inTime = inTime;
    }

    /**
     * Gets out time.
     *
     * @return the out time
     */
    public LocalDateTime getOutTime() {
        return outTime;
    }

    /**
     * Sets out time.
     *
     * @param outTime the out time
     */
    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }

    /**
     * Gets recurring customer.
     *
     * @return the recurring customer
     */
    public boolean getRecurringCustomer() {
        return this.recurringCustomer;
    }

    /**
     * Sets recurring customer.
     *
     * @param recurringCustomer the recurring customer
     */
    public void setRecurringCustomer(boolean recurringCustomer) {
        this.recurringCustomer = recurringCustomer;
    }
}
