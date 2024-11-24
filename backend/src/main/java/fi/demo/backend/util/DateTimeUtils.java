package fi.demo.backend.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeUtils {

    /**
     * Returns whether a given datetime of interest will occur during a given duration, counting from
     * a start given datetime. When the two given datetimes are equal, method will return true.
     * @param start: DateTime when the period will start
     * @param thresholdDuration: the threshold duration.
     * @param dateTimeOfInterest: the datetime for which the method will evaluate, whether it's during
     *                          the given duration, starting from the given start -datetime.
     * @return boolean
     */

    public static boolean isDuring(LocalDateTime start,
                                   Duration thresholdDuration,
                                   LocalDateTime dateTimeOfInterest) {

        return dateTimeOfInterest.isEqual(start.plus(thresholdDuration))
                || dateTimeOfInterest.isEqual(start)
                || (dateTimeOfInterest.isBefore(start.plus(thresholdDuration))
                && dateTimeOfInterest.isAfter(start));
    }

}
