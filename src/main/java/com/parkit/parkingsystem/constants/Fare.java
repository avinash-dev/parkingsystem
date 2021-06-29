package com.parkit.parkingsystem.constants;

public class Fare {

    public static final long FREE_PARKING_TIME_IN_MINUTE = 30;

    public static final double RECURRING_USER_DISCOUNT = 0.05;

    public static final double CAR_RATE_PER_HOUR = 1.5;
    public static final double CAR_RATE_PER_DAY = 24 * CAR_RATE_PER_HOUR;
    public static final double CAR_RATE_PER_MONTH = 30 * CAR_RATE_PER_DAY;
    public static final double CAR_RATE_PER_MINUTE = CAR_RATE_PER_HOUR / 60;

//    public static final double CAR_RATE_PER_MONTH = 900;
//    public static final double CAR_RATE_PER_DAY = 30;

//    public static final double BIKE_RATE_PER_MONTH = 600;
//    public static final double BIKE_RATE_PER_DAY = 20.0;
//    public static final double BIKE_RATE_PER_HOUR = 1.0;

    public static final double BIKE_RATE_PER_HOUR = 1.0;
    public static final double BIKE_RATE_PER_DAY = 24 * BIKE_RATE_PER_HOUR;
    public static final double BIKE_RATE_PER_MONTH = 30 * BIKE_RATE_PER_DAY;
    public static final double BIKE_RATE_PER_MINUTE = BIKE_RATE_PER_HOUR / 60;


}
