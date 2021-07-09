package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;

/**
 * The type Data base prepare service.
 */
public class DataBasePrepareService {

    /**
     * The Data base test config.
     */
    final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    /**
     * Clear data base entries.
     */
    public void clearDataBaseEntries() {
        Connection connection = null;
        try {
	        connection = dataBaseTestConfig.getConnection();

	        //set parking entries to available
	        connection.prepareStatement("update parking set available = true").execute();

	        //clear ticket entries;
	        connection.prepareStatement("truncate table ticket").execute();

        } catch (Exception e) {
	        e.printStackTrace();
        } finally {
	        dataBaseTestConfig.closeConnection(connection);
        }
    }


}
