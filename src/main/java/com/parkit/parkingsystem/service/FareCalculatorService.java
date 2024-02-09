//Cela définit le package auquel appartient la classe FareCalculatorService.
package com.parkit.parkingsystem.service;
//Importe la classe Fare depuis le package com.parkit.parkingsystem.constants. 
//Cette classe contient probablement des constantes liées aux tarifs de stationnement.
import com.parkit.parkingsystem.constants.Fare;
//Importe la classe Ticket depuis le package com.parkit.parkingsystem.model. 
//Cette classe représente probablement un billet de stationnement.
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    //Surcharge de la méthode calculateFare. Appelle la deuxième méthode calculateFare 
    //avec false pour le rabais.
    public void calculateFare(Ticket ticket) {
        calculateFare(ticket,false);}
    //La méthode calculateFare(Ticket ticket, boolean discount) prend deux arguments : 
    //un objet Ticket et une valeur booléenne discount qui indique si une réduction doit être
    // appliquée ou non. La méthode vérifie d'abord si l'heure de sortie ( outTime ) est valide et
    // après l'heure d'entrée ( inTime ). Sinon, une IllegalArgumentException est levée.
    //Débute la méthode calculateFare qui prend un objet Ticket et un booléen discount 
    //indiquant si un rabais est appliqué.
    public void calculateFare(Ticket ticket, boolean discount) {
        //Vérifie si l'heure de sortie (outTime) est valide et postérieure à l'heure d'entrée
        // (inTime). Sinon, une exception est levée.
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
        }

        // Calculate the arrival time in milliseconds
        long inTimeMillis = ticket.getInTime().getTime();

        // Calculate the exit time in milliseconds.
        long outTimeMillis = ticket.getOutTime().getTime();

        // Calculate the parking duration in minutes
        long duration = outTimeMillis - inTimeMillis;

        // Convert from milliseconds to minutes
        int durationInMinutes = (int) (duration / (60 * 1000));

        // If the time is less than or equal to 30 minutes, then the price is 0
        if (durationInMinutes <= 30) {
            ticket.setPrice(0.0);
        } else {
            // Convert the minutes to hours
            double additionalHours = durationInMinutes / 60.0;

            //Utilise une structure switch pour calculer le prix en fonction du type de véhicule.
            //pour déterminer le type de stationnement du véhicule associé à un billet (ticket).
            // En fonction du type de stationnement, il calcule le prix du stationnement 
            //en considérant la durée de stationnement (additionalHours) et le taux horaire
            // spécifique au type de véhicule. Si un rabais est appliqué (discount est vrai), 
            //le prix est réduit de 5%.
            switch (ticket.getParkingSpot().getParkingType()) {//Commence la déclaration switch qui évalue le type de stationnement associé au billet.
                case CAR:
                //Si le rabais est appliqué (discount est vrai), le prix est calculé 
                //en réduisant le taux horaire de 5%. Sinon, le prix est calculé en utilisant
                //le taux horaire standard. Le résultat est ensuite défini comme 
                //le prix du billet.
                    if(discount){
                        ticket.setPrice(additionalHours * Fare.CAR_RATE_PER_HOUR * (0.95));}
                    else{
                        ticket.setPrice(additionalHours * Fare.CAR_RATE_PER_HOUR);
                    }
                    break;
                case BIKE:
                    if(discount){
                        ticket.setPrice(additionalHours * Fare.BIKE_RATE_PER_HOUR * (0.95));}
                    else{
                        ticket.setPrice(additionalHours * Fare.BIKE_RATE_PER_HOUR);
                    }
                    break;
                //Si le type de stationnement n'est ni CAR ni BIKE, une exception de type 
                //IllegalArgumentException est lancée avec le message "Unknown Parking Type".
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
                }
            }
        }
    }






/**package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        // Verify that the exit time is valid & later that the entry time
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
        }
        //Calculate the arrival time in milliseconds
        long inTimeMillis = ticket.getInTime().getTime();//changing int for long, getHours for getTime

        //Calculate the exit time in milliseconds.
        long outTimeMillis = ticket.getOutTime().getTime();

        //Calculate the parking duration in minutes
        long duration = outTimeMillis - inTimeMillis;

        //Convert from milliseconds to minutes
        int durationInMinutes = (int) (duration / (60 * 1000));

        //If the time is less than or equal to 30
        if (durationInMinutes <= 30) {
            //then the price is 0
            ticket.setPrice(0.0);
        } else {
            //otherwise, convert the minutes to hours
            // The line `double additionalHours = durationInMinutes / 60.0;` is calculating the
            // additional hours of parking based on the duration in minutes.
            double additionalHours = durationInMinutes / 60.0;
            switch (ticket.getParkingSpot().getParkingType()) {
                //According to type of vehicle, multiply the hours by the rate
                case CAR:
                    ticket.setPrice(additionalHours * Fare.CAR_RATE_PER_HOUR);
                    break;
                case BIKE:
                    ticket.setPrice(additionalHours * Fare.BIKE_RATE_PER_HOUR);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}
**/