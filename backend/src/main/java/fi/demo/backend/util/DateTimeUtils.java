package fi.demo.backend.util;

import java.time.LocalDateTime;
import java.time.Period;

public class DateTimeUtils {

    /**
     * Returns whether a given datetime of interest will occur during a given period of time, counting from
     * a start given datetime. When the two given datetimes are equal, method will return true.
     * @param start: DateTime when the period will start
     * @param thresholdPeriod: the threshold Period.
     * @param dateTimeOfInterest: the datetime for which the method will evaluate, whether it's during
     *                          the given period, starting from the given start -datetime.
     * @return boolean
     */
    public static boolean isDuring(LocalDateTime start, Period thresholdPeriod, LocalDateTime dateTimeOfInterest) {
        return dateTimeOfInterest.isEqual(start.plus(thresholdPeriod))
                || dateTimeOfInterest.isEqual(start)
                || (dateTimeOfInterest.isBefore(start.plus(thresholdPeriod))
                && dateTimeOfInterest.isAfter(start));
    }

}
