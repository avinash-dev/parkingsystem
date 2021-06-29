package com.parkit.parkingsystem.UnitTests;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.parkit.parkingsystem.config.DataBaseConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Data base config test.
 */
@ExtendWith(MockitoExtension.class)
public class DataBaseConfigTest {

    /**
     * The constant dataBaseConfig.
     */
    private static DataBaseConfig dataBaseConfig;

    /**
     * The Ps.
     */
    @Mock
    PreparedStatement ps;


    /**
     * Data base config instance.
     */
    @BeforeAll
    static void DataBaseConfigInstance() {
        dataBaseConfig = new DataBaseConfig();
    }

    /**
     * Db connection should be true.
     */
    @Test
    public void DBConnection_ShouldBeTrue() {
        // GIVEN
        Connection testCon = null;

        // WHEN
        try {
            testCon = dataBaseConfig.getConnection();
        } catch (SQLException | ClassNotFoundException sqlException) {
            sqlException.printStackTrace();
        }

        // THEN
        assertThat(testCon).isInstanceOf(ConnectionImpl.class);
        dataBaseConfig.closeConnection(testCon);
    }


    /**
     * Db closing result set should be void.
     */
    @Test
    public void DBClosingResultSet_ShouldBeVoid() {
        // GIVEN
        Connection testCon = null;

        // WHEN
        try {
            testCon = dataBaseConfig.getConnection();
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(testCon);

        } catch (SQLException | ClassNotFoundException sqlException) {
            sqlException.printStackTrace();
        }

        // THEN
        assertThat(testCon).isInstanceOf(ConnectionImpl.class);
    }
}
