package fi.demo.backend.util;

import java.time.LocalDateTime;
import java.time.Period;

public class DateTimeUtils {

    /**
     * Returns whether a given datetime of interest will occur during a given period of time, counting from
     * a start given datetime.
     * @param start
     * @param thresholdPeriod
     * @param dateTimeOfInterest
     * @return
     */
    public static boolean isDuring(LocalDateTime start, Period thresholdPeriod, LocalDateTime dateTimeOfInterest) {
        return dateTimeOfInterest.isEqual(start.plus(thresholdPeriod))
                || dateTimeOfInterest.isEqual(start)
                || (dateTimeOfInterest.isBefore(start.plus(thresholdPeriod))
                && dateTimeOfInterest.isAfter(start));
    }

}
