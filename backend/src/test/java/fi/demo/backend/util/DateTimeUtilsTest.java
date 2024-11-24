package fi.demo.backend.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeUtilsTest {

    private final DateTimeUtils utils = new DateTimeUtils();

    @Test
    public void dateTimeIsDuringPeriodOfInterest() {
        // Basic case, clearly during the duration
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 30, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 1, 1, 55, 0, 0);
        Duration thresholdDuration = Duration.ofDays(1);
        assertThat(DateTimeUtils.isDuring(dt1, thresholdDuration, dt2)).isTrue();
    }

    @Test
    public void dateTimeIsDuringPeriodOfInterestWhenDateTimesAreEqual() {
        // Basic case, clearly during the duration
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 30, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 1, 1, 30, 0, 0);
        Duration thresholdDuration = Duration.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdDuration, dt2)).isTrue();
    }

    @Test
    public void dateTimeIsDuringDurationOfInterestOnTheThreshold() {
        // Exactly on the threshold
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 2, 1, 1, 0, 0);
        Duration thresholdDuration = Duration.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdDuration, dt2)).isTrue();
    }

    @Test
    public void dateTimeIsDuringDurationOfInterestInsideCornerCase() {
        // Just inside the duration
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 2, 0, 59, 59, 999);
        Duration thresholdDuration = Duration.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdDuration, dt2)).isTrue();
    }

    @Test
    public void dateTimeIsNotDuringDurationOfInterestOutsideCornerCase() {
        // Just outside (after) the duration
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 2, 1, 1, 0, 1);
        Duration thresholdDuration = Duration.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdDuration, dt2)).isFalse();
    }

    @Test
    public void dateTimeIsNotDuringDurationWhenItItBeforeTheStart() {
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 1, 0, 55, 0, 1);
        Duration thresholdDuration = Duration.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdDuration, dt2)).isFalse();
    }

    @Test
    public void dateTimeIsClearlyNotDuringDurationOfInterest() {
        // Sanity check
        LocalDateTime dt1 = LocalDateTime.of(2020, 1, 1, 1, 1, 0, 0);
        LocalDateTime dt2 = LocalDateTime.of(2020, 1, 3, 1, 1, 0, 1);
        Duration thresholdDuration = Duration.ofDays(1);

        assertThat(DateTimeUtils.isDuring(dt1, thresholdDuration, dt2)).isFalse();
    }

}
