package fi.demo.backend.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeUtilsTest {

    private final DateTimeUtils utils = new DateTimeUtils();

    @Test
    public void dateTimeIsDuringPeriodOfInterest() {
        // Basic case, clearly during the period
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 30, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 1, 1, 55, 0, 0);
        Period thresholdPeriod = Period.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdPeriod, dt2)).isTrue();
    }

    @Test
    public void dateTimeIsDuringPeriodOfInterestWhenDateTimesAreEqual() {
        // Basic case, clearly during the period
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 30, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 1, 1, 30, 0, 0);
        Period thresholdPeriod = Period.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdPeriod, dt2)).isTrue();
    }

    @Test
    public void dateTimeIsDuringPeriodOfInterestOnTheThreshold() {
        // Exactly on the threshold
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 2, 1, 1, 0, 0);
        Period thresholdPeriod = Period.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdPeriod, dt2)).isTrue();
    }

    @Test
    public void dateTimeIsDuringPeriodOfInterestInsideCornerCase() {
        // Just inside the period
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 2, 0, 59, 59, 999);
        Period thresholdPeriod = Period.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdPeriod, dt2)).isTrue();
    }

    @Test
    public void dateTimeIsNotDuringPeriodOfInterestOutsideCornerCase() {
        // Just outside (after) the period
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 2, 1, 1, 0, 1);
        Period thresholdPeriod = Period.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdPeriod, dt2)).isFalse();
    }

    @Test
    public void dateTimeIsNotDuringPeriodWhenItItBeforeTheStart() {
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 1, 0, 55, 0, 1);
        Period thresholdPeriod = Period.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdPeriod, dt2)).isFalse();
    }

    @Test
    public void dateTimeIsClearlyNotDuringPeriodOfInterest() {
        // Sanity check
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 3, 1, 1, 0, 1);
        Period thresholdPeriod = Period.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdPeriod, dt2)).isFalse();
    }

}
