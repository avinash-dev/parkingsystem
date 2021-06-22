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

@ExtendWith(MockitoExtension.class)
public class DataBaseConfigTest {

    private static DataBaseConfig dataBaseConfig;

    @Mock
    PreparedStatement ps;


    @BeforeAll
    static void DataBaseConfigInstance() {
        dataBaseConfig = new DataBaseConfig();
    }

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
