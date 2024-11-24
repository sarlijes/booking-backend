package fi.demo.backend.service;

import fi.demo.backend.entity.Booking;
import fi.demo.backend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findDuplicateBookings(LocalDateTime from) {
        List<Booking> futureBookings = bookingRepository.findByStartTimeGreaterThanOrderByStartTime(from);
        return futureBookings;
    }

}
