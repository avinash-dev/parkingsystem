package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * The type Input reader util.
 */
public class InputReaderUtil {

    /**
     * The constant scan.
     */
    private static Scanner scan = new Scanner(System.in);
    /**
     * The constant logger.
     */
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    public InputReaderUtil() {
    }

    public InputReaderUtil(Scanner scanner) {
        InputReaderUtil.scan = scanner;
    }

    /**
     * Read selection int.
     *
     * @return the int
     */
    public int readSelection() {
        try {
            return Integer.parseInt(scan.nextLine());
        } catch (Exception e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     * Read vehicle registration number string.
     *
     * @return the string
     */
    public String readVehicleRegistrationNumber() {
        try {
            String vehicleRegNumber = scan.nextLine();
            if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        } catch (Exception e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }


}
