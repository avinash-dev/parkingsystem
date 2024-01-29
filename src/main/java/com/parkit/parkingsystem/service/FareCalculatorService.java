package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTimeMillis = ticket.getInTime().getTime();
        long outTimeMillis = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration =  outTimeMillis - inTimeMillis;
        int durationInMinutes = (int) (duration / (60.0*1000.0));// convrsion a minutos
        
        if (durationInMinutes<=30) {// cuando es menor o igual a 30 minutos devuelve precio 0
            ticket.setPrice(0.0);
        } else{
            double rMinutes=durationInMinutes-30;
            double rHours=Math.ceil(rMinutes/60.0);

            switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(rHours * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(rHours * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
       
    }
}