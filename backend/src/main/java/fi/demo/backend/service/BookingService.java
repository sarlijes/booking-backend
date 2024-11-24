package fi.demo.backend.service;

import fi.demo.backend.entity.Booking;
import fi.demo.backend.repository.BookingRepository;
import fi.demo.backend.util.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Returns a list of bookings that are likely double bookings mistakenly done by the customer.
     * The logic works like so:
     * 1. Find all future bookings, sorted by their start time so that the booking to
     *    take place soonest is the first.
     * 2. Loop the list. Given booking b1, find each next booking that meets the criteria, i.e.
     *    has the same customer and will take place during a given duration of interest since the start time of
     *    the booking slot of b1.
     * @param from: LocalDateTime: only bookings with start times greater than this are included.
     * @param durationInterest: Duration of interest
     * @return All upcoming duplicate bookings
     */
    public List<Booking> findDuplicateBookings(LocalDateTime from,
                                               Duration durationInterest) {
        List<Booking> allBookings = bookingRepository
                .findByStartTimeGreaterThanOrderByStartTime(from);
        Set<Booking> duplicates = new HashSet<>();

        for (int i = 0; i < allBookings.size(); i++) {
            Booking currentBooking = allBookings.get(i);
            LocalDateTime currentStartTime = currentBooking.getStartTime();
            // Compare the current Booking against the others that come after it in the list:
            for (int j = i + 1; j < allBookings.size(); j++) {
                Booking nextBooking = allBookings.get(j);
                if (!currentBooking.getCustomer().equals(nextBooking.getCustomer())) {
                    // Definitely not a duplicate, as customers are different.
                    continue;
                }
                LocalDateTime nextStartTime = nextBooking.getStartTime();
                // Check whether the next Booking's start time is within the given duration
                // of the current Booking's start time. If yes, consider both as duplicates.
                if (DateTimeUtils.isDuring(currentStartTime, durationInterest, nextStartTime)) {
                    duplicates.add(currentBooking);
                    duplicates.add(nextBooking);
                } else {
                    // Since the list is sorted by booking's start time, no need to check further if times
                    // are not overlapping.
                    break;
                }
            }
        }
        return new ArrayList<>(duplicates);
    }

}
