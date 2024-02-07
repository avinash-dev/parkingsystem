package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

/*
 * Cette classe réalise des tests unitaires sous différents scénarios
 * pour évaluer le bon fonctionnement des méthodes dans FareCalculatorService.
 */
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    // Crée une instance de FareCalculatorService
    @BeforeAll // avant d'exécuter tous les tests
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    // Crée une instance de ticket
    @BeforeEach // avant chaque test
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test // représente un test individuel pour un cas spécifique

    // Test dans le cas d'une voiture qui se gare pendant une heure
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        //Configuration du ticket :
        ticket.setInTime(inTime);//Configure l'heure d'entrée du ticket avec la valeur de inTime.
        ticket.setOutTime(outTime);//Configure l'heure de sortie du ticket avec la valeur de outTime.
        ticket.setParkingSpot(parkingSpot);//Configure l'emplacement de stationnement du ticket avec l'objet parkingSpot créé précédemment.
        fareCalculatorService.calculateFare(ticket);
        // Vérifie que la première valeur est égale à la seconde
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    // Cas d'une moto qui se gare pendant une heure
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        // Vérifie que la première valeur est égale à la seconde
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    // Cas d'un type de parking inconnu qui se gare pendant une heure
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Vérification de l'exception :
        //assertThrows() est une méthode fournie par le framework de test JUnit. Elle est utilisée pour vérifier si une exception est levée lors de l'exécution d'une certaine action.
        //Deux arguments sont attendues : la classe d'exception attendue et la méthode à tester.
        //expression lambda (() -> ...) qui représente le code à exécuter pour le test fareCalculatorService.calculateFare(ticket)
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }



    @Test
    // Test pour une heure d'entrée future pour une moto
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    // Test pour une moto lorsqu'elle se gare pour moins d'une heure mais plus de 30 minutes
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes de stationnement devraient donner 3/4 du tarif de stationnement
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    // Test pour une voiture lorsqu'elle se gare pour moins d'une heure mais plus de 30 minutes.
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes de stationnement devraient donner 3/4 du tarif de stationnement
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        fareCalculatorService.calculateFare(ticket);

        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }


    @Test
    // Test pour une moto lorsqu'elle se gare pendant 30 minutes
    public void calculateFareBikeWith30ParkingTime(){

        Date inTime = new Date();

        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.0 ), ticket.getPrice() );
    }

    
    @Test
    // Test pour une voiture lorsqu'elle se gare pendant 30 minutes
    public void calculateFareCarWith30ParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 30 * 60 * 1000) );//30 : C'est le nombre de minutes*60 : Il y a 60 secondes dans une minute, donc nous multiplions par 60 pour convertir les minutes en secondes*1000 : Il y a 1000 millisecondes dans une seconde, donc nous multiplions par 1000 pour convertir les secondes en millisecondes.
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.0 ) , ticket.getPrice());
    }

    @Test
    // Test pour une voiture lorsqu'elle se gare pour plus d'une journée
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 : C'est le nombre d'heures*60 : Il y a 60 minutes dans une heure, donc nous multiplions par 60 pour convertir les heures en minutes*60 : Il y a 60 secondes dans une minute, donc nous multiplions par 60 pour convertir les minutes en secondes*1000 : Il y a 1000 millisecondes dans une seconde, donc nous multiplions par 1000 pour convertir les secondes en millisecondes.
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    //!!!!!!!!!!ÉTAPE 4: DÉVELOPPER LA FONCTIONNALITÉ DE RÉDUCTION DE 5%!!!!!!!!!!!!

    @Test
    // Test pour vérifier qu'un véhicule avec un ticket de réduction paie 95 % du prix total.
    public void calculateFareCarWithDiscount() {//Définit une méthode publique appelée 
        //calculateFareCarWithDiscount qui sera exécutée en tant que partie de la suite de tests.
        // Crée un objet Date représentant l'heure d'entrée du véhicule.
        Date inTime = new Date();
        
        // Définit l'heure d'entrée actuelle pour simuler que le véhicule est stationné pendant 1 heure (en millisecondes).
        inTime.setTime(System.currentTimeMillis() - (1 * 60 * 60 * 1000));//Cette soustraction soustrait la durée d'une heure en millisecondes de l'heure actuelle, ce qui recule le temps d'une heure.
        // Crée un autre objet Date pour représenter l'instant actuel, initialise l'heure de sortie du ticket de stationnement en utilisant l'heure actuelle au moment de l'exécution du programme.
        Date outTime = new Date();

        // Crée un objet ParkingSpot avec l'ID 1, le type de véhicule CAR et marqué comme réduction (true).
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        // Crée un objet Ticket
        Ticket ticket = new Ticket();
        // Définit l'heure d'entrée du ticket avec la valeur de inTime.
        ticket.setInTime(inTime);
        // Définit l'heure de sortie du ticket avec la valeur de outTime.
        ticket.setOutTime(outTime);
        // Définit l'emplacement de stationnement du ticket avec l'objet parkingSpot créé précédemment.
        ticket.setParkingSpot(parkingSpot);

        // Calcule le tarif avec réduction
        // Appelle la méthode calculateFare de l'objet fareCalculatorService en passant le ticket et en spécifiant qu'il bénéficie d'une réduction (true).
        fareCalculatorService.calculateFare(ticket, true);

        // Vérifie que le prix calculé est égal à 95 % du prix total attendu
        // Utilise la méthode assertEquals de JUnit pour vérifier que le prix calculé du ticket est égal à 95 % du tarif total attendu pour une voiture par heure.
        assertEquals(0.95 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    // Test pour vérifier qu'une moto avec un ticket de réduction paie 95 % du prix total.
    public void calculateFareBikeWithDiscountFor() {
        // Date et heure actuelles
        Date inTime = new Date();
        
        // Simuler que la moto est stationnée pendant 1 heure
        inTime.setTime(System.currentTimeMillis() - (1 * 60 * 60 * 1000));
        Date outTime = new Date();

        // Crée un ticket avec un type de moto et marqué comme réduction
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // Calcule le tarif avec réduction
        fareCalculatorService.calculateFare(ticket, true);

        // Vérifie que le prix calculé est égal à 95 % du prix total attendu
        assertEquals(0.95*Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }
}



/*package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
/*
 * This class performs unit tests   under various scenarios  
 *  to assess the proper functioning of 
 *  the methods in FareCalculatorService
 *  under various scenarios
 */
/*public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    //Create an instance of FareCalculatorService
    @BeforeAll //before running all the tests
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    //Create an instance of tiket
    @BeforeEach //Before of each test
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test //represents an individual test for a specific case

    //Test in the case of a car that parks for one hour
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        //AssertEquals verifies that the first value is equal to the second
        //the first value is manually set 
        //he second value is part of the already established code
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    //Case of a motorcycle that parks for one hour
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    //Case of an unknown parking type that parks for one hour
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    //Test for a future entry time of one hour for a motorcycle
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    //Test for a motorcycle when it parks for less than an hour but more than 30 minutes
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        //45 minutes*60 seconds*1000 miliseconds
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        //indicate that is a BIKE
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        //Sets the values of the ticket (entry time, exit time, type of vehicle)
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Calculate the price of that ticket
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
        //The 0.75 comes from 45 minutes divided / by 60 (one hour), and it is multiplied by the BIKE price
    }

    @Test
    //Test for a car when it parks for less than an hour but more than 30 minutes.
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        //45 minutes*60 seconds*1000 milliseconds
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        //indicate that is a CAR
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        //Sets the values of the ticket (entry time, exit time, type of vehicle)
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        //Calculate the price of that ticket
        fareCalculatorService.calculateFare(ticket);

        //The 0.75 comes from 45 minutes divided / by 60 (one hour), and it is multiplied by the CAR price
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }


    @Test
    //Test for a motorcycle when it parks for 30 minutes
    public void calculateFareBikeWith30ParkingTime(){

        //The process is similar to the previous one
        Date inTime = new Date();

        //Test witch 30 minutes
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);//Set the entry time in the ticket
        ticket.setOutTime(outTime);//Set the exit time in the ticket
        ticket.setParkingSpot(parkingSpot);//Set the parking spot in the ticket
        fareCalculatorService.calculateFare(ticket);//call the method to calculate the fare
        //The value of the ticket is calculated with the assigned values (setInTime, setOutTime, setParkingSpot)
        assertEquals((0.0 ), ticket.getPrice() );//Expecting the method to calculate the fare
        //The 0.0 comes because when it's 30 minutes, the price is 0
        //and with the assert, it is comparing that 0.0 is equal to the price of the created ticket."
    }

    
    @Test
    //Test for a car when it parks for 30 minutes
    public void calculateFareCarWith30ParkingTime(){
        //The same as the previous method (calculateFareBikeWith30ParkingTime) 
        //but with Car
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.0 ) , ticket.getPrice());//was set to 0.0
    }

    @Test
    //Test for a car when it parks for one day
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
        ////24 because there are 24 hours in a day multiplied by the car price or rate"
    }
    //!!!!!!!!!!STEP 4 DEVELOP FUNTCTION 5% DISCOUNT!!!!!!!!!!!!
    @Test //Il s'agit d'une annotation de JUnit indiquant que la méthode suivante est un cas de test.
    // Test to verify that a vehicle with a discount ticket pays 95% of the total price.
    public void calculateFareCarWithDiscount() {//Définit une méthode publique appelée 
        //calculateFareCarWithDiscount qui sera exécutée en tant que partie de la suite de tests.
        // Current date and time Crée un objet Date appelé inTime qui représente l'instant actuel.
        Date inTime = new Date();
        
        // Simulate the vehicle being parked for 1 hours
        //Définit l'heure de l'objet inTime pour simuler que le véhicule est resté garé pendant 1 heure (en millisecondes).
        inTime.setTime(System.currentTimeMillis() - (1 * 60 * 60 * 1000));
        //Crée un autre objet Date appelé outTime qui représente l'instant actuel.
        Date outTime = new Date();

        // Create a ticket with vehicle type and marked as discount
        //Crée un objet ParkingSpot avec le numéro d'identification 1, le type de véhicule CAR et marqué
        // comme réduction (true).
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        //Crée un objet Ticket.
        Ticket ticket = new Ticket();
        //Définit l'heure d'entrée du billet avec la valeur de inTime.
        ticket.setInTime(inTime);
        //Définit l'heure de sortie du billet avec la valeur de outTime.
        ticket.setOutTime(outTime);
        //Définit l'emplacement de stationnement du billet avec l'objet parkingSpot créé précédemment.
        ticket.setParkingSpot(parkingSpot);

        // Calculate the fare with discount
        //Appelle la méthode calculateFare de l'objet fareCalculatorService en passant le billet et 
        //en indiquant qu'il bénéficie d'une réduction (true).
        fareCalculatorService.calculateFare(ticket, true);


        // Verify that the calculated price is equal to 95% of the expected total price
        //Utilise la méthode assertEquals de JUnit pour vérifier que le prix calculé du billet est égal à 95 %
        // du prix total attendu pour un véhicule CAR par heure.
        assertEquals(0.95 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    // Test to verify that a bike with a discount ticket pays 95% of the total price.
    public void calculateFareBikeWithDiscountFor() {
        // Current date and time
        Date inTime = new Date();
        
        // Simulate the bike being parked for 1 hour
        inTime.setTime(System.currentTimeMillis() - (1 * 60 * 60 * 1000));
        Date outTime = new Date();

        // Create a ticket with bike type and marked as discount
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
        Ticket ticket = new Ticket();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // Calculate the fare with discount
        fareCalculatorService.calculateFare(ticket, true);


        // Verify that the calculated price is equal to 95% of the expected total price
        assertEquals(0.95*Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }
}*/