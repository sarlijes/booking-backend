package fi.demo.backend.controller;

import fi.demo.backend.entity.Booking;
import fi.demo.backend.service.BookingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/booking/findDuplicateBookings")
    public List<Booking> findDuplicateBookings() {
        return bookingService.findDuplicateBookings(
                LocalDateTime.now(),
                Duration.ofDays(1)
        );
    }

}
