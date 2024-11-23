package fi.demo.backend.util;

import java.time.LocalDateTime;
import java.time.Period;

public class DateTimeUtils {

    public static boolean isDuring(LocalDateTime start, Period thresholdPeriod, LocalDateTime dateTimeOfInterest) {
        return dateTimeOfInterest.isEqual(start.plus(thresholdPeriod))
                || dateTimeOfInterest.isBefore(start.plus(thresholdPeriod));
    }

}
