package com.parkit.parkingsystem.UnitTests;

import com.parkit.parkingsystem.model.Duration;
import com.parkit.parkingsystem.service.DurationCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Duration service test.
 */
public class DurationServiceTest {

    /**
     * The constant durationCalculatorService.
     */
    private static DurationCalculatorService durationCalculatorService;

    /**
     * Sets up.
     */
    @BeforeAll
    private static void setUp() {
        durationCalculatorService = new DurationCalculatorService();
    }

    /**
     * Sets up duration should return correct duration.
     */
    @Test
    public void setUpDuration_shouldReturnCorrectDuration() {

        // GIVEN
        LocalDateTime inTime = LocalDateTime.now().minusHours(1);
        LocalDateTime outTime = LocalDateTime.now();

        // WHEN
        Duration duration = durationCalculatorService.calculateDifference_WithFreeTime(inTime, outTime);

        // THEN
        assertThat(duration.getHour()).isEqualTo(1);
        assertThat(duration.getMinute()).isEqualTo(0);
        assertThat(duration.getDay()).isEqualTo(0);
        assertThat(duration.getMonth()).isEqualTo(0);
        assertThat(duration.getYear()).isEqualTo(0);

    }
}
