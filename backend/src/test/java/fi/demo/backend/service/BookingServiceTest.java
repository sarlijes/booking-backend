package fi.demo.backend.service;

import fi.demo.backend.entity.Booking;
import fi.demo.backend.entity.Customer;
import fi.demo.backend.repository.BookingRepository;
import fi.demo.backend.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    private Booking firstBooking = null;

    @Test
    public void dependenciesAreInjected() {
        assertThat(bookingService).isNotNull();
        assertThat(bookingRepository).isNotNull();
        assertThat(customerRepository).isNotNull();
    }

    public void mock() {
        Customer c1 = Customer.builder().lastName("Brent").id(1L).build();
        Customer c2 = Customer.builder().lastName("Canterbury").id(2L).build();
        List<Booking> bookings = new ArrayList<>();
        // A booking for a certain customer (c1):
        firstBooking = Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 9, 0))
                .customer(c1)
                .build();
        bookings.add(firstBooking);
        // Corner case (just inside the period of interest since the first booking), same customer:
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 8, 9, 0))
                .customer(c1)
                .build());
        // Corner case (just outside the period of interest since the second booking), same customer:
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 9, 9, 15))
                .customer(c1)
                .build());
        // Same day as the first one, but different customer:
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 9, 30))
                .customer(c2)
                .build());
        when(bookingRepository
                .findByStartTimeGreaterThanOrderByStartTime(firstBooking.getStartTime()))
                .thenReturn(bookings);
    }

    @Test
    public void canListDuplicatesWhenCustomerHasTwoBookings() {
        mock();
        // Basic case
        List<Booking> result = bookingService
                .findDuplicateBookings(firstBooking.getStartTime());
        assertThat(result).hasSize(2);
    }

}
