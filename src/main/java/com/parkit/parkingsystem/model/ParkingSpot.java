package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * The type Parking spot.
 */
public class ParkingSpot {
    /**
     * The Number.
     */
    private final int number;
    /**
     * The Parking type.
     */
    private final ParkingType parkingType;
    /**
     * The Is available.
     */
    private boolean isAvailable;

    /**
     * Instantiates a new Parking spot.
     *
     * @param number      the number
     * @param parkingType the parking type
     * @param isAvailable the is available
     */
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return number;
    }

    /**
     * Gets parking type.
     *
     * @return the parking type
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * Is available boolean.
     *
     * @return the boolean
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets available.
     *
     * @param available the available
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
