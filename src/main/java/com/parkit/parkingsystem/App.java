package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type App.
 */
public class App {
    /**
     * The constant logger.
     */
    private static final Logger logger = LogManager.getLogger("App");

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
